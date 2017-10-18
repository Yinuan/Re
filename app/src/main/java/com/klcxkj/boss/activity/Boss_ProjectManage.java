package com.klcxkj.boss.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.klcxkj.boss.adpater.LvProjectManageApater;
import com.klcxkj.rs.R;
import com.klcxkj.rs.activity.ACT_Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Boss_ProjectManage extends ACT_Network {

    private ListView mListView;
    private LvProjectManageApater projectManageApater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss__project_manage);
        loadData();
        initview();
        bindview();

    }


    private void initview() {
        showMenu("项目管理");
        mListView = (ListView) findViewById(R.id.weihu_project_manage_listview);
        projectManageApater =new LvProjectManageApater(this);
        List<String> data =new ArrayList<>();
        data.add("重庆大学");
        data.add("重庆大学");
        data.add("重庆大学");
        data.add("重庆大学");
        data.add("重庆大学");
        projectManageApater.setList(data);
        mListView.setAdapter(projectManageApater);
    }
    private void bindview() {

    }

    private void loadData() {
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
