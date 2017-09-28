package com.klcxkj.rs.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:卡片挂失
 */
public class ACT_CampusCardLoss extends ACT_Network {
//	http://211.149.224.58:6002/tStudent/studentLostAndUnLostCard?PrjID=5&EmployeeID=3&intStatus=0
	private String url = RSApplication.BASE_URL+"tStudent/studentLostAndUnLostCard?";
	private EditText mEditCardID, mEditCardName, mEditCardStatus;
	private Button mButtonSubmit;

	private CardInfo mCardInfo;
	private CardInfo cardIn;//用来解析的对象
	private HashMap<String, String> mStatusMap;
	private LoadingDialogProgress progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_campus_card_loss);
		initView();
		bindEvent();
		initViewFromIntent();
	}
	
	private void initView() {

		mEditCardID = (EditText)this.findViewById(R.id.edit_cardID);
		mEditCardName = (EditText)this.findViewById(R.id.edit_user_name);
		mEditCardStatus = (EditText)this.findViewById(R.id.edit_card_status);
		mButtonSubmit = (Button)this.findViewById(R.id.button_bind_next);

	}
	
	private void initViewFromIntent() {
		mCardInfo = AppPreference.getInstance().getCardInfo();
		if (mCardInfo!=null){
			mEditCardID.setText(mCardInfo.getCardID()+"");
			mEditCardName.setText(mCardInfo.getEmployeeName());
			int statusId =mCardInfo.getNCardStatusID();
			String statusName ="";
			switch (statusId){
				case 0://正常
					statusName="正常";
					break;
				case 1://挂失
					statusName="挂失";
					break;
				case 2://退卡
					statusName="退卡";
					break;
				case 3://未领卡
					statusName="未领卡";
					break;
				case 4://销户
					statusName="销户";
					break;

			}
			mEditCardStatus.setText(statusName);
			if (statusName.equals("正常")){
				showMenu("挂失");
			}else {
				showMenu("解挂");
			}

		}

	}

	private void bindEvent() {

		mButtonSubmit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if (mEditCardStatus.getText().toString().equals("正常")|| mEditCardStatus.getText().toString().equals("挂失")){
					showpop();
				}

			}
		});
	}

	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		// TODO Auto-generated method stub

		Log.d("ACT_CampusCardLoss", "result:" + result);
		Gson gson =new Gson();
		 cardIn =gson.fromJson(result.toString(),CardInfo.class);


		return 0;
	}

	@Override
	protected void loadDatas() {
		progress.dismiss();
		mButtonSubmit.setEnabled(true);
		if (cardIn.getNCardStatusID()==1){//挂失状态

			toast("挂失成功");
			mCardInfo.setNCardStatusID(1);
			showMenu("解挂");
			mEditCardStatus.setText("挂失");
		}else {	//正常
			mCardInfo.setNCardStatusID(0);
			mEditCardStatus.setText("正常");
			toast("解挂成功");
			showMenu("挂失");
		}
		AppPreference.getInstance().saveCardInfos(mCardInfo);
		//EventBus.getDefault().postSticky("campusCardLoss");
		finish();
	}

	@Override
	protected void loadError(JSONObject result) {
		progress.dismiss();
		mButtonSubmit.setEnabled(true);

		
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
	/**
	 * 选择其他金额
	 */
	private void showpop() {
		View view = LayoutInflater.from(ACT_CampusCardLoss.this).inflate(R.layout.pop_style_4, null);
		final AutoCompleteTextView value = (AutoCompleteTextView) view.findViewById(R.id.pop_4_value);
		value.setHint("请输入密码");
		Button btn_ok = (Button) view.findViewById(R.id.pop_4_confrim);
		Button btn_cancle = (Button) view.findViewById(R.id.pop_4_cancle);
		TextView title = (TextView) view.findViewById(R.id.pop_4_title);
		title.setText("提示");
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
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				final InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		},50);
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
			//验证密码
				String password = value.getText().toString();
				String pa =AppPreference.getInstance().getPassWord();
				Log.d("ACT_CampusCardLoss","password:"+ pa);
				if (!TextUtils.isEmpty(password) && password.equals(pa)) {
					StringBuffer sb = new StringBuffer(url);
					sb.append("PrjID="+getNewUser().getPrjID());
					sb.append("&EmployeeID=" + getNewUser().getEmployeeID());
					if (mCardInfo.getNCardStatusID()==1) {
						sb.append("&intStatus="+ 0);
					}  else if (mCardInfo.getNCardStatusID()==0) {
						sb.append("&intStatus="+ 1);
					}
					sb.append("&ServerIP="+getNewUser().getServerIP());
					sb.append("&ServerPort="+ getNewUser().getServerPort());
					sendGetRequest(sb.toString());
					progress = GlobalTools.getInstance().showDailog(ACT_CampusCardLoss.this,"提交..");
					mButtonSubmit.setEnabled(false);
					popupWindow.dismiss();
				} else {
//							DialogUtil.dismissAlertDialog();
//							PopupWindowUtil.showPopupWindow(ACT_CampusCardLoss.this, ACT_CampusCardLoss.this.findViewById(R.id.layout_navbar));
					toast("密码错误，请重新输入");
				}
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

		//设置软件盘不挡
		popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

	}


}
