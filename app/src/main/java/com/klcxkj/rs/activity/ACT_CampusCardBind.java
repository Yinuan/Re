package com.klcxkj.rs.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:绑定卡片
 */
public class ACT_CampusCardBind extends ACT_Network {
	private String url = RSApplication.BASE_URL+"tStudent/sTudentBinddingCard?";
	private ImageView mImageBack;
	private Button mButtonBindNext;
	private EditText mEditCardID, mEditPassword;
	private LoadingDialogProgress progress;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_campus_card_bind);
		initView();
		bindEvent();
		
	}
	
	private void initView() {
		mImageBack = (ImageView)this.findViewById(R.id.image_back);
		mButtonBindNext = (Button)this.findViewById(R.id.button_bind_next);
		mEditCardID = (EditText)this.findViewById(R.id.cardID);
		mEditPassword = (EditText)this.findViewById(R.id.password);
		showMenu("绑定校园卡");
	}

	private void bindEvent() {

		mButtonBindNext.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(mEditCardID.getText())) {
					toast("请输入卡号");
					return;
				}
				if (TextUtils.isEmpty(mEditPassword.getText())) {
					toast("请输入PIN码");
					return;
				}
				// TODO Auto-generated method stub
				StringBuffer sb = new StringBuffer(url);
				sb.append("PrjID="+getNewUser().getPrjID());
				sb.append("&CardID=" +  mEditCardID.getText());
				sb.append("&CardPwd="+ mEditPassword.getText());
				sb.append("&telPhone="+getNewUser().getTelPhone());
				sb.append("&ServerIP="+getNewUser().getServerIP());
				sb.append("&ServerPort="+ getNewUser().getServerPort());
				Log.d("---", "sb:" + sb);
				sendGetRequest(sb.toString());
				mButtonBindNext.setEnabled(false);
				progress = GlobalTools.getInstance().showDailog(ACT_CampusCardBind.this,"绑定..");
			}
		});
	}

	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Log.d("ACT_CampusCardBind", "result:" + result);
		toast("绑定成功");
		//将卡片信息中的绑卡ID保存起来
		Gson gson =new Gson();
		CardInfo cardInfoNew =gson.fromJson(result.toString(),CardInfo.class);
		//更新用户信息
		UserInfo userInfo =AppPreference.getInstance().getUserInfo();
		userInfo.setEmployeeID(cardInfoNew.getEmployeeID());
		AppPreference.getInstance().saveLoginUser(userInfo);
		//发送信息给homeFragment，告诉他我绑定成功了
		EventBus.getDefault().postSticky("cardIsBinded");
		return 0;
	}

	@Override
	protected void loadDatas() {
		// TODO Auto-generated method stub
		progress.dismiss();
		finish();
	}

	@Override
	protected void loadError(JSONObject result) {
		// TODO Auto-generated method stub
		progress.dismiss();
		mButtonBindNext.setEnabled(true);

	}

}
