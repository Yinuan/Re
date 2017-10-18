package com.klcxkj.rs.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.klcxkj.imagepicker.ImagePicker;
import com.klcxkj.imagepicker.bean.ImageItem;
import com.klcxkj.imagepicker.ui.ImageGridActivity;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.bo.BaseBo;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.bo.QINiu;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.CircleImageView;
import com.klcxkj.rs.widget.LoadingDialogProgress;
import com.klcxkj.rs.wxdemo.GlideImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * author : yinjuan
 * time： 2017/8/29 13:54
 * email：yin.juan2016@outlook.com
 * Description:领款申诉
 */
public class ACT_MonneyComplaints extends ACT_Network {

    private TextView mID,mName;//卡号，名字
    private EditText mNum;  //金额数量
    private ImageView mImg; //图片
    private Button mBtn;
    private LoadingDialogProgress progress;
    private CircleImageView iconDelete;
    private String QINIU_IMAGE = RSApplication.BASE_URL+"tStudent/getPicToken";//七牛

    private static final String MOONNEY_COMPLAINT = RSApplication.BASE_URL+"tStudent/getMoneyApply?";

    private ImagePicker imagePicker;
    private QINiu qiNiu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__monney_complaints);
        initView();
        initData();
        bindView();
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        CardInfo cardInfo = AppPreference.getInstance().getCardInfo();
        mID.setText(cardInfo.getCardID()+"");
        mName.setText(cardInfo.getEmployeeName());
    }


    /**
     * 控件初始化
     */
    private void initView() {
        showMenu("领款申诉");
        mID = (TextView) findViewById(R.id.monney_ID);
        mImg = (ImageView) findViewById(R.id.monney_img);
        mName = (TextView) findViewById(R.id.monney_name);
        mNum = (EditText) findViewById(R.id.monney_num);
        mBtn = (Button) findViewById(R.id.monney_btn);
        iconDelete = (CircleImageView) findViewById(R.id.delete_icon);
        mNum.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
}

    /**
     * 事件点击
     */
    private void bindView() {
        //选择图片
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //有图片的时候
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent1 = new Intent(ACT_MonneyComplaints.this, ImageGridActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);
            }
        });
        //提交
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String monneyNum =mNum.getText().toString();
                if (monneyNum.length()<=0){
                    toast("请填写申诉金额");
                    return;
                }
                mBtn.setEnabled(false);
                //上传图片
                if (images !=null && images.size()>0){
                    StringBuffer sb=new StringBuffer(QINIU_IMAGE);
                    sendGetRequest(sb.toString());
                }else {
                    submitMonneyComplaintToServer("");
                }
               progress = GlobalTools.getInstance().showDailog(ACT_MonneyComplaints.this,"提交..");


            }
        });
        iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (images==null){
                    iconDelete.setVisibility(View.GONE);
                    return;
                }
                iconDelete.setVisibility(View.GONE);
                images.clear();
                mImg.setImageResource(R.mipmap.add_imagepicker);
            }
        });
    }

    /**
     * 提交申诉
     */
    private void submitMonneyComplaintToServer(String reportImg) {
        UserInfo user =AppPreference.getInstance().getUserInfo();
        CardInfo card =AppPreference.getInstance().getCardInfo();

        StringBuffer sb =new StringBuffer(MOONNEY_COMPLAINT);
        sb.append("PrjID="+user.getPrjID());
        sb.append("&TelPhone="+user.getTelPhone());
        sb.append("&EmployeeID="+card.getEmployeeID());
        sb.append("&CardID="+card.getCardID());
        sb.append("&applyMoney="+mNum.getText().toString());
        sb.append("&ServerIP="+user.getServerIP());
        sb.append("&ServerPort="+user.getServerPort());
        sb.append("&applyImg="+reportImg);
        Log.d("ACT_MonneyComplaints", "sb:" + sb);
        sendGetRequest(sb.toString());


    }



    @Override
    protected int parseJson(JSONObject result, String url) throws JSONException {
        Log.d("ACT_MonneyComplaints", "result:" + result);
        Gson gson =new Gson();
        if (url.contains(QINIU_IMAGE)){
            qiNiu =gson.fromJson(result.toString(),QINiu.class);
            //上传图片
            if (qiNiu.getToken() !=null){
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String pathName = df.format(new Date());
                String reportImg =upLoadImage(images.get(0).getPath(),qiNiu.getToken(),pathName);
                Log.d("ACT_MeRepair","reportImg:=="+reportImg);
                submitMonneyComplaintToServer(qiNiu.getDomainName()+pathName);
            }else {
                submitMonneyComplaintToServer("");
            }
        }else if (url.contains(MOONNEY_COMPLAINT)){
            BaseBo baseBo =gson.fromJson(result.toString(),BaseBo.class);
            if (baseBo.isSuccess()){
                toast(baseBo.getMsg());
                showPop();
            }else {
                toast(baseBo.getMsg());

            }
        }


        return 0;
    }

    @Override
    protected void loadDatas() {
            if (progress!=null){
                progress.dismiss();
            }

    }

    @Override
    protected void loadError(JSONObject result) {
        if (progress!=null){
            progress.dismiss();
        }
        mBtn.setEnabled(true);

    }

    ArrayList<ImageItem> images = null;
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.d("ACT_MonneyComplaints", "images:" + images);
                if (images != null) {
                    iconDelete.setVisibility(View.VISIBLE);
                    Glide.with(ACT_MonneyComplaints.this)
                            .load(images.get(0).getPath())
                            .crossFade()
                            .into(mImg);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    Glide.with(ACT_MonneyComplaints.this)
                            .load(images.get(0))
                            .crossFade()
                            .into(mImg);
                }
            }
        }
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    private int getWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int width =(widthPixels*3)/4;
        return  width;
    }

    private void showPop() {
        View view = LayoutInflater.from(ACT_MonneyComplaints.this).inflate(R.layout.pop_style_3, null);
       TextView title = (TextView) view.findViewById(R.id.pop_3_content);
        Button btn = (Button) view.findViewById(R.id.pop_3_btn);
        title.setText("\u3000"+"我们会尽快处理并在处理完毕后短信通知您（三个工作日内）");
        final PopupWindow popupWindow = new PopupWindow(view,getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
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
                finish();
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
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }
}
