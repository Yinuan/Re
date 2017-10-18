package com.klcxkj.rs.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.adapter.LRechargeRecrodingAdpater;
import com.klcxkj.rs.bean.RechargeRecording;
import com.klcxkj.rs.bean.RechargeRecrodingResult;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:充值记录
 */
public class ACT_Rechage_Recording extends ACT_Network {

    private ListView listView;
    private static final int QUERY_TYPE =4; //查询支付宝的充值记录
    private int count=1; //页数
    private List<RechargeRecording> mDatas;//数据源
    private List<RechargeRecording> listDatas;//分页显示数据
    private RechargeRecrodingResult recrodingResult;  //解析实体类
    private LRechargeRecrodingAdpater rAdpater;//充值适配器
    private Handler mHandler =new Handler();
    private int maxCount;
    private  SmartRefreshLayout refreshLayout;
    private LoadingDialogProgress progress;
    private RelativeLayout layout_null;  //无数据视图
    private static final String RECHARGE_RECORDING = RSApplication.BASE_URL+"tStudent/getStuBillList?";//充值记录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__rechage__recording);
        showMenu("充值记录");
        initView();
        initdata();
        bindView();
    }

    private void bindView() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                mDatas.clear();
                listDatas.clear();

                initdata();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mDatas.size()-listDatas.size()<=20 ){//20-40
                            //不在上拉
                            refreshLayout.setEnableLoadmore(false);
                            for (int i = maxCount; i <mDatas.size() ; i++) {
                                listDatas.add(mDatas.get(i));
                            }

                        }else { //40
                            maxCount=maxCount+20;
                            for (int i = maxCount-20; i <maxCount ; i++) {
                                listDatas.add(mDatas.get(i));
                            }
                        }
                        rAdpater.notifyDataSetChanged();
                    }
                },1850);


            }
        });
    }


    private void initView() {
        listDatas =new ArrayList<>();
        mDatas =new ArrayList<>();
        rAdpater =new LRechargeRecrodingAdpater(this);
        rAdpater.setList(listDatas);
        listView = (ListView) findViewById(R.id.listView_rechage_recording);
        layout_null = (RelativeLayout)findViewById(R.id.data_null);
        listView.setAdapter(rAdpater);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(false);

    }
    //初始化数据
    private void initdata() {
        maxCount =20;
       progress = GlobalTools.getInstance().showDailog(this,"加载..");
        loadDataFromServer();

    }
    private void loadDataFromServer(){
        StringBuffer sb =new StringBuffer(RECHARGE_RECORDING);
        sb.append("PrjID="+getNewUser().getPrjID());
        sb.append("&EmployeeID="+getNewUser().getEmployeeID());
        sb.append("&PageIndex="+count);
        sb.append("&queryType="+QUERY_TYPE);
        sb.append("&ServerIP="+getNewUser().getServerIP());
        sb.append("&ServerPort="+getNewUser().getServerPort());
        sendGetRequest(sb.toString());
    }

  

    @Override
    protected int parseJson(JSONObject result, String url) throws JSONException {
        Log.d("ACT_Rechage_Recording", "result:" + result);
        Gson gson =new Gson();
        recrodingResult =gson.fromJson(result.toString(),RechargeRecrodingResult.class);

        return 0;
    }

    @Override
    protected void loadDatas() {
      progress.dismiss();
        if (recrodingResult.getObj()!=null && recrodingResult.getObj().size()>0) {
            mDatas.addAll(recrodingResult.getObj());
           if (mDatas.size()>20){
               for (int i = 0; i <20 ; i++) {
                   listDatas.add(mDatas.get(i));
               }
               //大于20条数据的时候，开放下拉刷新
               refreshLayout.setEnableLoadmore(true);
           }else {
               listDatas.addAll(mDatas);
           }
           rAdpater.setList(listDatas);
            rAdpater.notifyDataSetChanged();
        }else {
            refreshLayout.setVisibility(View.GONE);
            layout_null.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void loadError(JSONObject result) {
        progress.dismiss();
    }


}
