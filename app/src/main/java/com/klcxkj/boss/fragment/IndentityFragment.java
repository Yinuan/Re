package com.klcxkj.boss.fragment;

import com.klcxkj.rs.R;
import com.klcxkj.rs.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * autor:OFFICE-ADMIN
 * time:2017/10/9
 * email:yinjuan@klcxkj.com
 * description:个人信息
 */

public class IndentityFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.boss_fg_indentity;
    }

    @Override
    protected void initLayout() {

    }

    @Override
    public void loadLayout() {

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
