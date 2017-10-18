package com.klcxkj.rs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.activity.ACT_ApplyCard_Rechge;
import com.klcxkj.rs.activity.ACT_BillActivity;
import com.klcxkj.rs.activity.ACT_CampusCardApply;
import com.klcxkj.rs.activity.ACT_CampusCardBind;
import com.klcxkj.rs.activity.ACT_CampusCardLoss;
import com.klcxkj.rs.activity.ACT_FillCard;
import com.klcxkj.rs.activity.ACT_Html;
import com.klcxkj.rs.activity.ACT_MeContactUs;
import com.klcxkj.rs.activity.ACT_MeMessageCenter;
import com.klcxkj.rs.activity.ACT_MeMessageDetail;
import com.klcxkj.rs.activity.ACT_MeRepair;
import com.klcxkj.rs.activity.ACT_MeRidicule;
import com.klcxkj.rs.activity.ACT_MonneyComplaints;
import com.klcxkj.rs.activity.ACT_OperationGuide;
import com.klcxkj.rs.activity.ACT_Rechage;
import com.klcxkj.rs.activity.ACT_TCode;
import com.klcxkj.rs.bean.RedPointBean;
import com.klcxkj.rs.bean.UpdatePassResult;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.bo.CardSelfIsOk;
import com.klcxkj.rs.bo.SystemInfo;
import com.klcxkj.rs.download.DownLoadUtils;
import com.klcxkj.rs.download.DownloadApk;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * autor:OFFICE-ADMIN
 * time:2017/8/23
 * email:yinjuan@klcxkj.com
 * description:
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    //小菜单
    private RelativeLayout operationGuide;//操作指引
    private RelativeLayout getCardByMyself;//自主办卡
    private RelativeLayout queryBill;//查询账单
    private RelativeLayout messageCenter;//消息中心
    private RelativeLayout complaintMonney;//领款申诉
    private RelativeLayout faultReport;//故障报修
    private RelativeLayout feedBack;//意见反馈
    private RelativeLayout callUs;//联系我们
    private RelativeLayout tCode;//二维码
    //园
    private ImageView ciecleImg;
    //中间三大菜单
    private LinearLayout rechange;//充值
    private LinearLayout lossCard;//挂失
    private LinearLayout addCard;//补卡
    private TextView campusCardLoss;//挂失/解挂
    //一级
    private LinearLayout cardBinded; //绑定卡片
    private RelativeLayout cardUnBind;//未绑定卡片
    private Button bindCard;//绑定卡片按钮
    private TextView bindCardMsg;//绑定卡片左边的提示语
    private TextView cardId; //卡号
    private TextView cardStatus; //卡状态
    private TextView cardMonney; //卡上余额
    private TextView card_monney_remain;//未领金额
    //头部，顶部
    private RelativeLayout top_layout;
    private LinearLayout bottom_layput;//底部
    private RedPointBean redPointBean;//红点点推送的实体类
    private TextView message; //消息
    //卡片
    private CardInfo cardInfo;
    private   UserInfo userInfo;
    //下来刷新
    private SmartRefreshLayout smartRefreshLayout;
    private String queryCardInfo = RSApplication.BASE_URL + "tStudent/studentGetCardInfo?";  //查询卡片信息
    private String RED_POINT =RSApplication.BASE_URL+"tStudent/getNewMsg?";//红点点
    private  static final String SYSTEM_INFO =RSApplication.BASE_URL+"tPrjInfo/sysInfo?";
    private static final String queryCardSelf =RSApplication.BASE_URL+"/tStudent/getHasChargeDev?";//自助办卡，补卡
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initLayout() {
        initView();
        //初始化数据
        initData();
        //注册点击事件
        bindOnclick();
        //bus注册
        EventBus.getDefault().register(this);
    }

    private void initView() {
        smartRefreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.home_refreshLayout);
        //3级
        operationGuide = (RelativeLayout) mView.findViewById(R.id.layout_function_introduction);
        feedBack = (RelativeLayout) mView.findViewById(R.id.layout_make_complaints);
        faultReport = (RelativeLayout) mView.findViewById(R.id.layout_ridicule_repair);
        callUs = (RelativeLayout) mView.findViewById(R.id.layout_contact_us);
        messageCenter = (RelativeLayout) mView.findViewById(R.id.layout_message_center);
        queryBill = (RelativeLayout) mView.findViewById(R.id.layout_home_bill);
        getCardByMyself = (RelativeLayout) mView.findViewById(R.id.button_apply_card);
        complaintMonney = (RelativeLayout) mView.findViewById(R.id.monney_complaints);
        ciecleImg = (ImageView) mView.findViewById(R.id.message_cricle);
        tCode = (RelativeLayout) mView.findViewById(R.id.home_erweima);
        //2级
        rechange = (LinearLayout) mView.findViewById(R.id.home_rechange);
        lossCard = (LinearLayout) mView.findViewById(R.id.home_loss);
        addCard = (LinearLayout) mView.findViewById(R.id.home_add_card);
        campusCardLoss = (TextView) mView.findViewById(R.id.home_card_campus_and_loss);
        //1级
        bindCardMsg = (TextView) mView.findViewById(R.id.home_card_bindBtn_tv);
        cardBinded = (LinearLayout) mView.findViewById(R.id.home_card_binded);
        cardUnBind = (RelativeLayout) mView.findViewById(R.id.home_card_unbind);
        bindCard = (Button) mView.findViewById(R.id.home_card_bindBtn);
        cardId = (TextView) mView.findViewById(R.id.text_card_ID);
        cardStatus = (TextView) mView.findViewById(R.id.text_card_statu);
        cardMonney = (TextView) mView.findViewById(R.id.text_card_monney);
        card_monney_remain = (TextView) mView.findViewById(R.id.text_card_monney_remain);

        //头部底部
        top_layout = (RelativeLayout) mView.findViewById(R.id.home_top_layout);
        bottom_layput = (LinearLayout) mView.findViewById(R.id.home_bottom_layout);
        message = (TextView) mView.findViewById(R.id.home_message_tv);
        top_layout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //4.反注册广播接收器
        DownloadApk.unregisterBroadcast(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        //判断卡片是否绑定了
        Log.d("HomeFragment", "onResume");
        if (CardIsBind()){  //绑定
            //查询卡信息
            cardInfo = AppPreference.getInstance().getCardInfo();
            //显示卡片信息
            showView();
            loadCardDatasforServer();
            cardBinded.setVisibility(View.VISIBLE);
            cardUnBind.setVisibility(View.GONE);

        }else {
            //未绑定
            cardBinded.setVisibility(View.GONE);
            cardUnBind.setVisibility(View.VISIBLE);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String message){
        Log.d("HomeFragment", "message"+message);
        if (message.equals("cardIsUnbind")){ //获取到卡片解除绑定成功的消息
            //更新卡片状态
            if (CardIsBind()){
                cardBinded.setVisibility(View.VISIBLE);
                cardUnBind.setVisibility(View.GONE);
            }else {
                bindCardMsg.setText("绑定校园卡以查看余额");
                bindCard.setText("绑卡");
                cardBinded.setVisibility(View.GONE);
                cardUnBind.setVisibility(View.VISIBLE);
            }
        }else if (message.equals("campusCardLoss")){  //挂失与解挂
            /* cardInfo =AppPreference.getInstance().getCardInfo();
            String status ="";
            switch (cardInfo.getNCardStatusID()){
                case 0:
                    status ="正常";
                    break;
                case 1:
                    status ="挂失";
                    break;
                case 2:
                    status ="退卡";
                    break;
                case 3:
                    status ="未领卡";
                    break;
                case 4:
                    status ="销户";
                    break;
            }
            cardStatus.setText("("+status+")");
            if (status.equals("挂失")){
                campusCardLoss.setText("解挂");
            }else if (status.equals("正常")){
                campusCardLoss.setText("挂失");
            }*/

        }else if (message.equals("cardIsBinded")){//获取到卡片绑定成功的消息
           // initData();
        }else if (message.equals("jiguang")){  //极光推送
            //消息中心
            getMessageRedPoint();
            //卡机
            queryCardSelfIsOk();
            //领款申诉
            loadCardDatasforServer();
        }else if (message.equals("applyCardIsSucess")){ //自助申卡成功
           //查询卡片信息
            Log.d("HomeFragment", "applyCardIsSucess");
           // userInfo =AppPreference.getInstance().getUserInfo();
           // loadCardDatasforServer();
        }else if (message.equals("cardApplyOk")){ //卡片充值了后


        }
    }



    private void initData() {

        //1.注册下载广播接收器
        DownloadApk.registerBroadcast(getActivity());
        //2.删除已存在的Apk
        DownloadApk.removeFile(getActivity());

        //获取系统的消息
       getSystemInfo();
    }


    private void getSystemInfo() {
        HashMap<String,String> map =new HashMap<>();
        map.put("ServerIP",getNewUser().getServerIP());
        map.put("ServerPort",getNewUser().getServerPort()+"");
       sendPostRequest(SYSTEM_INFO,map);
    }

    /**
     * 查询卡片信息
     */
    private void loadCardDatasforServer() {
        StringBuffer sb = new StringBuffer(queryCardInfo);
        sb.append("PrjID=" + userInfo.getPrjID());
        sb.append("&EmployeeID=" + userInfo.getEmployeeID());
        sb.append("&ServerIP=" + userInfo.getServerIP());
        sb.append("&ServerPort=" + userInfo.getServerPort());
        Log.d("HomeFragment", "sb:" + sb);
        sendGetRequest(sb.toString());
    }

    private boolean CardIsBind() {
         userInfo = AppPreference.getInstance().getUserInfo();
        if (userInfo.getEmployeeID()==0) { //未绑卡
            return false;
        }
        return true;
    }

    private void bindOnclick() {
        operationGuide.setOnClickListener(this);
        feedBack.setOnClickListener(this);
        faultReport.setOnClickListener(this);
        callUs.setOnClickListener(this);
        messageCenter.setOnClickListener(this);
        queryBill.setOnClickListener(this);
        getCardByMyself.setOnClickListener(this);
        complaintMonney.setOnClickListener(this);

        rechange.setOnClickListener(this);
        addCard.setOnClickListener(this);
        lossCard.setOnClickListener(this);
        bindCard.setOnClickListener(this);

        top_layout.setOnClickListener(this);
        tCode.setOnClickListener(this);

        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                loadCardDatasforServer();
            }
        });

    }

    /**
     * 红点
     */
    private void getMessageRedPoint(){
        StringBuffer sb =new StringBuffer(RED_POINT);
        sb.append("PrjID="+userInfo.getPrjID());
        sb.append("&TelPhone="+userInfo.getTelPhone());
        sb.append("&ServerIP="+userInfo.getServerIP());
        sb.append("&ServerPort="+userInfo.getServerPort());
        sendGetRequest(sb.toString());
    }

    /**
     * 判断是否可以自助办卡，补卡
     */
    private void queryCardSelfIsOk(){
        StringBuffer sb =new StringBuffer(queryCardSelf);
        sb.append("PrjID="+userInfo.getPrjID());
        sb.append("&ServerIP="+userInfo.getServerIP());
        sb.append("&ServerPort="+userInfo.getServerPort());
        sendGetRequest(sb.toString());
    }
    @Override
    public void loadLayout() {

    }

    @Override
    protected int parseJson(JSONObject result, String url) throws JSONException {
        Log.d("HomeFragment", "result:" + result);
        Gson gson =new Gson();
        if (url.contains(queryCardInfo)){
            Log.d("HomeFragment", "queryCardInfo:" + result);
            cardInfo =gson.fromJson(result.toString(),CardInfo.class);
            //保存卡片信息
            if (cardInfo.getCardID() !=0){
                AppPreference.getInstance().saveCardInfo(result.toString());
            }
            //更新UI
            showView();
        }else if (url.contains(RED_POINT)){
            //top open
             redPointBean = gson.fromJson(result.toString(),RedPointBean.class);
            if (redPointBean.getRedPoint()==1){
                ciecleImg.setVisibility(View.VISIBLE);
            }
            message.setText(redPointBean.getRepTitle());
            top_layout.setVisibility(View.VISIBLE);
            bottom_layput.setVisibility(View.GONE);

        }else if (url.contains(queryCardSelf)){
            Log.d("HomeFragment", "queryCardSelf:" + result);
            CardSelfIsOk cardIsOk =gson.fromJson(result.toString(),CardSelfIsOk.class);
            if (cardIsOk.getSuccess().equals("true")){
                //可以自助办卡，补卡
                //cardIsOk.getHasChargeDev()
                userInfo.setHasChargeDev(cardIsOk.getHasChargeDev());

                //更新用户信息
                AppPreference.getInstance().saveLoginUser(userInfo);
                Log.d("HomeFragment", "userInfo.getHasChargeDev():" + userInfo.getHasChargeDev());
            }

        }


        return 0;
    }

    private void showView() {
        if (cardInfo ==null){
            return;
        }
        if (cardInfo.getPrefillMoney()>0) {
            //提示充值
            bindCardMsg.setText("已申卡或已补卡待充值，需预充"+cardInfo.getPrefillMoney()+"元才可领卡");
            bindCard.setText("充值");
            //未绑定
            cardBinded.setVisibility(View.GONE);
            cardUnBind.setVisibility(View.VISIBLE);
        }else {
            if (CardIsBind()){
                cardBinded.setVisibility(View.VISIBLE);
                cardUnBind.setVisibility(View.GONE);
            }else {
                //未绑定
                cardBinded.setVisibility(View.GONE);
                cardUnBind.setVisibility(View.VISIBLE);
            }
        }
        cardId.setText(cardInfo.getCardID()+"");
        String status ="";

        switch (cardInfo.getNCardStatusID()){
            case 0:
                status ="正常";
                break;
            case 1:
                status ="挂失";
                break;
            case 2:
                status ="退卡";
                break;
            case 3:
                status ="未领卡";
                break;
            case 4:
                status ="销户";
                break;
        }
        cardStatus.setText("("+status+")");
        if (status.equals("挂失")){
            campusCardLoss.setText("解挂");
        }else if (status.equals("正常")){
            campusCardLoss.setText("挂失");
        }
        cardMonney.setText(cardInfo.getNCardValue()+"");
        card_monney_remain.setText(cardInfo.getAccountMoney()+"");

    }
    private String getcardStats(){
        if (cardInfo ==null){
            return "未知";
        }
        String status ="";
        switch (cardInfo.getNCardStatusID()){
            case 0:
                status ="正常";
                break;
            case 1:
                status ="挂失";
                break;
            case 2:
                status ="退卡";
                break;
            case 3:
                status ="未领卡";
                break;
            case 4:
                status ="销户";
                break;
        }
        return status;
    }
    @Override
    protected void loadDatas() {
       // showView();
    }

    @Override
    protected void loadError(JSONObject result) {
        showView();
    }

    @Override
    protected void handleErrorResponse(String url, VolleyError error) {
        super.handleErrorResponse(url, error);
    }

    private SystemInfo sysytemInfo;
    @Override
    protected void handleResponse(String url, JSONObject json) {
        super.handleResponse(url, json);
        Log.d("HomeFragment", "sysytem+json:" + json);
        Gson gson = new Gson();
        UpdatePassResult resp = gson.fromJson(json.toString(), UpdatePassResult.class);
        if (resp.getSuccess().equals("true")){
             sysytemInfo =gson.fromJson(json.toString(),SystemInfo.class);
        }
        if (sysytemInfo !=null){
            String sysVersion =sysytemInfo.getVersion().replace(".","");
            String localVersion =getLocalVersionName(getActivity()).replace(".","");
            if (!localVersion.equals(sysVersion)){
                if (sysytemInfo.getForceUpdate().equals("1")){ //强制升级
                   showPop3();
                }else {
                    //弹出一个dialog，提示升级
                    //toast("当前版本不是最新，请升级");
                    showPop();
                    Log.d("HomeFragment", sysytemInfo.getForceUpdateContent());
                }


                Log.d("HomeFragment", getLocalVersionName(getActivity()));
                Log.d("HomeFragment", sysytemInfo.getVersion());

            }
        }


    }

    /**
     * 下载更新
     */
    private void downApk(String msg){
         if (DownLoadUtils.getInstance(getActivity().getApplicationContext()).canDownload()) {
                DownloadApk.downloadApk(getActivity().getApplicationContext(),sysytemInfo.getUpgradeURL(), "校园热水更新", "Re");
               // Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
            } else {
                DownLoadUtils.getInstance(getActivity().getApplicationContext()).skipToDownloadManager();

            }
    }
    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_function_introduction: //操作指引
                Intent intent = new Intent(getActivity(), ACT_OperationGuide.class);
                Bundle bundle =new Bundle();
                bundle.putSerializable("sysytemInfo",sysytemInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layout_make_complaints://意见反馈
                startActivity(new Intent(getActivity(), ACT_MeRidicule.class));
                break;
            case R.id.layout_ridicule_repair://故障报修
                if (CardIsBind()) {
                    startActivity(new Intent(getActivity(),ACT_MeRepair.class));
                }else {
                    toast("请先自助办卡或绑定卡片");
                }
                break;
            case R.id.layout_contact_us://联系我们
                startActivity(new Intent(getActivity(), ACT_MeContactUs.class));
                break;
            case R.id.layout_message_center://消息中心
                ciecleImg.setVisibility(View.GONE);
                Intent intent1 =new Intent(getActivity(), ACT_MeMessageCenter.class);
                startActivity(intent1);
                break;
            case R.id.layout_home_bill: //账单中心
                if (CardIsBind()) {
                    startActivity(new Intent(getActivity(), ACT_BillActivity.class));
                }else {
                    toast("请先自助办卡或绑定卡片");
                }

                break;
            case R.id.button_apply_card: //自助办卡
                if (CardIsBind()){
                    toast("您已办卡，不需办卡!");
                    return;
                }else {
                   UserInfo userIn =AppPreference.getInstance().getUserInfo();
                    Log.d("HomeFragment", "userIn.getHasChargeDev()2:" + userIn.getHasChargeDev());
                    int msg =userIn.getHasChargeDev();
                    if (msg==1){
                        startActivity(new Intent(getActivity(), ACT_CampusCardApply.class));
                    }else {
                        showPop2();
                    }
                }

                break;
            case R.id.monney_complaints://领款申诉
                if (CardIsBind()) {
                    showPopOut();

                }else {
                    toast("请先自助办卡或绑定卡片");
                }

                break;
            case R.id.home_rechange://充值
                if (CardIsBind()) {
                    switch (cardInfo.getNCardStatusID()){
                        case 0:  //正常
                            startActivity(new Intent(getActivity(), ACT_Rechage.class));
                            break;
                        case 1: //挂失
                          toast("您的卡片已经挂失，不能充值!");
                            break;
                        case 2: //退卡
                            toast("您的卡片已经退卡，不能充值!");
                            break;
                        case 3:  //未领卡
                            double perMonny =cardInfo.getPrefillMoney();
                            if (perMonny>0){
                                Intent intent2 =new Intent(getActivity(),ACT_ApplyCard_Rechge.class);
                                intent2.putExtra("perMonny",perMonny+"");
                                startActivity(intent2);
                            }else {
                                startActivity(new Intent(getActivity(), ACT_Rechage.class));
                            }

                            break;
                        case 4: // 销户
                            toast("您的卡片已经销户，不能充值!");
                            break;
                    }

                }else {
                    toast("请先自助办卡或绑定卡片");
                }

                break;
            case R.id.home_loss://挂失
                if (CardIsBind()) {
                    switch (cardInfo.getNCardStatusID()){
                        case 0:  //正常
                            startActivity(new Intent(getActivity(), ACT_CampusCardLoss.class));
                            break;
                        case 1: //挂失
                            startActivity(new Intent(getActivity(), ACT_CampusCardLoss.class));
                            break;
                        case 2: //退卡
                            toast("您的卡片已经退卡，不能挂失!");
                            break;
                        case 3:  //未领卡
                            toast("您有卡片待领，请先领卡!");
                            break;
                        case 4: // 销户
                            toast("您的卡片已经销户，不能挂失!");
                            break;
                    }

                }else {
                    toast("请先自助办卡或绑定卡片");
                }
                break;
            case R.id.home_add_card://补卡
                if (CardIsBind()) {
                    switch (cardInfo.getNCardStatusID()){
                        case 0:  //正常
                           toast("您的卡片状态正常，不需补卡");
                            break;
                        case 1: //挂失
                            int msg =userInfo.getHasChargeDev();
                            if (msg==1){
                                startActivity(new Intent(getActivity(), ACT_FillCard.class));
                            }else {
                                showPop2();
                            }
                            break;
                        case 2: //退卡
                            toast("您的卡片已经退卡，不能补卡!");
                            break;
                        case 3:  //未领卡
                            toast("您有卡片待领，请先领卡!");
                            break;
                        case 4: // 销户
                            toast("您的卡片已经销户，不能补卡!");
                            break;
                    }

                }else {
                    toast("请先自助办卡或绑定卡片");
                }
                break;
            case R.id.home_card_bindBtn://绑卡

                if (bindCard.getText().toString().equals("绑卡")){
                    startActivity(new Intent(getActivity(), ACT_CampusCardBind.class));
                }else if (bindCard.getText().toString().equals("充值")){
                    Intent intent2 =new Intent(getActivity(), ACT_ApplyCard_Rechge.class);
                    intent2.putExtra("perMonny",cardInfo.getPrefillMoney()+"");
                    startActivity(intent2);
                }
                break;
            case R.id.home_top_layout://消息点击
                top_layout.setVisibility(View.GONE);
                bottom_layput.setVisibility(View.VISIBLE);
                ciecleImg.setVisibility(View.GONE);
                if (redPointBean.getMsgType().equals("1")){
                    Intent intent2 =new Intent(getActivity(), ACT_Html.class);
                    intent2.putExtra("htmlName","messageType");
                    intent2.putExtra("htmlUrl",redPointBean.getHtmlPath());
                    startActivity(intent2);
                }else {
                    Intent intent2 =new Intent(getActivity(), ACT_MeMessageDetail.class);
                    intent2.putExtra("message_title",redPointBean.getRepTitle());
                    intent2.putExtra("message_content",redPointBean.getRepContent());
                    startActivity(intent2);
                }

                break;

            case R.id.home_erweima:  //卡片的二维码信息
                if (CardIsBind()) {
                    switch (cardInfo.getNCardStatusID()){
                        case 0:  //正常
                            toast("此功能在未领卡状态下才可使用!");
                            break;
                        case 1: //挂失
                            toast("此功能在未领卡状态下才可使用!");
                            break;
                        case 2: //退卡
                            toast("此功能在未领卡状态下才可使用!");
                            break;
                        case 3:  //未领卡
                            if (cardInfo.getPrefillMoney()>0) {
                                toast("请先充值再去领卡!");
                                return;
                            }
                            startActivity(new Intent(getActivity(), ACT_TCode.class));
                            break;
                        case 4: // 销户
                            toast("此功能在未领卡状态下才可使用!");
                            break;
                    }

                }else {
                  //  startActivity(new Intent(getActivity(), XizaoActivity.class));
                    toast("请先自助办卡或绑定卡片");
                }
                break;


        }
    }




    private void showPop() {
        final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_2, null);
        TextView title = (TextView) view1.findViewById(R.id.pop_title);
        TextView content = (TextView) view1.findViewById(R.id.pop_content);
        Button btn_ok = (Button) view1.findViewById(R.id.pop_btn_confrim);
        Button btn_cacle = (Button) view1.findViewById(R.id.pop_btn_cancle);
        title.setText("提示");
        String sysVersion =sysytemInfo.getVersion().replace(".","");
        StringBuffer sb =new StringBuffer();
        sb.append("V");
        for (int i = 0; i <sysVersion.length() ; i++) {
           sb.append(sysVersion.substring(i,i+1));
            sb.append(".");
        }
        sb.deleteCharAt(sb.length()-1);
        content.setText("\u3000"+"检测到新版本 "+sb.toString()+", 是否立即更新?");
        btn_ok.setText("现在更新");
        btn_cacle.setText("稍后更新");

        final PopupWindow popupWindow = new PopupWindow(view1, getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setFocusable(false);// 取得焦点
        //点击外部消失
         // popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 软键盘不会挡着popupwindow
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
            }
        });
        //popupWindow.showAsDropDown(mSubmit);
        btn_cacle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                String msg ="下载已开始，您可以点击查看当前下载进度条";
                downApk(msg);

               /* UpdaterConfig config = new UpdaterConfig.Builder(getActivity())
                        .setTitle("校园热水")
                        .setDescription(getString(R.string.system_download_description))
                        .setFileUrl(sysytemInfo.getUpgradeURL())
                        .setCanMediaScanner(true)
                        .build();
                Updater.get().showLog(true).download(config);*/
            }
        });
        // 监听菜单的关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        // 监听触屏事件
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void showPop2() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_3, null);
        TextView title = (TextView) view.findViewById(R.id.pop_3_content);
        Button btn = (Button) view.findViewById(R.id.pop_3_btn);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setText("\u3000"+"自助办卡功能暂未开通，请到管理中心去办理");
        final PopupWindow popupWindow = new PopupWindow(view, getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setFocusable(false);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        //  popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 软键盘不会挡着popupwindow
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //popupWindow.showAsDropDown(mSubmit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //
            }
        });
        // 监听菜单的关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        // 监听触屏事件
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    private int getWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int width =(widthPixels*3)/4;
        return  width;
    }
    private void showPop3() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_3, null);
        TextView title = (TextView) view.findViewById(R.id.pop_3_content);
        Button btn = (Button) view.findViewById(R.id.pop_3_btn);
        btn.setText("开始更新");
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setText("对不起，当前版本V"+getLocalVersionName(getActivity())+" 已不可用!请更新到更高版本");
        final PopupWindow popupWindow = new PopupWindow(view, getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setFocusable(false);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        //  popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
      //  popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 软键盘不会挡着popupwindow
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //popupWindow.showAsDropDown(mSubmit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //
                String msg ="下载已开始，您可以点击查看当前下载进度条";
                downApk(msg);

            }
        });
        // 监听菜单的关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        // 监听触屏事件
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

    }

    private void showPopOut() {
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_2b, null);
        TextView title = (TextView) view1.findViewById(R.id.pop_title);
        TextView content = (TextView) view1.findViewById(R.id.pop_content);
        Button btn_ok = (Button) view1.findViewById(R.id.pop_btn_confrim);
        Button btn_cacle = (Button) view1.findViewById(R.id.pop_btn_cancle);
        title.setText("提示");
        content.setText("是否领款异常?");
        btn_ok.setText("取消");
        btn_cacle.setText("确定");

        final PopupWindow popupWindow = new PopupWindow(view1, getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setFocusable(false);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        //  popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 软键盘不会挡着popupwindow
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view1, Gravity.CENTER, 0, 0);
        //popupWindow.showAsDropDown(mSubmit);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        btn_cacle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                startActivity(new Intent(getActivity(), ACT_MonneyComplaints.class));
            }
        });
        // 监听菜单的关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        // 监听触屏事件
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }


}
