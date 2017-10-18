package com.klcxkj.rs.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.BaseBo;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:意见反馈
 */
public class ACT_MeRidicule extends ACT_Network{

	private String url = RSApplication.BASE_URL+"tStudent/complainReport?";
	private Button mButtonSubmit;
	private EditText mEditEidiculeDetail;//, mEditRidiculeTitle;
	private LoadingDialogProgress progress;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_me_ridicule);
		findViews();
		bindEvent();

	}
	
	private void findViews() {
		mEditEidiculeDetail =  (EditText)this.findViewById(R.id.edit_ridicule_detail);
		mButtonSubmit = (Button)this.findViewById(R.id.buttonSubmit);
		showMenu("意见反馈");
	}
	
	private void bindEvent() {
		setupUI(findViewById(R.id.root_layout));

		mButtonSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(mEditEidiculeDetail.getText())) {
					toast("请输入投诉建议内容");
					return;
				}
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("telPhone", getNewUser().getTelPhone());
					params.put("repTitle", "意见反馈");
					params.put("repContent", mEditEidiculeDetail.getText().toString());
					params.put("ServerIP", getNewUser().getServerIP());
					params.put("ServerPort", getNewUser().getServerPort()+"");
					sendPostRequest(url, params);
			progress = GlobalTools.getInstance().showDailog(ACT_MeRidicule.this,"提交..");

			}
		});
		

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
		Log.d("ACT_MeRidicule", "json:" + json);
		progress.dismiss();
		BaseBo result = new Gson().fromJson(json.toString(), BaseBo.class);
		if(result.isSuccess()){
			//toast(result.getMsg());
			//finish();
			showPop("感谢你宝贵的意见~");
		}else{
			toast(R.string.operate_error);
		}
	}
	
	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		return 0;
	}
	@Override
	protected void loadDatas() {}	
	@Override
	protected void loadError(JSONObject result) {}

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
		View view1 = LayoutInflater.from(ACT_MeRidicule.this).inflate(R.layout.pop_style_1b, null);
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
