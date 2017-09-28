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
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.SchoolAdapter;
import com.klcxkj.rs.bo.SchoolInfoBo;
import com.klcxkj.rs.bo.SchoolListResponse;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.view.SideBar2;
import com.klcxkj.rs.view.SideBar2.OnTouchingLetterChangedListener;
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
 * Description:学校列表
 */
public class ACT_SchoolList extends ACT_Network {
	private String url = RSApplication.BASE_URL+"tPrjInfo/getPrjInfoByCityId?cityId=";
//	private CityListResponse mCitys;
	protected List<SchoolInfoBo> mSchools;
	protected SchoolAdapter mAdapter;
	protected ListView mListView;
	private ImageView mImageBack;
	private SideBar2 sideBar2;
	protected RelativeLayout layoutSearch;
	private EditText mEditText; 
	private TextView mTextTitle;
	private String cityId;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	private LoadingDialogProgress progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_city);
		initView();
		bindEvent();
		initDataFromIntent();
		progress = GlobalTools.getInstance().showDailog(this,"加载..");
		initDatas(url);
		mEditText.setHint(R.string.hint_school_name);
	}
	
	private void initDataFromIntent() {
		cityId = getIntent().getStringExtra("cityId");
		url = url + cityId;
	}

	private void bindEvent() {
		// TODO Auto-generated method stub
		mImageBack.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				ACT_SchoolList.this.finish();
			}
		});
		sideBar2.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {  
		              
	            public void onTouchingLetterChanged(String s) {  
	                int position = mAdapter.getPositionForSection(s.charAt(0));  
	                if(position != -1){  
	                	mListView.setSelection(position);  
	                }  
	                  
	            }  
        }); 
		
	    mEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				// TODO Auto-generated method stub
				if ("".equals(editable.toString())) {
					mAdapter.setContacts(mSchools);
					mAdapter.notifyDataSetChanged();
					return;
				}
				ArrayList<SchoolInfoBo> daySalesDatasSearch = new ArrayList<SchoolInfoBo>();
				for (SchoolInfoBo schoolInfoBo : mSchools) {
					if (schoolInfoBo.getPrjName().contains(editable.toString()) || schoolInfoBo.getPriPy().contains(editable.toString().toUpperCase())) {
						daySalesDatasSearch.add(schoolInfoBo);
					}
				}
				mAdapter.setContacts(daySalesDatasSearch);
				mAdapter.notifyDataSetChanged();
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}  
		              
	      });  
	    
	    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SchoolInfoBo school= mAdapter.getContacts().get(position);
				Intent intent =  new Intent();
				intent.putExtra("school", school.getPrjName());
				intent.putExtra("schoolId", school.getPrjRecId());
				setResult(RESULT_OK, intent);
				ACT_SchoolList.this.finish();
			}
		});
	}

	private void initView(){
		mImageBack =(ImageView) this.findViewById(R.id.image_back);
		mListView = (ListView)this.findViewById(R.id.list_city);
		sideBar2 = (SideBar2) this.findViewById(R.id.sideBar);
		layoutSearch = (RelativeLayout)this.findViewById(R.id.layout_search);
		mAdapter = new SchoolAdapter(this);
		mListView.setAdapter(mAdapter);
		mEditText = (EditText)this.findViewById(R.id.edit_text_city);
		mTextTitle = (TextView)this.findViewById(R.id.text_title);
		mTextTitle.setText(R.string.title_school);
//	    indexBar.setListView(mListView);	
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
		showMenu("学校选择");
	}
	
	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		Gson gson = new Gson();
		SchoolListResponse schoolListResponse = gson.fromJson(result.toString(), SchoolListResponse.class);
		mSchools = schoolListResponse.getObj();
		mAdapter.setContacts(mSchools);
		mAdapter.notifyDataSetChanged();
		HashMap<String, String> map = new HashMap<String, String>();
		for (SchoolInfoBo schoolInfoBo : mSchools) {
			try {
				String letter = schoolInfoBo.getPriPy().toUpperCase()
						.substring(0, 1);
				map.put(letter, letter);
			} catch (Exception e) {
				toast("py---" + schoolInfoBo.getPriPy());
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
		return 0;
	}

	@Override
	protected void loadDatas() {
		progress.dismiss();
	}

	@Override
	protected void loadError(JSONObject result) {
		progress.dismiss();

	}

}
