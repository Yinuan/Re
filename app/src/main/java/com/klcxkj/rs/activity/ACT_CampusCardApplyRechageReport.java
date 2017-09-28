package com.klcxkj.rs.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.bo.CardInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:充值成功
 */
public class ACT_CampusCardApplyRechageReport extends ACT_Network{
	private TextView mTextDone;
	private TextView mText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_campus_card_rechage_report);
		showMenu("充值结果");

		mText = (TextView)this.findViewById(R.id.textView1);

		CardInfo mCardInfo = AppPreference.getInstance().getCardInfo();
		String sAgeFormat;
		if (TextUtils.isEmpty(getIntent().getStringExtra("money"))) {
			sAgeFormat = getResources().getString(R.string.campus_card_rechage_success, mCardInfo.getPrefillMoney()+"");
		} else {
			if (getIntent().getStringExtra("rechageType").equals("cardMonney")) {
				sAgeFormat = getResources().getString(R.string.campus_card_rechage_success2, getIntent().getStringExtra("money"));
			} else {
				sAgeFormat = getResources().getString(R.string.campus_card_rechage_success, getIntent().getStringExtra("money"));
			}
			    
		}
		
		mText.setText(sAgeFormat);
	}

	@Override
	protected int parseJson(JSONObject result, String url) throws JSONException {
		return 0;
	}

	@Override
	protected void loadDatas() {

	}

	@Override
	protected void loadError(JSONObject result) {

	}
}
