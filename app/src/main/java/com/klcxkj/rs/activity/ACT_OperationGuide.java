package com.klcxkj.rs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.RSApplication;
import com.klcxkj.rs.bo.SystemInfo;
import com.klcxkj.rs.util.GlobalTools;
import com.klcxkj.rs.widget.LoadingDialogProgress;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * autor:OFFICE-ADMIN
 * time:2017/8/23
 * email:yinjuan@klcxkj.com
 * description:操作指引
 */
public class ACT_OperationGuide extends ACT_Network implements View.OnClickListener{

    private RelativeLayout opera_one,opera_two,opera_three;
    private  static final String SYSTEM_INFO =RSApplication.BASE_URL+"tPrjInfo/sysInfo?";
    private String html_bind;  //绑卡
    private String html_myself;  //自助绑卡
    private String html_question;  //常见问题
    private   SystemInfo systemInfo;//服务器获取的解析类
    private LoadingDialogProgress progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__operation_guide);
        initview();
        initdata();
    }

    private void initdata() {
        Intent intent =getIntent();
         systemInfo = (SystemInfo) intent.getSerializableExtra("sysytemInfo");
        if (systemInfo ==null){
            StringBuffer sb =new StringBuffer(SYSTEM_INFO);
            sb.append("ServerIP="+getNewUser().getServerIP());
            sb.append("&ServerPort="+getNewUser().getServerPort());
            sendGetRequest(sb.toString());
            progress = GlobalTools.getInstance().showDailog(this,"加载..");
        }else {
            html_bind =systemInfo.getBindCardHelpURL();
            html_myself =systemInfo.getOpenCardHelpURL();
            html_question =systemInfo.getQuestionHelpURL();
        }


    }


    private void initview() {
        showMenu("操作指引");
        opera_one = (RelativeLayout) findViewById(R.id.operation_one);
        opera_two = (RelativeLayout) findViewById(R.id.operation_two);
        opera_three = (RelativeLayout) findViewById(R.id.operation_three);

        opera_one.setOnClickListener(this);
        opera_two.setOnClickListener(this);
        opera_three.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.operation_one://绑卡流程
                Intent intent =new Intent(ACT_OperationGuide.this,ACT_Html.class);
                intent.putExtra("htmlName","bindCard");
                intent.putExtra("htmlUrl",html_bind);
                startActivity(intent);
                break;
            case R.id.operation_two://自主办卡流程
                Intent intent1 =new Intent(ACT_OperationGuide.this,ACT_Html.class);
                intent1.putExtra("htmlName","getCardByMyself");
                intent1.putExtra("htmlUrl",html_myself);
                startActivity(intent1);
                break;
            case R.id.operation_three://常见问题
                Intent intent2 =new Intent(ACT_OperationGuide.this,ACT_Html.class);
                intent2.putExtra("htmlName","question");
                intent2.putExtra("htmlUrl",html_question);
                startActivity(intent2);
                break;
        }

    }


    @Override
    protected int parseJson(JSONObject result, String url) throws JSONException {
        Log.d("ACT_OperationGuide", "result:" + result);
        Gson gson =new Gson();
         systemInfo =gson.fromJson(result.toString(),SystemInfo.class);
        return 0;
    }

    @Override
    protected void loadDatas() {
            progress.dismiss();
        if (systemInfo!=null) {
            html_bind =systemInfo.getBindCardHelpURL();
            html_myself =systemInfo.getOpenCardHelpURL();
            html_question =systemInfo.getQuestionHelpURL();
        }
    }

    @Override
    protected void loadError(JSONObject result) {
        progress.dismiss();

    }
}
