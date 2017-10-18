package com.klcxkj.boss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.klcxkj.rs.R;
import com.klcxkj.rs.activity.ACT_BuildingChose;
import com.klcxkj.rs.activity.ACT_Network;

import org.json.JSONException;
import org.json.JSONObject;

public class Boss_ProjectDetail extends ACT_Network implements View.OnClickListener{

    private RelativeLayout line1,line2,line3,line4,line5,line6,line7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss__project_detail);
        initview();
    }

    private void initview() {
        showMenu("项目详情");
        line1 = (RelativeLayout) findViewById(R.id.boss_project_detail_list_1);


        line1.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.boss_project_detail_list_1:
                Intent intent1 =new Intent(Boss_ProjectDetail.this,ACT_BuildingChose.class);
                intent1.putExtra("type","boss");
                intent1.putExtra("buildingName","noBuildName");
                startActivity(intent1);
                break;
        }
    }
}
