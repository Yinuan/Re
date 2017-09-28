package com.klcxkj.rs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.adapter.LMessageListAdapter;
import com.klcxkj.rs.bo.MessageBean;
import com.klcxkj.rs.bo.MessageListResult;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:设置-最新资讯
 */
public class ACT_MeMessageCenter extends ACT_Network{
	private String MESSAGE_CENTER = RSApplication.BASE_URL+"tStudent/getPublishList?";
	private ListView mListView;
	private LMessageListAdapter mAdapter;
	private List<MessageBean> mDatas;
	private ImageView data_null;
	private MessageListResult messageListResult;
	private LoadingDialogProgress progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_me_message_center);
		initView();
		bindEvent();
		initData();

	}

	private void initData() {
		progress = GlobalTools.getInstance().showDailog(this,"加载..");
		StringBuffer sb1 = new StringBuffer(MESSAGE_CENTER);
		sb1.append("PrjID="+getNewUser().getPrjID());
		sb1.append("&TelPhone="+getNewUser().getTelPhone());
		sb1.append("&ServerIP="+getNewUser().getServerIP());
		sb1.append("&ServerPort="+ getNewUser().getServerPort());
		Log.d("ACT_MeMessageCenter","sb1:"+ sb1.toString());
		sendGetRequest(sb1.toString());
	}

	private void bindEvent() {

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {

				if (messageListResult.getObj().get(position).getMsgType() ==1){
					Intent intent =new Intent(ACT_MeMessageCenter.this,ACT_Html.class);
					intent.putExtra("htmlName","messageDetail");
					intent.putExtra("htmlUrl",messageListResult.getObj().get(position).getHtmlPath());
					startActivity(intent);
				}else {
					Intent intent =new Intent(ACT_MeMessageCenter.this,ACT_MeMessageDetail.class);
					intent.putExtra("message_title",messageListResult.getObj().get(position).getRepTitle());
					intent.putExtra("message_content",messageListResult.getObj().get(position).getRepContent());
					startActivity(intent);
				}


			}
		});
	}
	private void initView() {
		showMenu("消息中心");
		mListView =  (ListView) this.findViewById(R.id.listView1);
		data_null = (ImageView) findViewById(R.id.message_data_null);
		mAdapter = new LMessageListAdapter(this);
		mDatas =new ArrayList<>();
		mAdapter.setList(mDatas);
		mListView.setAdapter(mAdapter);

	}
	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Log.d("ACT_MeMessageCenter", "result:" + result);
		Gson gson =new Gson();
		 messageListResult =gson.fromJson(result.toString(),MessageListResult.class);
		return 0;
	}

	@Override
	protected void loadDatas() {
		progress.dismiss();
		if (messageListResult.getObj() !=null){
			mDatas.addAll(messageListResult.getObj());
			data_null.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		}else {
			data_null.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void loadError(JSONObject result) {
		progress.dismiss();


	}
	



	
	public Long getDate(String dateString) {
		Date curDate = null;
		try {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		curDate = formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (long) 0;
		}
		return curDate.getTime();
	}
	
	public String formatDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = formatter.format(getDate(dateString));
		return date;
	}
	
}
