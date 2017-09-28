package com.klcxkj.rs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.klcxkj.rs.R;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:设置-最新资讯-详情
 */
public class ACT_MeMessageDetail  extends ACT_Network{

	private String url ;
	private TextView messageDetail,messageTilte;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_me_message_detail);
		initView();


	}

	private void initView() {
		showMenu("消息详情");
		messageDetail = (TextView) findViewById(R.id.message_detail);
		messageTilte = (TextView) findViewById(R.id.message_title);
		Intent intent =getIntent();
		String title =intent.getStringExtra("message_title");
		String msg =intent.getStringExtra("message_content");
		messageTilte.setText(title);
		messageDetail.setText(msg);
	}


	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void loadDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadError(JSONObject result) {
		// TODO Auto-generated method stub
		
	}

}
