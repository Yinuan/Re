package com.klcxkj.rs.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.bean.UserInfo;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:引导页
 */
public class ACT_SplashScreen extends ACT_Base {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_splashscreen);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		new Handler().postDelayed(new Runnable(){
			public void run(){
				if(AppPreference.getInstance().isFirstTimeOpen()){//第一次打开
					Intent intent = new Intent(getApplicationContext(), ACT_GuidePage.class);
					startActivity(intent);
				}else{
					UserInfo loginUser = AppPreference.getInstance().getUserInfo();
					if(loginUser != null){
						goMainPage();
					}else{
						Intent intent = new Intent(getApplicationContext(), ACT_Login.class);
						startActivity(intent);
					}
				}
				finish();
			}
		}, 1000);
	}

	private void goMainPage() {
		Intent intent = new Intent(this, ACT_MainUI.class);
		startActivity(intent);
		finish();
	}
	
	
}
