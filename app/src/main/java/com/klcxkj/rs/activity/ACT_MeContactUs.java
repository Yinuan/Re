package com.klcxkj.rs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.ContactUs;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:联系我们
 */
public class ACT_MeContactUs extends ACT_Network implements View.OnClickListener{

	private String CONTACT_US = RSApplication.BASE_URL+"tStudent/getConnectInfo?";

	private RelativeLayout us_one; //版权申明
	private RelativeLayout us_two; //免责申明
	private RelativeLayout us_three; //服务电话
	private TextView us_tell;  //电话号码
	private TextView version;  //版本号
	private ContactUs contactUs;  //服务器获取的解析类
	private LoadingDialogProgress progress;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_me_contact_us);
		initView();
		bindEvent();
		initdata();
	}

	private void initdata() {
		version.setText("校园热水 V"+getLocalVersion(ACT_MeContactUs.this));
		progress = GlobalTools.getInstance().showDailog(this,"加载..");
		StringBuffer sb2 = new StringBuffer(CONTACT_US);
		sb2.append("PrjID="+getNewUser().getPrjID());
		sb2.append("&ServerIP="+getNewUser().getServerIP());
		sb2.append("&ServerPort="+ getNewUser().getServerPort());
		sendGetRequest(sb2.toString());
	}

	private void initView() {
		us_one = (RelativeLayout) findViewById(R.id.contact_us_one);
		us_two = (RelativeLayout) findViewById(R.id.contact_us_two);
		us_three = (RelativeLayout) findViewById(R.id.contact_us_three);
		us_tell = (TextView) findViewById(R.id.contact_us_tell);
		version = (TextView) findViewById(R.id.cantact_us_txt);
		showMenu("联系我们");
	}
	private void bindEvent() {

		us_one.setOnClickListener(this);
		us_two.setOnClickListener(this);
		us_three.setOnClickListener(this);
	}
	
	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Log.d("ACT_MeContactUs", "result:" + result);
		Gson gson =new Gson();
		contactUs =gson.fromJson(result.toString(),ContactUs.class);

		return 0;
	}

	@Override
	protected void loadDatas() {
		us_tell.setText(contactUs.getTelPhone());

		progress.dismiss();
	}
	@Override
	protected void loadError(JSONObject result) {

		progress.dismiss();
	}

	/**
	 * 获取当前版本号
	 * @param ctx
	 * @return
	 */
	public static String getLocalVersion(Context ctx) {
		String localVersion = "";
		try {
			PackageInfo packageInfo = ctx.getApplicationContext()
					.getPackageManager()
					.getPackageInfo(ctx.getPackageName(), 0);
			localVersion = packageInfo.versionName;
			Log.d("TAG", "本软件的版本号。。" + localVersion);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return localVersion;
	}

	@Override
	public void onClick(View view) {
			switch (view.getId()){
				case R.id.contact_us_one:  //版权
					Intent intent =new Intent(ACT_MeContactUs.this,ACT_Html.class);
					intent.putExtra("htmlUrl",contactUs.getVersionContent());
					intent.putExtra("htmlName","version");
					startActivity(intent);
					break;
				case R.id.contact_us_two:  //免责
					Intent intent2 =new Intent(ACT_MeContactUs.this,ACT_Html.class);
					intent2.putExtra("htmlUrl",contactUs.getDutyContent());
					intent2.putExtra("htmlName","duty");
					startActivity(intent2);
					break;
				case R.id.contact_us_three:  //电话
					showPop();
					break;

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
		int width =(widthPixels*3)/4;
		return  width;
	}

	private void showPop() {
		View view1 = LayoutInflater.from(ACT_MeContactUs.this).inflate(R.layout.pop_style_2, null);
		TextView title = (TextView) view1.findViewById(R.id.pop_title);
		TextView content = (TextView) view1.findViewById(R.id.pop_content);
		Button btn_ok = (Button) view1.findViewById(R.id.pop_btn_confrim);
		Button btn_cacle = (Button) view1.findViewById(R.id.pop_btn_cancle);
		title.setText("提示");
		content.setText("是否拨打"+us_tell.getText().toString()+"?");
		btn_ok.setText("确定");
		btn_cacle.setText("取消");

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
		btn_cacle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				//拨打电话
				String tellPhone =us_tell.getText().toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tellPhone));
				startActivity(intent);
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
