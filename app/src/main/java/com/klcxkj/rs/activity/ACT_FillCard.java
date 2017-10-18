package com.klcxkj.rs.activity;

import android.content.Context;
import android.content.Intent;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.bo.AddCardResult;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:自助补卡
 */
public class ACT_FillCard extends ACT_Network {
	private String fill_url = RSApplication.BASE_URL +"tStudent/selfHelpAutoReMakeCard?";
	private TextView cardId;
	private TextView cardName;
	private TextView cardSex;
	private TextView cardIDCard;
	private TextView cardBuilding;
	private TextView cardRoom;
	private  Button mButtonBindNext;
	private CardInfo cardInfo;
	private UserInfo userInfo;
	private LoadingDialogProgress progress;

	private String queryCardInfo = RSApplication.BASE_URL + "tStudent/studentGetCardInfo?";  //查询卡片信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_campus_card_fill);

		initView();
		initdata();
		bindEvent();



	}
	/**
	 * 查询卡片信息
	 */
	private void loadCardDatasforServer() {
		StringBuffer sb = new StringBuffer(queryCardInfo);
		sb.append("PrjID=" + userInfo.getPrjID());
		sb.append("&EmployeeID=" + userInfo.getEmployeeID());
		sb.append("&ServerIP=" + userInfo.getServerIP());
		sb.append("&ServerPort=" + userInfo.getServerPort());
		Log.d("HomeFragment", "sb:" + sb);
		sendGetRequest(sb.toString());
	}
	private void initdata() {
		cardInfo = AppPreference.getInstance().getCardInfo();
		userInfo =AppPreference.getInstance().getUserInfo();
		if (cardInfo !=null){
			cardId.setText(cardInfo.getCardID()+"");
			cardName.setText(cardInfo.getEmployeeName());
			int sexI =Integer.valueOf(cardInfo.getSexID());
			String sexNm="";
			switch (sexI){
				case 0:
					sexNm ="女";
					break;
				case 1:
					sexNm ="男";
					break;
			}
			cardSex.setText(sexNm);
			cardIDCard.setText(cardInfo.getIdentifier());
			cardBuilding.setText(cardInfo.getBuildingName());
			cardRoom.setText(cardInfo.getRoomName());
		}
	}

	private void initView() {
		cardId = (TextView) findViewById(R.id.fill_cardId);
		cardName = (TextView) findViewById(R.id.fill_cardName);
		cardSex = (TextView) findViewById(R.id.fill_cardSex);
		cardIDCard = (TextView) findViewById(R.id.fill_cardIDCard);
		cardBuilding = (TextView) findViewById(R.id.fill_cardBuilding);
		cardRoom = (TextView) findViewById(R.id.fill_cardRoom);
		mButtonBindNext = (Button) findViewById(R.id.button_submit);
	}

	private void bindEvent() {
			showMenu("自助补卡");


		mButtonBindNext.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showPopOut();

			}
		});

	}

	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Log.d("ACT_FillCard", "result:" + result);
		Gson gson =new Gson();
		if (url.contains(fill_url)){

			AddCardResult result1 =gson.fromJson(result.toString(),AddCardResult.class);
			if (result1.getSuccess().equals("true")){
				if (result1.getObj()==0){
					toast("补卡成功，请及时领取新卡");
					//查询卡片信息
					loadCardDatasforServer();
				}else {
					toast(result1.getMsg());
				}

			}else {
				if (result1.getObj() ==-1){
					toast("补卡失败，未找到人员信息");
				}else if (result1.getObj()==-2){
					toast("补卡失败，卡状态不正确");
				}else if (result1.getObj() ==-3){
					toast("补卡异常");
				}
			}
		}else if (url.contains(queryCardInfo)){
			cardInfo =gson.fromJson(result.toString(),CardInfo.class);
			//保存卡片信息
			if (cardInfo.getCardID() !=0){
				AppPreference.getInstance().saveCardInfo(result.toString());
				double perMonny =cardInfo.getPrefillMoney();
				if (perMonny>0){
					Intent intent2 =new Intent(ACT_FillCard.this,ACT_ApplyCard_Rechge.class);
					intent2.putExtra("perMonny",perMonny+"");
					startActivity(intent2);

				}
				finish();
			}

		}
		return 0;
	}

	@Override
	protected void loadDatas() {
			if (progress !=null){
				progress.dismiss();
			}
	}

	@Override
	protected void loadError(JSONObject result) {
		if (progress !=null){
			progress.dismiss();
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

	private void showPopOut() {
		View view1 = LayoutInflater.from(ACT_FillCard.this).inflate(R.layout.pop_style_2, null);
		TextView title = (TextView) view1.findViewById(R.id.pop_title);
		TextView content = (TextView) view1.findViewById(R.id.pop_content);
		Button btn_ok = (Button) view1.findViewById(R.id.pop_btn_confrim);
		Button btn_cacle = (Button) view1.findViewById(R.id.pop_btn_cancle);
		title.setText("提示");
		content.setText(getResources().getString(R.string.dialog_content_fill_card));
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
				showpop2();

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

	/**
	 * 选择其他金额
	 */
	private void showpop2() {
		View view = LayoutInflater.from(ACT_FillCard.this).inflate(R.layout.pop_style_4, null);
		final AutoCompleteTextView value = (AutoCompleteTextView) view.findViewById(R.id.pop_4_value);
		value.setHint("请输入登录密码");
		Button btn_ok = (Button) view.findViewById(R.id.pop_4_confrim);
		Button btn_cancle = (Button) view.findViewById(R.id.pop_4_cancle);
		TextView title = (TextView) view.findViewById(R.id.pop_4_title);
		title.setText("提示: 请输入登录密码");
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

					popupWindow.dismiss();
					progress = GlobalTools.getInstance().showDailog(ACT_FillCard.this,"提交..");

					StringBuffer sb =new StringBuffer(fill_url);
					sb.append("PrjID="+getNewUser().getPrjID());
					sb.append("&EmployeeID="+getNewUser().getEmployeeID());
					sb.append("&ServerIP="+getNewUser().getServerIP());
					sb.append("&ServerPort="+getNewUser().getServerPort());
					Log.d("ACT_FillCard", "sb:" + sb);
					sendGetRequest(sb.toString());
					mButtonBindNext.setEnabled(false);
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
