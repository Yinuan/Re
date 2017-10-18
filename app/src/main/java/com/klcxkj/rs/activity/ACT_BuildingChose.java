package com.klcxkj.rs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.boss.activity.Boss_BuildingDetail;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.adapter.LRoomAdpater;
import com.klcxkj.rs.bo.Ban;
import com.klcxkj.rs.bo.Building;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.ClearEditText;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:楼栋和宿舍选择
 */
public class ACT_BuildingChose extends ACT_Network  {

    private ClearEditText clearEditText;
    private TextView roomAddress;
    private ListView listView;
    private List<String> data ;
    private List<Ban> buildDatas;
    private LRoomAdpater adpater;
    private LoadingDialogProgress progress;

    private String cType;//类型
    private String buildName =""; //楼栋

    private static String BUILDING_URL = RSApplication.BASE_URL + "tStudent/studentGetBuildingInfo?"; //楼栋

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__building_chose);
        //初始化数据和控件绑定
        initView();
        initData();
        bindClick();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String message){
        Log.d("ACT_BuildingChose", "onEvent"+message);
        if (message.equals("updateUserInfo")){//获取到卡片绑定成功的消息
           finish();
        }else if (message.equals("choseRoom")){
            finish();
        }

    }

    private void initData() {
      progress = GlobalTools.getInstance().showDailog(this,"加载");
        loadDatasFrmServer();
        data =new ArrayList<>();
        adpater =new LRoomAdpater(this);
        adpater.setList(data);
        listView.setAdapter(adpater);
    }



    private void initView() {
        Intent intent =getIntent();
        cType =intent.getStringExtra("type");
        buildName =intent.getStringExtra("buildingName");
        showMenu("楼栋选择");
        clearEditText = (ClearEditText) findViewById(R.id.room_search);
        roomAddress = (TextView) findViewById(R.id.room_address);
        listView = (ListView) findViewById(R.id.list_room);
        if (buildName!=null){
            if (buildName.equals("noBuildName")){
                roomAddress.setVisibility(View.GONE);
            }
            roomAddress.setText("当前: "+buildName);
        }else {
            roomAddress.setText("当前:");
        }


    }
    private void bindClick() {
        //查询
        clearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ("".equals(editable.toString())){
                    adpater =new LRoomAdpater(ACT_BuildingChose.this);
                    adpater.setList(data);
                    listView.setAdapter(adpater);
                    return;
                }
                List<String> sData =new ArrayList<String>();
                for (String room:data) {
                    if (room.contains(editable.toString())){
                        sData.add(room);
                    }
                }
                adpater =new LRoomAdpater(ACT_BuildingChose.this);
                adpater.setList(sData);
                listView.setAdapter(adpater);
            }
        });

        //listview点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (cType.equals("boss")){
                    //跳转到房间号选择
                    Intent intent =new Intent(ACT_BuildingChose.this,Boss_BuildingDetail.class);
                    intent.putExtra("buildingID",buildDatas.get(position).getBuildingID());
                    intent.putExtra("buildingName",buildDatas.get(position).getBuildingName());
                    startActivity(intent);
                }else {
                    //跳转到房间号选择
                    Intent intent =new Intent(ACT_BuildingChose.this,ACT_RoomChose.class);
                    Log.d("ACT_BuildingChose", buildDatas.get(position).getBuildingID());
                    intent.putExtra("type",cType);
                    intent.putExtra("buildingID",buildDatas.get(position).getBuildingID());
                    intent.putExtra("buildingName",buildDatas.get(position).getBuildingName());
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * 网络请求数据
     */
    private void loadDatasFrmServer() {
        StringBuffer sb = new StringBuffer(BUILDING_URL);
        sb.append("PrjID=" + getNewUser().getPrjID());
        sb.append("&ServerIP=" + getNewUser().getServerIP());
        sb.append("&ServerPort=" + getNewUser().getServerPort());
        Log.d("ACT_BuildingChose", "sb:" + sb);
        sendGetRequest(sb.toString());
    }
    @Override
    protected int parseJson(JSONObject result, String url) throws JSONException {
        Log.d("ACT_BuildingChose", "result:" + result);
        Gson gson =new Gson();
        Building building =gson.fromJson(result.toString(),Building.class);
        if (building.getObj() !=null) {
            buildDatas =building.getObj();
            for (int i = 0; i <buildDatas.size() ; i++) {
                data.add(buildDatas.get(i).getBuildingName());
            }
            adpater.notifyDataSetChanged();

        }
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
