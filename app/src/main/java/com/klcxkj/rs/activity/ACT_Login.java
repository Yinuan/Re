package com.klcxkj.rs.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:登录
 */
public class ACT_Login extends ACT_Network {
	public static String url = RSApplication.BASE_URL+"tStudent/sTudentLogIn?";
//	private ImageView mImageBack;
	private Button loginBtn;
	private EditText mEditTextUserName;
	private EditText mEditTextUserPassword;
	private TextView mForgetPassword, text_register;
	private View progressbar,top_menu,home_null;
	private String mPassword;
	private int userType;//用户类型
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		initView();
		bindEvent();
		initViewFromDate();
	}
	
	private void initViewFromDate() {
		// TODO Auto-generated method stub
		String phone = getIntent().getStringExtra("phone");
		if (!TextUtils.isEmpty(phone)) {
			mEditTextUserName.setText(phone);
		}
	}


	
	private void bindEvent() {
		setupUI(findViewById(R.id.root_layout));

		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(mEditTextUserName.getText()) || TextUtils.isEmpty(mEditTextUserPassword.getText())) {
					toast("手机号或密码不能为空");
					return;
				}
				
				loginBtn.setClickable(false);
				progressbar.setVisibility(View.VISIBLE);
				StringBuffer sb = new StringBuffer(url);
				sb.append("telPhone="+mEditTextUserName.getText());//15112622639
				sb.append("&logPwd="+ mEditTextUserPassword.getText());//yl123456
				mPassword = mEditTextUserPassword.getText().toString();
				sendGetRequest(sb.toString());
//				mHandler.sendMessageDelayed(Message.obtain(), 1000);
			}
		});
		mForgetPassword.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				startActivity(new Intent(ACT_Login.this, ACT_ForgetPassword.class));
			}
		});
		text_register.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(ACT_Login.this, ACT_Register.class);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		loginBtn = (Button) findViewById(R.id.login);
		progressbar = findViewById(R.id.progressbar);
		mEditTextUserName = (EditText)findViewById(R.id.username);
		mEditTextUserPassword = (EditText)findViewById(R.id.password);
		mForgetPassword =  (TextView)findViewById(R.id.text_forget_password);
		text_register =  (TextView)findViewById(R.id.text_register);
		top_menu =findViewById(R.id.top_menu_view_1);
		home_null =findViewById(R.id.top_menu_view_2);
		if (aVersion){
			top_menu.setVisibility(View.VISIBLE);
			home_null.setVisibility(View.VISIBLE);
		}else {
			top_menu.setVisibility(View.GONE);
			home_null.setVisibility(View.GONE);
		}
	}
	
	private void afterLoginSucc(){
		progressbar.setVisibility(View.GONE);
		Intent intent = new Intent(this, ACT_MainUI.class);
		startActivity(intent);
		finish();
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			afterLoginSucc();
		};
	};

	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		toast("登录成功");
		Log.d("ACT_Login", "result:" + result.toString());
		//在这里设置极光推送的标记
		Gson gson =new Gson();
		UserInfo userInfo =gson.fromJson(result.toString(),UserInfo.class);
		userType =userInfo.getUserType();
		Log.d("ACT_Login", "极光推送alias："+userInfo.getAlias());
		Log.d("ACT_Login", "极光推送tag："+userInfo.getTags());
		Set<String> set =new HashSet<>();
		String[] str =userInfo.getTags().split(",");
		for (String item: str) {
			if (!TextUtils.isEmpty(item)){
				if (StringUtils.isValidTagAndAlias(item)){
					set.add(item);
				}
			}
		}
		JPushInterface.setAliasAndTags(ACT_Login.this,userInfo.getAlias(),set,mAliasCallback);
		//JPushInterface.setAlias(ACT_Login.this,1,userInfo.getAlias());
		//JPushInterface.setTags(ACT_Login.this,2,set);
		//激光成功没
		AppPreference.getInstance().saveLoginUser(userInfo);
		//保存密码
		AppPreference.getInstance().savePassWord(mEditTextUserPassword.getText().toString());
		return 0;
	}

	@Override
	protected void loadDatas() {
		loginBtn.setClickable(true);
		progressbar.setVisibility(View.GONE);
		//判断是用户还是老板
		if (userType ==0){ //普通用户
			startActivity(new Intent(ACT_Login.this, ACT_MainUI.class));
		}

		finish();
	}

	@Override
	protected void loadError(JSONObject result) {
		// TODO Auto-generated method stub
		loginBtn.setClickable(true);
		progressbar.setVisibility(View.GONE);
	}

	/**
	 * 极光推送的通知栏设置
	 */
	private void setStyleCustom() {
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(ACT_Login.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
		builder.layoutIconDrawable = R.drawable.jpush_notification_icon;
		builder.developerArg0 = "developerArg2";
		JPushInterface.setPushNotificationBuilder(2, builder);


	}

	/**
	 * 极光推送设置alias的回调
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback()
	{
		@Override
		public void gotResult(int code, String alias, Set<String> tags)
		{
			String logs;
			switch (code)
			{
				case 0:
					logs = "Set tag and alias success";
					Log.d("-----", "极光推送设置别名成功");
					setStyleCustom();
					break;

				case 6002:

					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.d("-----", "极光推送设置别名失败");
					break;

				default:
					logs = "Failed with errorCode = " + code;
					Log.d("----", "极光推送设置别名"+code);
			}

			// ExampleUtil.showToast(logs, getApplicationContext());
		}

	};


}
