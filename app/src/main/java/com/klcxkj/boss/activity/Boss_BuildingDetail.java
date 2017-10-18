package com.klcxkj.boss.activity;

import android.os.Bundle;

import com.klcxkj.rs.R;
import com.klcxkj.rs.activity.ACT_Network;

import org.json.JSONException;
import org.json.JSONObject;

public class Boss_BuildingDetail extends ACT_Network {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss__building_detail);
        showMenu("楼栋详情");
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
