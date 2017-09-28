package com.klcxkj.rs.activity;

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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.SendSmsResponse;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.util.StringUtils;
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
 * Description:忘记密码
 */
public class ACT_ForgetPassword extends ACT_Network{
	public static final int SMS_SUCC = 1;
	public static final int SMS_FAIL = 2;
	public static final int SMS_TIME = 3;
	private String AlterPass = RSApplication.BASE_URL+"tStudent/forgetLogInPwd?";
	private ImageView mImageBack;
	private Button mSubmit;
	private TextView mTextVerification;
	private EditText mEditTextUserPhone;
	private EditText mEditTextUserPassword;
	private EditText mEditTextVerification;
	private CheckBox mImageShowPassword;
	private TextView codeHint;
	private View progressbar;
	String rightCode;
	private String mPassword;
	private HashMap<String,String> phoneAndCode = new HashMap<String, String>();
	
	private final Timer timer = new Timer();
	private TimerTask task;
	private int time = 60;
	private LoadingDialogProgress progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_reset_password);
		initView();
		bindEvent();


		
	}
	private void bindEvent() {

		mSubmit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(mEditTextUserPhone.getText())) {
					toast("请输入手机号码");
					return;
				}
				if (mEditTextUserPhone.getText().toString().trim().length() != 11) {
					toast("手机号码格式不正确");
					return;
				}
				if (TextUtils.isEmpty(rightCode)) {
					toast("请获取验证码");
					return;
				}
				if (TextUtils.isEmpty(mEditTextVerification.getText().toString())) {
					toast("请输入验证码");
					return;
				}
				if (rightCode != null && !rightCode.equals(mEditTextVerification.getText().toString())) {
					toast("验证码不正确");
					//弹出一个pop
					showPop("短信验证码不正确");
					return;
				}
				if (!phoneAndCode.containsKey(mEditTextUserPhone.getText().toString())) {
					toast("验证码与手机不匹配，请重新获取");
					return;
				}
				if (mEditTextUserPassword.length() < 6) {
					toast("密码不能少于6位");
					return;
				}
				mSubmit.setClickable(false);
				progress = GlobalTools.getInstance().showDailog(ACT_ForgetPassword.this,"提交..");
//				progressbar.setVisibility(View.VISIBLE);
				StringBuffer sb = new StringBuffer(AlterPass);
				sb.append("telPhone="+mEditTextUserPhone.getText());
				sb.append("&LogPwd="+ mEditTextUserPassword.getText());
				mPassword = mEditTextUserPassword.getText().toString();
				sendGetRequest(sb.toString());
			}
		});
		//获取验证码
		mTextVerification.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if (mEditTextUserPhone.getText().toString().trim().length() != 11) {
					toast("手机号码格式不正确");
					return;
				}
				mTextVerification.setEnabled(false);
				final String mobile = mEditTextUserPhone.getText().toString();
				sendSMS(mobile);

				

			}
		});

		//按钮下一步的监听
		mEditTextUserPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (mEditTextUserPassword.getText() !=null &&mEditTextUserPassword.getText().toString().length()>=6){
					mSubmit.setBackgroundResource(R.drawable.btn_yuyue);
					mSubmit.setEnabled(true);
				}else {
					mSubmit.setBackgroundResource(R.drawable.btn_yuyue_none);
					mSubmit.setEnabled(false);
				}
			}
		});
		mImageShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){  
					mEditTextUserPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                }else{  
                	mEditTextUserPassword.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                }  
				mEditTextUserPassword.setSelection(mEditTextUserPassword.getText().length());
			}
		});
	}



	private void initView() {
		// TODO Auto-generated method stub
		mImageBack = (ImageView)this.findViewById(R.id.image_back);
		mSubmit = (Button)this.findViewById(R.id.submit);
		mTextVerification = (TextView) this.findViewById(R.id.text_verification);
		mEditTextUserPhone = (EditText)this.findViewById(R.id.user_phone);
		mEditTextUserPassword = (EditText)this.findViewById(R.id.password);
		mEditTextVerification = (EditText)this.findViewById(R.id.verification);
		mImageShowPassword = (CheckBox) this.findViewById(R.id.checkBox_show_passward);
		codeHint = (TextView) findViewById(R.id.Phonecode_hint);
		showMenu("忘记密码");
	}

	
	Handler mhandler = new Handler(){
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case SMS_SUCC:
				//toast(R.string.verif_code_sms_succ);
				String mobile =mEditTextUserPhone.getText().toString();
				mTextVerification.setBackgroundResource(R.drawable.btn_getcode_none);
				//截取mobile的前三位及后三位
				Log.d("ACT_ForgetPassword", StringUtils.bankPhoneReplaceWithStar(mobile));
				codeHint.setVisibility(View.VISIBLE);
				codeHint.setText("手机验证码已发送至您"+StringUtils.bankPhoneReplaceWithStar(mobile)+"的手机");
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
				mTextVerification.setText(time--+"秒");
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
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Gson gson = new Gson();
		if (url.contains(ACT_Register.send_sms_url)) {
			SendSmsResponse sendSmsResponse = gson.fromJson(result.toString(),
					SendSmsResponse.class);
			if (sendSmsResponse.success.equals("true")) {
				rightCode = sendSmsResponse.code;
				String mobile = mEditTextUserPhone.getText().toString();
				phoneAndCode.put(mobile, rightCode);
				mhandler.sendEmptyMessage(SMS_SUCC);
			}else {
				mhandler.sendEmptyMessage(SMS_FAIL);
			}
		}
		return 0;
	}

	@Override
	protected void loadDatas() {
		if (progress!=null){
			progress.dismiss();
		}
		mSubmit.setClickable(false);
		//修改密码成功
		AppPreference.getInstance().savePassWord(mPassword);
		Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	protected void loadError(JSONObject result) {
		if (progress !=null){
			progress.dismiss();
		}
		mSubmit.setClickable(true);
		mTextVerification.setEnabled(true);
	}

	private void sendSMS(String mobile) {
//		StringBuffer stringBuffer = new StringBuffer(ACT_Register.send_sms_url);
//		stringBuffer.append("tel=" + mobile);
//		sendGetRequest(stringBuffer.toString());
		
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String str2 = df.format(new Date());
		String s = str2.substring(0, 11) + "telStr"
				+ mobile;
		String sk2 = getMd5(s);

		StringBuffer stringBuffer = new StringBuffer(ACT_Register.send_sms_url);
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
	private void showPop(String str) {
		View view1 = LayoutInflater.from(ACT_ForgetPassword.this).inflate(R.layout.pop_style_1, null);
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
