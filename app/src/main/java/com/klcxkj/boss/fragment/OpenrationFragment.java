package com.klcxkj.boss.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.klcxkj.boss.activity.Boss_ProjectManage;
import com.klcxkj.rs.R;
import com.klcxkj.rs.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * autor:OFFICE-ADMIN
 * time:2017/10/9
 * email:yinjuan@klcxkj.com
 * description:业务
 */

public class OpenrationFragment extends BaseFragment implements View.OnClickListener{

    //我的项目
    private RelativeLayout myProject;
    //维护
    private RelativeLayout projectManage;
    private RelativeLayout deviceMiss;
    private RelativeLayout faultReport;
    private RelativeLayout workDiary;//工作日记
    @Override
    protected int getLayoutId() {
        return R.layout.boss_fg_operation;
    }

    @Override
    protected void initLayout() {
        //我的项目
        myProject = (RelativeLayout) mView.findViewById(R.id.boss_weihu_my_project);
        //维护
        projectManage = (RelativeLayout) mView.findViewById(R.id.boss_weihu_project_manage);
        deviceMiss = (RelativeLayout) mView.findViewById(R.id.boss_weihu_device_miss);
        faultReport = (RelativeLayout) mView.findViewById(R.id.boss_weihu_guzhnag_take);
        workDiary= (RelativeLayout) mView.findViewById(R.id.boss_weihu_gongzuo_rji);



        //
        myProject.setOnClickListener(this);
        projectManage.setOnClickListener(this);
        deviceMiss.setOnClickListener(this);
        faultReport.setOnClickListener(this);
        workDiary.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.boss_weihu_project_manage: //维护项目管理
                startActivity(new Intent(getActivity(), Boss_ProjectManage.class));
                break;
        }
    }
}
