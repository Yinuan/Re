package com.klcxkj.rs.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bean.ApplyCard;
import com.klcxkj.rs.bean.BuildingAndRoomName;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.bo.Ban;
import com.klcxkj.rs.bo.BaseBo;
import com.klcxkj.rs.bo.Room;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:自助申卡
 */
public class ACT_CampusCardApply extends ACT_Network implements View.OnClickListener {
	private String url = RSApplication.BASE_URL + "tStudent/studentAutoMakeCard?";
	private LinearLayout layout_all;//布局parent
	private EditText mEditName;//name
	private RadioGroup sex;
	private EditText mEditStudentNumber; //身份证
	private RelativeLayout building_layout,room_layout;
	private  TextView mBuildingID,mRoomID;//楼栋和房间号
	private Button mButtonBindNext;//提交按钮

	private String sexNumber;//性别
	private String buildingId; //楼栋号
	private String buildingName;//楼栋名字
	private LoadingDialogProgress progress;
	private Room aRoom;
	private Ban aBuild;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_campus_card_apply);
		initView();
		bindEvent();
		EventBus.getDefault().register(this);
		showPop();

	}




	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(BuildingAndRoomName brName){
		 aRoom =brName.getRoom();
		 aBuild =brName.getBuild();
		buildingName =aBuild.getBuildingName();
		buildingId =aBuild.getBuildingID();
		mBuildingID.setText(aBuild.getBuildingName());
		mRoomID.setText(aRoom.getRoomName());

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void initView() {
		showMenu("自助办卡");
		mEditName = (EditText) findViewById(R.id.apply_name);
		sex = (RadioGroup) findViewById(R.id.apply_sex);
		mEditStudentNumber = (EditText) findViewById(R.id.apply_IDCard);
		building_layout = (RelativeLayout) findViewById(R.id.apply_building_layout);
		room_layout = (RelativeLayout) findViewById(R.id.apply_room_layout);
		mBuildingID = (TextView) findViewById(R.id.apply_building);
		mRoomID = (TextView) findViewById(R.id.apply_room);
		mButtonBindNext = (Button) findViewById(R.id.apply_btn);
		layout_all = (LinearLayout) findViewById(R.id.apply_layout);

	}


	private void bindEvent() {
		//姓名输入
		mEditName.setFilters(new InputFilter[] { new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {

				try {
					int len = 0;
					boolean more = false;
					do {
						SpannableStringBuilder builder = new SpannableStringBuilder(
								dest).replace(dstart, dend,
								source.subSequence(start, end));
						len = builder.toString().getBytes("UTF-8").length;
						more = len > 30;
						if (more) {
							end--;
							source = source.subSequence(start, end);
						}
					} while (more);
					return source;
				} catch (UnsupportedEncodingException e) {
					return "Exception";
				}
			}
		} });

		//性别选择
		sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
				RadioButton radioButton = (RadioButton) findViewById(checkId);
				String str =radioButton.getText().toString();
				if (str.equals("男")){
					sexNumber ="1";
				}else {
					sexNumber ="0";
				}
				Log.d("ACT_CampusCardApply", "raSex:"+sexNumber);
			}
		});
		mButtonBindNext.setOnClickListener(this);
		building_layout.setOnClickListener(this);
		room_layout.setOnClickListener(this);

	}

private void submitDataToserver(){
	progress = GlobalTools.getInstance().showDailog(ACT_CampusCardApply.this,"提交..");
	String pass = AppPreference.getInstance().getPassWord();
	Log.d("ACT_CampusCardApply", "passssss:"+pass);
	Log.d("ACT_CampusCardApply", "sexxxxx:"+sexNumber);
	HashMap<String, String> params = new HashMap<String, String>();
	params.put("telPhone", getNewUser().getTelPhone());
	params.put("PrjID", getNewUser().getPrjID()+"");
	params.put("EmployeeName", mEditName.getText().toString());
	params.put("SexID", sexNumber);
	params.put("RoomID", aRoom.getRoomID());
	params.put("identifier", mEditStudentNumber.getText()
			.toString());
	params.put("UserPwd", pass);
	params.put("ServerIP", getNewUser().getServerIP());
	params.put("ServerPort", getNewUser().getServerPort()+"");
	Log.d("ACT_CampusCardApply", "params:" + params);
	sendPostRequest(url, params);
}

	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		progress.dismiss();

		return 0;
	}

	@Override
	protected void loadDatas() {

	}

	@Override
	protected void loadError(JSONObject result) {
		progress.dismiss();
	}

	@Override
	protected void handleErrorResponse(String url, VolleyError error) {
		super.handleErrorResponse(url, error);
		if (error instanceof TimeoutError) {
			toast(R.string.timeout_error);
		} else {
			toast(R.string.operate_error);
		}
		progress.dismiss();
	}

	@Override
	protected void handleResponse(String url, JSONObject json) {
		super.handleResponse(url, json);
		progress.dismiss();
		Log.d("ACT_CampusCardApply", "json:" + json.toString());
		//将卡片信息保存
		Gson gson =new Gson();
		BaseBo baseBo =gson.fromJson(json.toString(),BaseBo.class);
		if (baseBo.isSuccess()){
			ApplyCard card =gson.fromJson(json.toString(),ApplyCard.class);
			toast(card.getMsg());
			//更新用户资料
			UserInfo userInfo =AppPreference.getInstance().getUserInfo();
			userInfo.setEmployeeID(card.getEmployeeID());
			AppPreference.getInstance().saveLoginUser(userInfo);
			Log.d("ACT_CampusCardApply", "applyCardIsSucess");
			EventBus.getDefault().postSticky("applyCardIsSucess");
			showPop2();

		}else {
			toast(baseBo.getMsg());
		}


	}




	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.apply_building_layout://楼栋
				Intent intent1 =new Intent(ACT_CampusCardApply.this,ACT_BuildingChose.class);
				intent1.putExtra("type","ACT_CampusCardApply");
				intent1.putExtra("buildingName",buildingName);
				startActivity(intent1);
				break;
			case R.id.apply_room_layout://房间号
				Intent intent =new Intent(ACT_CampusCardApply.this,ACT_RoomChose.class);
				if (buildingId !=null){
					intent.putExtra("buildingID",buildingId);
					intent.putExtra("buildingName",mBuildingID.getText().toString());
					intent.putExtra("type","ACT_CampusCardApply");
				}else {
					toast("请先选择楼栋");
					return;
				}
				startActivity(intent);
				break;
			case R.id.apply_btn: //提交按钮
				if (TextUtils.isEmpty(mEditName.getText())) {
					toast("请输入姓名");
					return;
				}
				if (TextUtils.isEmpty(sexNumber)) {
					toast("请选择性别");
					return;
				}
				if (TextUtils.isEmpty(mEditStudentNumber.getText())) {
					toast("请输入身份证号码");
					return;
				}
				if (TextUtils.isEmpty(mBuildingID.getText().toString())) {
					toast("请选择楼栋");
					return;
				}
				if (TextUtils.isEmpty(mRoomID.getText().toString())) {
					toast("请选择房间");
					return;
				}
				if (mEditStudentNumber.getText().toString().length()<15){
					toast("身份证号码不少于15位");
					return;
				}
				submitDataToserver();
				break;


		}
	}


	private void showPop() {
		View view = LayoutInflater.from(ACT_CampusCardApply.this).inflate(R.layout.pop_style_2b, null);
		TextView title = (TextView) view.findViewById(R.id.pop_title);
		TextView content = (TextView) view.findViewById(R.id.pop_content);
		Button btn_ok = (Button) view.findViewById(R.id.pop_btn_confrim);
		Button btn_cacle = (Button) view.findViewById(R.id.pop_btn_cancle);
		title.setText("提示");
		content.setText("如已办卡，请直接绑卡");
		btn_ok.setText("去绑卡");
		btn_cacle.setText("立即办卡");

		final PopupWindow popupWindow = new PopupWindow(view, getWidth(),
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
		getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				popupWindow.showAtLocation(layout_all, Gravity.CENTER, 0, 0);
			}
		});

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
				//
				startActivity(new Intent(ACT_CampusCardApply.this,
						ACT_CampusCardBind.class));
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
	private void showPop2() {
		View view = LayoutInflater.from(ACT_CampusCardApply.this).inflate(R.layout.pop_style_3, null);
		TextView title = (TextView) view.findViewById(R.id.pop_3_content);
		Button btn = (Button) view.findViewById(R.id.pop_3_btn);
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setText("\u3000"+"请前往自助领卡机领卡");
		final PopupWindow popupWindow = new PopupWindow(view, getWidth(),
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
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

		//popupWindow.showAsDropDown(mSubmit);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				//

				finish();
			}
		});
		// 监听菜单的关闭事件
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				Log.d("ACT_CampusCardApply", "走没走");
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
}
