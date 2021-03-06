package com.klcxkj.rs.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.klcxkj.imagepicker.ImagePicker;
import com.klcxkj.imagepicker.bean.ImageItem;
import com.klcxkj.imagepicker.ui.ImageGridActivity;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.adapter.GRepairApater;
import com.klcxkj.rs.bo.Ban;
import com.klcxkj.rs.bo.BaseBo;
import com.klcxkj.rs.bo.QINiu;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.CircleImageView;
import com.klcxkj.rs.widget.LoadingDialogProgress;
import com.klcxkj.rs.wxdemo.GlideImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:故障报修
 */
public class ACT_MeRepair extends ACT_Network {

	
	private String REPAIR = RSApplication.BASE_URL+"tStudent/studentReportErrDev?"; //故障报修
	private String QINIU_IMAGE = RSApplication.BASE_URL+"tStudent/getPicToken";//七牛
	private Button mButtonSubmit;
	private EditText mEditEidiculeDetail;//, mEditRidiculeTitle;
	private GridView gridView;
	private GRepairApater gRepairApater;
	private List<Ban> data;
	private ImageView add_image;
	private ImagePicker imagePicker;
	private CircleImageView iconDelete;

	private String rTitle ="刷卡不出水";
	private StringBuffer rContent;
	private QINiu qiNiu;
	private LoadingDialogProgress progress;

	private Handler mHandle =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					rContent =new StringBuffer();
					for (int i = 0; i <data.size() ; i++) {
						if (data.get(i).getBuildingID().equals("1")){
							rContent.append(data.get(i).getBuildingName()+";");
						}
					}
					mEditEidiculeDetail.setText(rContent.toString());
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_me_repair);
		imagePicker =ImagePicker.getInstance();
		imagePicker.setImageLoader(new GlideImageLoader());
		findViews();
		bindEvent();

	}
	
	private void findViews() {
		showMenu("故障报修");
		iconDelete = (CircleImageView) findViewById(R.id.delete_icon);
		mEditEidiculeDetail =  (EditText)findViewById(R.id.edit_ridicule_detail);

		mButtonSubmit = (Button)findViewById(R.id.buttonSubmit);

		add_image = (ImageView) findViewById(R.id.image_picker);

		gridView = (GridView) findViewById(R.id.gridview_repair);
		gRepairApater =new GRepairApater(this);
		data =new ArrayList<>();
		data.add(new Ban("0","刷卡不出水"));
		data.add(new Ban("0","读卡报错"));
		data.add(new Ban("0","领款不成功"));
		gRepairApater.setList(data);
		gridView.setAdapter(gRepairApater);

	}
	
	private void bindEvent() {
		setupUI(findViewById(R.id.root_layout));

		add_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//有图片的时候
				ImagePicker.getInstance().setSelectLimit(1);
				Intent intent1 = new Intent(ACT_MeRepair.this, ImageGridActivity.class);
				startActivityForResult(intent1, REQUEST_CODE_SELECT);
			}
		});
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if (data.get(i).getBuildingID().equals("0")){
					/*for (int j = 0; j < data.size(); j++) {
						data.get(j).setBuildingID("0");
					}*/
					data.get(i).setBuildingID("1");
					rTitle =data.get(i).getBuildingName();
				}else {
					data.get(i).setBuildingID("0");
				}

				mHandle.sendEmptyMessage(0);
				gRepairApater.notifyDataSetChanged();
			}
		});

		
		mButtonSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
//				if (TextUtils.isEmpty(mEditRidiculeTitle.getText())) {
//					toast("请输入标题");
//					return;
//				}
				if (TextUtils.isEmpty(mEditEidiculeDetail.getText())) {
					toast("请输入故障报修内容");
					return;
				}
//				if (url == null) {
//					toast("请选择吐槽类型");
//					return;
//				}
				progress = GlobalTools.getInstance().showDailog(ACT_MeRepair.this,"提交..");
				if (images !=null && images.size()>0){
					StringBuffer sb=new StringBuffer(QINIU_IMAGE);
					sendGetRequest(sb.toString());
				}else {
					submitReportToServer("");
				}
			}
		});
		iconDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (images ==null){
					return;
				}
				images.clear();
				add_image.setImageResource(R.mipmap.add_imagepicker);
				iconDelete.setVisibility(View.GONE);
			}
		});

	}

	private void submitReportToServer(String reportImg){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("PrjID", getNewUser().getPrjID()+"");
		params.put("telPhone",getNewUser().getTelPhone());
		params.put("EmployeeID", getNewUser().getEmployeeID()+"");
		params.put("RepTitle", "故障报修");
		params.put("RepContent", mEditEidiculeDetail.getText().toString());
		params.put("ServerIP", getNewUser().getServerIP());
		params.put("ServerPort", getNewUser().getServerPort()+"");
		params.put("reportImg", reportImg);
		sendPostRequest(REPAIR, params);
	}
	
	@Override
	protected void handleErrorResponse(String url, VolleyError error) {
		super.handleErrorResponse(url, error);
		progress.dismiss();
		if(error instanceof TimeoutError){
    		toast(R.string.timeout_error);
    	}else{
    		toast(R.string.operate_error);
    	}
	}
	
	@Override
	protected void handleResponse(String url, JSONObject json) {
		super.handleResponse(url, json);
		progress.dismiss();
		Log.d("submit", "json:" + json);
		BaseBo result = new Gson().fromJson(json.toString(), BaseBo.class);
		if(result.isSuccess()){
			//toast("提交成功");
		//	finish();
			showPop("提交成功!");
		}else{
			toast(R.string.operate_error);
		}
	}
	
	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {

		Log.d("qiniuyun", "result:" + result);
		 Gson gson =new Gson();
		 qiNiu =gson.fromJson(result.toString(),QINiu.class);
		return 0;
	}
	@Override
	protected void loadDatas() {
		//上传图片
		if (qiNiu.getToken() !=null){
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String pathName = df.format(new Date());
			String reportImg =upLoadImage(images.get(0).getPath(),qiNiu.getToken(),pathName);
			Log.d("ACT_MeRepair","reportImg:=="+ reportImg);
			submitReportToServer(qiNiu.getDomainName()+pathName);
		}else {
			submitReportToServer("");
		}
	}
	@Override
	protected void loadError(JSONObject result) {
		progress.dismiss();

	}

	ArrayList<ImageItem> images = null;
	public static final int IMAGE_ITEM_ADD = -1;
	public static final int REQUEST_CODE_SELECT = 100;
	public static final int REQUEST_CODE_PREVIEW = 101;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
			//添加图片返回
			if (data != null && requestCode == REQUEST_CODE_SELECT) {
				images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
				Log.d("ACT_MeRepair", "images.get(0):" + images.get(0).getPath());
				Log.d("ACT_MeRepair", "images:" + images);
				if (images != null) {
					iconDelete.setVisibility(View.VISIBLE);
					Glide.with(ACT_MeRepair.this)
							.load(images.get(0).getPath())
							.crossFade()
							.error(R.mipmap.add_imagepicker)
							.into(add_image);
				}
			}
		} else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
			//预览图片返回
			if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
				images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
				if (images != null) {
					Glide.with(ACT_MeRepair.this)
							.load(images.get(0))
							.crossFade()
							.into(add_image);
				}
			}
		}
	}

	/**
	 * 获取屏幕宽度
	 * @return
	 */
	private int getWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels = metrics.widthPixels;
		int width =(widthPixels*3)/5;
		return  width;
	}
	private void showPop(String str) {
		View view1 = LayoutInflater.from(ACT_MeRepair.this).inflate(R.layout.pop_style_1b, null);
		TextView title = (TextView) view1.findViewById(R.id.pop_title);
		Button btn = (Button) view1.findViewById(R.id.pop_btn);
		title.setText(str);
		btn.setText("确定");

		final PopupWindow popupWindow = new PopupWindow(view1, getWidth(),
				ViewGroup.LayoutParams.WRAP_CONTENT);
		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(cd);
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);
		popupWindow.setFocusable(false);// 取得焦点
		//注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		//点击外部消失
		//  popupWindow.setOutsideTouchable(true);
		//设置可以点击
		popupWindow.setTouchable(true);
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 软键盘不会挡着popupwindow
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
		//popupWindow.showAsDropDown(mSubmit);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				finish();
			}
		});
		// 监听菜单的关闭事件
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});
		// 监听触屏事件
		popupWindow.setTouchInterceptor(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				return false;
			}
		});
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			//在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
	}



}
