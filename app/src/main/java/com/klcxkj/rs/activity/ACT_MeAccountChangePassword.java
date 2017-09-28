package com.klcxkj.rs.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:密码修改
 */
public class ACT_MeAccountChangePassword extends ACT_Network{
//	http://211.149.224.58:6002/tStudent/updateLogInPwd?telPhone=111&LogPwd=22&PrjID=5&EmployeeID=1&UserPwd=111
	private String url = RSApplication.BASE_URL+"tStudent/updateLogInPwd?";
	private EditText mEditOldPassword;
	private EditText mEditNewPassword;
	private Button mButtonSubmit;
	private ImageView mImageBack;
	private CheckBox mImageShowOldPassword, mImageShowNewPassword;
	private LoadingDialogProgress progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_me_account_change_password);
		initView();
		bindEvent();
	}
	
	private void bindEvent() {
		// TODO Auto-generated method stub
		setupUI(findViewById(R.id.root_layout));
		mImageBack.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				ACT_MeAccountChangePassword.this.finish();
			}
		});
		mButtonSubmit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(mEditOldPassword.getText())) {
					toast("旧密码不能为空");
					return;
				}
				if (TextUtils.isEmpty(mEditNewPassword.getText())) {
					toast("新密码不能为空");
					return;
				}
				if (mEditNewPassword.length() < 6) {
					toast("密码不能少于6位");
					return;
				}
				mButtonSubmit.setClickable(false);
				progress = GlobalTools.getInstance().showDailog(ACT_MeAccountChangePassword.this,"修改..");
//				progressbar.setVisibility(View.VISIBLE);
				UserInfo userInfo =AppPreference.getInstance().getUserInfo();
				if (userInfo !=null){
					Log.d("ACT_MeAccountChangePass", userInfo.getTelPhone());
				}else {
					Log.d("ACT_MeAccountChangePass", "userinfo == null");
				}
				StringBuffer sb = new StringBuffer(url);
				sb.append("telPhone="+getNewUser().getTelPhone());
				sb.append("&LogPwd="+mEditNewPassword.getText());
				sb.append("&PrjID="+getNewUser().getPrjID());
				sb.append("&EmployeeID="+getNewUser().getEmployeeID());
				sb.append("&UserPwd="+mEditOldPassword.getText());
				sb.append("&ServerIP="+getNewUser().getServerIP());
				sb.append("&ServerPort="+ getNewUser().getServerPort());
				Log.d("ACT_MeAccountChangePass", "sb:" + sb);
				sendGetRequest(sb.toString());

			}
		});
		mImageShowOldPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){  
					mEditOldPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                }else{  
                	mEditOldPassword.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                }  
				mEditOldPassword.setSelection(mEditOldPassword.getText().length());
			}
		});
		mImageShowNewPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){  
					mEditNewPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                }else{  
                	mEditNewPassword.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                }  
				mEditNewPassword.setSelection(mEditNewPassword.getText().length());
			}
		});
	}
	private void initView() {
		mImageBack = (ImageView)this.findViewById(R.id.imageBack);
		mEditOldPassword =  (EditText)this.findViewById(R.id.edit_old_password);
		mEditNewPassword =  (EditText)this.findViewById(R.id.edit_new_password);
		mButtonSubmit = (Button)this.findViewById(R.id.buttonSubmit);
		mImageShowOldPassword = (CheckBox) this.findViewById(R.id.checkBox_show_old_passward);
		mImageShowNewPassword = (CheckBox) this.findViewById(R.id.checkBox_show_new_passward);
		showMenu("修改密码");
	}
	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Log.d("ACT_MeAccountChangePass", "result:" + result);

	/*	Gson gson =new Gson();
		UpdatePassResult result1 =gson.fromJson(result.toString(),UpdatePassResult.class);
		if (result1.getMsg().equals("成功")){
			Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
			finish();
		}*/
		return 0;
	}

	@Override
	protected void loadDatas() {
		progress.dismiss();
		AppPreference.getInstance().savePassWord(mEditNewPassword.getText().toString());
		Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
		String paa=AppPreference.getInstance().getPassWord();
		Log.d("ACT_MeAccountChangePass", "passsssss:"+paa);
		finish();

	}

	@Override
	protected void loadError(JSONObject result) {
		mButtonSubmit.setClickable(true);
		progress.dismiss();
	}

}
