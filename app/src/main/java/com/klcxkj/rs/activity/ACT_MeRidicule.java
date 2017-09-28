package com.klcxkj.rs.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
			toast(result.getMsg());
			finish();
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
}
