package com.klcxkj.rs.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.klcxkj.imagepicker.ImagePicker;
import com.klcxkj.imagepicker.bean.ImageItem;
import com.klcxkj.imagepicker.ui.ImageGridActivity;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.activity.ACT_BuildingChose;
import com.klcxkj.rs.activity.ACT_CampusCardBind;
import com.klcxkj.rs.activity.ACT_Login;
import com.klcxkj.rs.activity.ACT_RoomChose;
import com.klcxkj.rs.bean.UpdatePassResult;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.bo.QINiu;
import com.klcxkj.rs.widget.CircleImageView;
import com.klcxkj.rs.wxdemo.GlideImageLoader;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * autor:OFFICE-ADMIN
 * time:2017/8/23
 * email:yinjuan@klcxkj.com
 * description:
 */

public class MineFragment extends BaseFragment {


    private CircleImageView myhead_img; //我的头像
    private TextView myTellnumber,mySchool; //我的电话和学校
    private Button button_unbind; //绑定按钮
    private TextView myName,mySex,myIDCard; //我的姓名，性别，身份证号码
    private RelativeLayout myBuildingLine,myRoomLine; //楼栋，宿舍号布局一行
    private TextView myBuilding,myRoom;//楼栋，宿舍号
    private Button mButtonOut; //退出按钮
    private CardInfo mCardInfo; //卡片
    private  UserInfo userInfo;

    private String unbind_url = RSApplication.BASE_URL + "tStudent/cancelBinding?"; //解除绑定
    private String queryCardInfo = RSApplication.BASE_URL + "tStudent/studentGetCardInfo?";  //查询卡片信息
    private static String UPDATE_INFO =RSApplication.BASE_URL + "tStudent/studentUpdateUserInfo?"; //更新个人信息

    private ImagePicker imagePicker;
    private QINiu qiNiu;
    private String QINIU_IMAGE = RSApplication.BASE_URL+"tStudent/getPicToken";//七牛
    @Override
    protected int getLayoutId() {
        return R.layout.act_me_account;
    }





    @Override
    protected void initLayout() {
        initView();
        bindEvent();
        //初始化数据
        initData();
        EventBus.getDefault().register(this);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        Log.d("zzzzzzz", "mineFragmentOnCreate");
        Log.d("MineFragment", Thread.currentThread().getName());
    }

    private void initView() {
        myhead_img = (CircleImageView) mView.findViewById(R.id.mine_img);
        myTellnumber = (TextView) mView.findViewById(R.id.mine_tellnumber);
        mySchool = (TextView) mView.findViewById(R.id.mine_school);
        button_unbind = (Button) mView.findViewById(R.id.button_unbind);
        myName = (TextView) mView.findViewById(R.id.mine_name);
        mySex = (TextView) mView.findViewById(R.id.mine_sex);
        myIDCard = (TextView) mView.findViewById(R.id.mine_idcard);
        myBuildingLine = (RelativeLayout) mView.findViewById(R.id.mine_building_layout);
        myRoomLine = (RelativeLayout) mView.findViewById(R.id.mine_room_layout);
        myBuilding = (TextView) mView.findViewById(R.id.mine_building_tv);
        myRoom = (TextView) mView.findViewById(R.id.mine_dormitoryroom_tv);
        mButtonOut = (Button) mView.findViewById(R.id.button_out);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String message){
        Log.d("MineFragment", "onEvent:"+message);
        if (message.equals("cardIsBinded")){//获取到卡片绑定成功的消息
            loadCardDatasforServer();
        }else if (message.equals("updateUserInfo")){  //获取到修改个人信息成功
            loadCardDatasforServer();
        }else if (message.equals("applyCardIsSucess")){ //自助申卡
            loadCardDatasforServer();

        }

    }

    private void bindEvent() {

        mButtonOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showPopOut();

            }
        });

        button_unbind.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (button_unbind.getText().toString().equals("解除绑定")){
                    showPop();
                }else {
                    startActivity(new Intent(getActivity(), ACT_CampusCardBind.class));
                }


            }
        });
        //l楼栋选择
        myBuildingLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myBuildingLine", "mCardInfo:" + mCardInfo);
                if (mCardInfo == null) {
                    toast("请先自助申卡或绑定卡片");
                    return;
                }
                Intent intent = new Intent(getActivity(), ACT_BuildingChose.class);
                intent.putExtra("type","mineCenter");
                intent.putExtra("buildingName",myBuilding.getText().toString());
                startActivity(intent);
            }
        });
        //房间选择
        myRoomLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCardInfo == null) {
                    toast("请先自助申卡或绑定卡片");
                    return;
                }
                Intent intent = new Intent(getActivity(),
                        ACT_RoomChose.class);
                intent.putExtra("buildingID",mCardInfo.getBuildingID()+"");
                intent.putExtra("buildingName",myBuilding.getText().toString());
                intent.putExtra("type","mineCenter");
                if (String.valueOf(mCardInfo.getBuildingID()) ==null){
                    toast("请先选择楼栋");
                    return;
                }
                startActivity(intent);
            }
        });
        //头像选择
        myhead_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //有图片的时候
               if (userInfo.getEmployeeID()==0){
                   toast("请先绑定卡片");
                    return;
                }
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent1 = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);

            }
        });
    }

    private void initData() {
        userInfo =AppPreference.getInstance().getUserInfo();
        myTellnumber.setText(userInfo.getTelPhone());
        mySchool.setText(userInfo.getPrjName());

        Glide.with(getActivity())
                .load(userInfo.getHeadIcon())
                .crossFade()
                .dontAnimate()//第一次加载图片
                .error(R.mipmap.icon_defult)
                .into(myhead_img);
        if (userInfo.getEmployeeID()!=0){
            mCardInfo =AppPreference.getInstance().getCardInfo();
            if (mCardInfo !=null){
                //绑定了,直接从缓存拿
                button_unbind.setText("解除绑定");
                myName.setText(mCardInfo.getEmployeeName());
                String sex ="";
                switch (Integer.valueOf(mCardInfo.getSexID())){
                    case 0:
                        sex="女";
                        break;
                    case 1:
                        sex ="男";
                        break;
                }
                mySex.setText(sex);
                myIDCard.setText(mCardInfo.getIdentifier());
                myBuilding.setText(mCardInfo.getBuildingName());
                myRoom.setText(mCardInfo.getRoomName());


            }else {
                loadCardDatasforServer();
            }
        }else {
            //未绑定
            button_unbind.setText("绑定");
        }


    }

    /**
     * 查询卡片信息
     */
    private void loadCardDatasforServer() {
        userInfo =AppPreference.getInstance().getUserInfo();
        StringBuffer sb = new StringBuffer(queryCardInfo);
        sb.append("PrjID=" + userInfo.getPrjID());
        sb.append("&EmployeeID=" + userInfo.getEmployeeID());
        sb.append("&ServerIP=" + userInfo.getServerIP());
        sb.append("&ServerPort=" + userInfo.getServerPort());
        sendGetRequest(sb.toString());
    }


    @Override
    public void loadLayout() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MineFragment", "onResume");
    }

    @Override
    protected int parseJson(JSONObject result, String url) throws JSONException {
        Log.d("MineFragment", result.toString());
        Log.d("MineFragment", "url:"+url);
        Gson gson =new Gson();
        if (url.contains(unbind_url)) {
            if (result != null) {

                UpdatePassResult resp = gson.fromJson(result.toString(), UpdatePassResult.class);
                if (resp.getSuccess().equals("true")) {
                    //取消绑定成功
                    //①更新userInfo
                    userInfo.setEmployeeID(0);
                    AppPreference.getInstance().saveLoginUser(userInfo);
                    //②删除卡片缓存
                    AppPreference.getInstance().deleteCardInfo();
                    //③Ui更新
                    EventBus.getDefault().postSticky("cardIsUnbind");
                    mCardInfo =null;
                    button_unbind.setText("绑定");
                    myName.setText("--");
                    mySex.setText("--");
                    myIDCard.setText("--");
                    myBuilding.setText("");
                    myRoom.setText("");
                }
                toast(resp.getMsg());
            }

        }else if(url.contains(queryCardInfo)){ //查询卡片信息

            mCardInfo =gson.fromJson(result.toString(),CardInfo.class);
            myName.setText(mCardInfo.getEmployeeName());
            String sex ="";
            switch (Integer.valueOf(mCardInfo.getSexID())){
                case 0:
                    sex="女";
                    break;
                case 1:
                    sex ="男";
                    break;
            }
            mySex.setText(sex);
            myIDCard.setText(mCardInfo.getIdentifier());
            myBuilding.setText(mCardInfo.getBuildingName());
            myRoom.setText(mCardInfo.getRoomName());
            button_unbind.setText("解除绑定");
            //更新缓存卡片信息
            AppPreference.getInstance().saveCardInfos(mCardInfo);

        }else if (url.contains(QINIU_IMAGE)){

            qiNiu =gson.fromJson(result.toString(),QINiu.class);
            if (qiNiu.getToken() !=null){
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String pathName = df.format(new Date());
                upLoadImg(images.get(0).getPath(),qiNiu.getToken(),pathName);
            }
        }
        return 0;
    }



    @Override
    protected void loadDatas() {

    }

    @Override
    protected void loadError(JSONObject result) {
        //服务器出问题了


    }

    /**
     * 向服务器提交个人资料的修改
     */

    private void updateUserInfoToServer( String headicon) {
        HashMap<String,String> map =new HashMap<>();
        map.put("TelPhone",userInfo.getTelPhone());
        map.put("PrjID",userInfo.getPrjID()+"");
        map.put("EmployeeID",mCardInfo.getEmployeeID()+"");
        map.put("EmployeeName",mCardInfo.getEmployeeName());
        map.put("RoomID",mCardInfo.getRoomID()+"");
        map.put("SexID",mCardInfo.getSexID());
        map.put("Identifier",mCardInfo.getIdentifier());
        map.put("ServerIP",userInfo.getServerIP());
        map.put("ServerPort",userInfo.getServerPort()+"");
        map.put("headIcon","http://"+headicon);
        Log.d("MineFragment","headicon :" +headicon);
        sendPostRequest(UPDATE_INFO,map);
        StringBuffer sb =new StringBuffer(UPDATE_INFO);
        sb.append("TelPhone="+userInfo.getTelPhone());
        sb.append("&PrjID="+userInfo.getPrjID());
        sb.append("&EmployeeID="+userInfo.getEmployeeID());
        sb.append("&EmployeeName="+mCardInfo.getEmployeeName());
        sb.append("&RoomID="+mCardInfo.getRoomID());
        sb.append("&SexID="+mCardInfo.getSexID());
        sb.append("&Identifier="+mCardInfo.getIdentifier());
        sb.append("&ServerIP="+userInfo.getServerIP());
        sb.append("&ServerPort="+userInfo.getServerPort());
        sb.append("&headIcon="+headicon);
        Log.d("MineFragment", "sb:" + sb);

    }

    @Override
    protected void handleResponse(String url, JSONObject json) {
        super.handleResponse(url, json);
        //保存此图片路径
        //userInfo.setHeadIcon(images.get(0).getPath());
       // AppPreference.getInstance().saveLoginUser(userInfo);
        Log.d("MineFragment", "json:" + json);
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
    private void showPop() {
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_2, null);
        TextView title = (TextView) view1.findViewById(R.id.pop_title);
        TextView content = (TextView) view1.findViewById(R.id.pop_content);
        Button btn_ok = (Button) view1.findViewById(R.id.pop_btn_confrim);
        Button btn_cacle = (Button) view1.findViewById(R.id.pop_btn_cancle);
        title.setText("警告");
        content.setText("\u3000"+getResources().getString(R.string.pop_content));
        btn_ok.setText("解除绑定");
        btn_cacle.setText("再看看先");

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
                StringBuffer sb3 = new StringBuffer(unbind_url);
                sb3.append("telPhone=" +userInfo.getTelPhone());
                sb3.append("&ServerIP=" + userInfo.getServerIP());
                sb3.append("&ServerPort=" +userInfo.getServerPort());
                sendGetRequest(sb3.toString());
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
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.pop_style_2, null);
        TextView title = (TextView) view1.findViewById(R.id.pop_title);
        TextView content = (TextView) view1.findViewById(R.id.pop_content);
        Button btn_ok = (Button) view1.findViewById(R.id.pop_btn_confrim);
        Button btn_cacle = (Button) view1.findViewById(R.id.pop_btn_cancle);
        title.setText("提示");
        content.setText(getResources().getString(R.string.dialog_content_out_confirm));
        btn_ok.setText("确定");
        btn_cacle.setText("取消");

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
                AppPreference.getInstance().deleteLoginUser();
                AppPreference.getInstance().deleteCardInfo();
                Intent intent = new Intent();
                intent.setClass(getActivity(),
                        ACT_Login.class); // B为你按退出按钮所在的activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mCardInfo = null;
                System.exit(0);
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

    ArrayList<ImageItem> images = null;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 108;
    public static final int REQUEST_CODE_PREVIEW = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.d("mineFragment", "images:" + images);
                if (images != null) {
                    Glide.with(getActivity())
                            .load(images.get(0).getPath())
                            .crossFade()
                            .dontAnimate()//第一次加载图片
                            .error(R.mipmap.icon_defult)
                            .into(myhead_img);
                    //保存此图片路径
                    userInfo.setHeadIcon(images.get(0).getPath());
                    AppPreference.getInstance().saveLoginUser(userInfo);
                    //上传骑牛服务器
                    StringBuffer sb=new StringBuffer(QINIU_IMAGE);
                    sendGetRequest(sb.toString());
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    Glide.with(getActivity())
                            .load(images.get(0))
                            .crossFade()
                            .into(myhead_img);
                }
            }
        }
    }

    private void upLoadImg(String filePath, String token, final String fileName){
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(6)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(10)          // 服务器响应超时。默认60秒
                .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
                .recorder(null, null)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);
        File file =new File(filePath);
        uploadManager.put(file, fileName, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                Log.d("ACT_Network", "key:="+key+"info:=="+info+"response:=="+response);
                if (info.isOK()){
                    Log.d("ACT_Network","url..=:"+ key);
                    //修改个人信息
                    updateUserInfoToServer(qiNiu.getDomainName()+fileName);
                }

            }
        },null);

    }
}
