package com.klcxkj.rs.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.SendSmsResponse;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:注册
 */
public class ACT_Register extends ACT_Network {
	public static final int SMS_SUCC = 1;
	public static final int SMS_FAIL = 2;
	public static final int SMS_TIME = 3;

	// public static String send_sms_url = RSApplication.BASE_URL
	// + "tStudent/studentSendSms?";

	public static String send_sms_url = RSApplication.BASE_URL + "tStudent/studentSendSmsStr?";

	public static String register_url = RSApplication.BASE_URL + "tStudent/studentRegedit?";
	private static final String serviceHtml =RSApplication.BASE_URL+"helpFile/serviceRule.html";
			//RSApplication.BASE_URL+"helpFile/serviceRule.html";
	String rightCode;

	private Button mButtonSubmit;
	private RelativeLayout mLayoutCity;
	private RelativeLayout mLayoutSchool;
	private TextView mTextVerification;
	private TextView mTextViewCity;
	private TextView mTextViewSchool;
	private TextView register_login;
	private EditText mEditTextPhone;
	private EditText mEditTextPassword;
	private EditText mEditTextVerification;
	private CheckBox mImageShowPassword;
	private String cityId;
	private String schoolId;
	private TextView server_html;//服务条款
	private View view1,view2;

	private final Timer timer = new Timer();
	private TimerTask task;
	private int time = 60;
	private HashMap<String, String> phoneAndCode = new HashMap<String, String>();
	private LoadingDialogProgress progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_register);
		initView();
		bindEvent();
	}


	private void bindEvent() {
		//城市选择
		mLayoutCity.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent(ACT_Register.this,
						ACT_CityList.class);
				startActivityForResult(intent, 1);
			}
		});
		//学校选择
		mLayoutSchool.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if (TextUtils.isEmpty(mTextViewCity.getText())) {
					toast("请先选择学校所在城市");
					Intent intent = new Intent(ACT_Register.this,
							ACT_CityList.class);
					startActivityForResult(intent, 1);
					return;
				}
				Intent intent = new Intent(ACT_Register.this,
						ACT_SchoolList.class);
				intent.putExtra("cityId", cityId);
				startActivityForResult(intent, 2);
			}
		});
		//验证码
		mTextVerification.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if (mEditTextPhone.getText().toString().trim().length() != 11) {
					toast("手机号码格式不正确");
					return;
				}
				mTextVerification.setEnabled(false);
				String mobile = mEditTextPhone.getText().toString();
				sendSMS(mobile);

			}
		});
		//提交按钮
		mButtonSubmit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				if (TextUtils.isEmpty(mTextViewCity.getText())) {
					toast("请选择城市");
					return;
				}
				if (TextUtils.isEmpty(mTextViewSchool.getText())) {
					toast("请选择学校");
					return;
				}
				if (TextUtils.isEmpty(mEditTextPhone.getText())) {
					toast("请输入手机号码");
					return;
				}
				if (mEditTextPhone.getText().toString().trim().length() != 11) {
					toast("手机号码格式不正确");
					return;
				}
				if (TextUtils.isEmpty(rightCode)) {
					toast("请获取验证码");
					return;
				}
				if (TextUtils.isEmpty(mEditTextVerification.getText()
						.toString())) {
					toast("请输入验证码");
					return;
				}
				if (rightCode != null
						&& !rightCode.equals(mEditTextVerification.getText()
								.toString())) {
					toast("验证码不正确");
					return;
				}
				if (!phoneAndCode.containsKey(mEditTextPhone.getText()
						.toString())) {
					toast("验证码与手机不匹配，请重新获取");
					return;
				}
				if (mEditTextPassword.length() < 6) {
					toast("密码不能少于6位");
					return;
				}
				progress = GlobalTools.getInstance().showDailog(ACT_Register.this,"注册中..");
				StringBuffer sb = new StringBuffer(register_url);
				sb.append("prjRecId=" + schoolId);
				sb.append("&telPhone=" + mEditTextPhone.getText());
				sb.append("&logPwd=" + mEditTextPassword.getText());
				sendGetRequest(sb.toString());
				mButtonSubmit.setEnabled(false);
			}
		});


		mImageShowPassword
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							mEditTextPassword
									.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else {
							mEditTextPassword
									.setInputType(InputType.TYPE_CLASS_NUMBER
											| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
						}
						mEditTextPassword.setSelection(mEditTextPassword
								.getText().length());
					}
				});

		//注册按钮颜色变化
		mEditTextPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length()==6){
					mButtonSubmit.setBackgroundResource(R.drawable.btn_yuyue);
					mButtonSubmit.setEnabled(true);
				}else {
					mButtonSubmit.setBackgroundResource(R.drawable.btn_yuyue_none);
					mButtonSubmit.setEnabled(false);
				}
			}
		});
		//跳转到登陆界面
		register_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		//服务网条款
		server_html.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent =new Intent(ACT_Register.this,ACT_Html.class);
				intent.putExtra("htmlName","serviceItem");
				intent.putExtra("htmlUrl",serviceHtml);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub

		mLayoutCity = (RelativeLayout) this.findViewById(R.id.layout_get_city);
		mLayoutSchool = (RelativeLayout) this.findViewById(R.id.layout_get_school);

		mTextVerification = (TextView) this.findViewById(R.id.text_verification);
		mTextViewCity = (TextView) this.findViewById(R.id.city);
		mTextViewSchool = (TextView) this.findViewById(R.id.school);
		mEditTextPhone = (EditText) this.findViewById(R.id.username);
		mEditTextPassword = (EditText) this.findViewById(R.id.password);
		mEditTextVerification = (EditText) this.findViewById(R.id.verification);
		mButtonSubmit = (Button) this.findViewById(R.id.submit);
		mImageShowPassword = (CheckBox) this.findViewById(R.id.checkBox_show_passward);
		register_login = (TextView) findViewById(R.id.register_login);

		server_html = (TextView) findViewById(R.id.register_html);
		view1 =findViewById(R.id.top_menu_view_1);
		view2 =findViewById(R.id.top_menu_view_2);
		if (aVersion){
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);
		}else {
			view2.setVisibility(View.GONE);
			view1.setVisibility(View.GONE);
		}
	}

	Handler mhandler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case SMS_SUCC:
				toast(R.string.verif_code_sms_succ);
				mTextVerification.setBackgroundResource(R.drawable.btn_getcode_none);
				task = new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = SMS_TIME;
						mhandler.sendMessage(message);
					}
				};
				timer.schedule(task, 0, 1000);
				break;
			case SMS_FAIL:
				toast(R.string.verif_code_sms_fail);
				mTextVerification.setText(R.string.get_verification);
				mTextVerification.setEnabled(true);
				mTextVerification.setBackgroundResource(R.drawable.btn_getcode);
				break;
			case SMS_TIME:
				mTextVerification.setText(time-- + "秒");
				if (time == 0) {
					mTextVerification.setText(R.string.get_verification);
					task.cancel();
					mTextVerification.setEnabled(true);
					mTextVerification.setBackgroundResource(R.drawable.btn_getcode);
					time = 60;
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != this.RESULT_OK) {
			return;
		}
		if (requestCode == 1) {
			if (cityId != null && cityId.equals(data.getStringExtra("cityId"))) {
				return;
			}
			String cityName = data.getStringExtra("city");
			cityId = data.getStringExtra("cityId");
			mTextViewCity.setText(cityName);
			mTextViewSchool.setText("");
		} else if (requestCode == 2) {
			String cityName = data.getStringExtra("school");
			schoolId = data.getStringExtra("schoolId");
			Log.d("--", schoolId);
			mTextViewSchool.setText(cityName);
		}
	}

	protected int parseJson(JSONObject result, String url) throws JSONException {
		Gson gson = new Gson();
		Log.d("ACT_Register", result.toString());
		if (url.contains(send_sms_url)) {
			SendSmsResponse sendSmsResponse = gson.fromJson(result.toString(),
					SendSmsResponse.class);
			if (sendSmsResponse.success.equals("true")) {
				rightCode = sendSmsResponse.code;
				String mobile = mEditTextPhone.getText().toString();
				phoneAndCode.put(mobile, rightCode);
				mhandler.sendEmptyMessage(SMS_SUCC);
			} else {
				mhandler.sendEmptyMessage(SMS_FAIL);
			}
		}
		return 0;
	}

	protected void loadDatas() {
		if (progress!=null){
			progress.dismiss();
		}
		//保存密码
		AppPreference.getInstance().savePassWord(mEditTextPassword.getText().toString());
		toast("注册成功!");
		Intent intent = new Intent(ACT_Register.this,
				ACT_Login.class);
		intent.putExtra("phone", mEditTextPhone
				.getText().toString());
		startActivity(intent);
		finish();
		//showpop(getResources().getString(R.string.dialog_content_register_success));

	}

	@Override
	protected void loadError(JSONObject result) {
		if (progress!=null){
			progress.dismiss();
		}
		mButtonSubmit.setEnabled(true);
		mTextVerification.setEnabled(true);
		try {
			if (result != null && "1".equals(result.getString("errorCode"))) {
				showpop(getResources().getString(R.string.dialog_content_register_no_register));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void onClick_login(View view) {
		Intent intent = new Intent(this, ACT_Login.class);
		startActivity(intent);
		finish();
	}

	private void sendSMS(String mobile) {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String str2 = df.format(new Date());
		String s = str2.substring(0, 11) + "telStr" + mEditTextPhone.getText().toString();
		String sk2 = getMd5(s);

		StringBuffer stringBuffer = new StringBuffer(send_sms_url);
		stringBuffer.append("telStr=" + mobile);
		stringBuffer.append("&token=" + sk2);
		sendGetRequest(stringBuffer.toString());
	}

	private String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
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
	private void showpop(String str) {
		View view = LayoutInflater.from(ACT_Register.this).inflate(R.layout.pop_style_2, null);
		TextView title = (TextView) view.findViewById(R.id.pop_title);
		TextView content = (TextView) view.findViewById(R.id.pop_content);
		title.setText("提示");
		content.setText("\u3000"+str);
		Button btn_ok = (Button) view.findViewById(R.id.pop_btn_confrim);
		Button btn_cancle = (Button) view.findViewById(R.id.pop_btn_cancle);
		btn_cancle.setText("取消");
		btn_ok.setText("确定");
		final PopupWindow popupWindow = new PopupWindow(view, getWidth(),
				ViewGroup.LayoutParams.WRAP_CONTENT);
		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(cd);
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);
		//注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		//点击外部消失
		//  popupWindow.setOutsideTouchable(true);
		//设置可以点击
		popupWindow.setFocusable(true);
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 软键盘不会挡着popupwindow
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		//popupWindow.showAsDropDown(mSubmit);
		btn_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				//参数：1，自己的EditText。2，时间。
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				popupWindow.dismiss();
				//
				Intent intent = new Intent(ACT_Register.this,
						ACT_Login.class);
				intent.putExtra("phone", mEditTextPhone
						.getText().toString());
				startActivity(intent);
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
