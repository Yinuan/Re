package com.klcxkj.rs.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.CityAdapter;
import com.klcxkj.rs.bo.CityInfoBo;
import com.klcxkj.rs.bo.CityListResponse;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.view.SideBar2;
import com.klcxkj.rs.view.SideBar2.OnTouchingLetterChangedListener;
import com.klcxkj.rs.widget.ClearEditText;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:城市选择列表
 */
public class ACT_CityList extends ACT_Network {
	private String url = RSApplication.BASE_URL+"tCity/getCityInfo";
//	private CityListResponse mCitys;
	protected List<CityInfoBo> mCitys;
	protected CityAdapter mAdapter;
	protected ListView mListView;
	private SideBar2 sideBar2;
	protected RelativeLayout layoutSearch;
	private EditText mEditText; 
	private TextView mTextTitle;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	private ClearEditText clearEditText;
	private LoadingDialogProgress progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_city);
		initView();
		bindEvent();
		progress = GlobalTools.getInstance().showDailog(this,"加载");
		initDatas(url);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void bindEvent() {

		sideBar2.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {  
		              
	            public void onTouchingLetterChanged(String s) {  
	                int position = mAdapter.getPositionForSection(s.charAt(0));  
	                if(position != -1){  
	                	mListView.setSelection(position);  
	                }  
	                  
	            }  
        });

		clearEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				if ("".equals(editable.toString())) {
					mAdapter.setContacts(mCitys);
					mAdapter.notifyDataSetChanged();
					return;
				}
				ArrayList<CityInfoBo> daySalesDatasSearch = new ArrayList<CityInfoBo>();
				for (CityInfoBo cityInfoBo : mCitys) {
					if (cityInfoBo.getCityName().contains(editable.toString()) || cityInfoBo.getCityPy().contains(editable.toString().toUpperCase())) {
						daySalesDatasSearch.add(cityInfoBo);
					}
				}
				mAdapter.setContacts(daySalesDatasSearch);
				mAdapter.notifyDataSetChanged();
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

				
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

				
			}  
		              
	      });  
	    
	    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				CityInfoBo city= mAdapter.getContacts().get(position);
				Intent intent =  new Intent();
				intent.putExtra("city", city.getCityName());
				intent.putExtra("cityId", city.getCityId());
				setResult(RESULT_OK, intent);
				ACT_CityList.this.finish();
			}
		});
	}

	private void initView(){
		//自定义的搜素框
		clearEditText = (ClearEditText) findViewById(R.id.search);

		mListView = (ListView)this.findViewById(R.id.list_city);
		sideBar2 = (SideBar2) this.findViewById(R.id.sideBar);
		mAdapter = new CityAdapter(this);
		mListView.setAdapter(mAdapter);

		mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		sideBar2.setTextView(mDialogText);
		//标题栏
		showMenu("城市选择");
	}

	/**
	 *  解析数据Gson
	 * @param result
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Gson gson = new Gson();
		CityListResponse cityListResponse = gson.fromJson(result.toString(), CityListResponse.class);
		mCitys = cityListResponse.getObj();
		return 0;
	}

	/**
	 * 解析之后获取到的数据
	 */
	@Override
	protected void loadDatas() {
//		Toast.makeText(this, "count:"+mCitys.size(), Toast.LENGTH_LONG).show();
		progress.dismiss();
		mAdapter.setContacts(mCitys);
		mAdapter.notifyDataSetChanged();
		HashMap<String, String> map = new HashMap<String, String>();
		for (CityInfoBo cityInfoBo : mCitys) {
			try {
				String letter = cityInfoBo.getCityPy().toUpperCase()
						.substring(0, 1);
				map.put(letter, letter);
			} catch (Exception e) {
				toast("py---" + cityInfoBo.getCityPy());
			}
		}
		String[] b = new String[map.size() + 1];
		b[0] = "!";
		int count = 1;
		for(Map.Entry<String, String> entry : map.entrySet()){ 
	          b[count] = entry.getValue(); 
	          count++;
		} 
		Arrays.sort(b); 
		sideBar2.setB(b);
		sideBar2.invalidate();
	}

	@Override
	protected void loadError(JSONObject result) {
		progress.dismiss();

	}

}
