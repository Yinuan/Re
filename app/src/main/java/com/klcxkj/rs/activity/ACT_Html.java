package com.klcxkj.rs.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.klcxkj.rs.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

public class ACT_Html extends ACT_Network {

    private  String htmlName;
    private String htmlUrl ;
    private ProgressBar progressBar1;
    private com.tencent.smtt.sdk.WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__html);
        //x5窗口设定
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initview();
    }

    private void initview() {
        Intent intent =getIntent();
        htmlName=intent.getStringExtra("htmlName");
        htmlUrl =intent.getStringExtra("htmlUrl");
        if (htmlName.equals("duty")){
            showMenu("免责声明");
        }else if (htmlName.equals("version")){
            showMenu("版权声明");
        }else if (htmlName.equals("bindCard")){
            showMenu("绑卡流程");
        }else if (htmlName.equals("getCardByMyself")){
            showMenu("自主办卡流程");
        }else if (htmlName.equals("question")){
            showMenu("常见问题");
        }else if (htmlName.equals("messageDetail")){
            showMenu("消息详情");
        }else if (htmlName.equals("serviceItem")){
            showMenu("服务条款");
        }else if (htmlName.equals("messageType")){
            showMenu("新消息");
        }
        webView = (WebView) findViewById(R.id.html_webview);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        webView.loadUrl(htmlUrl);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                if (i==100){
                    progressBar1.setVisibility(View.GONE);
                }else {
                    progressBar1.setProgress(i);
                    progressBar1.setVisibility(View.VISIBLE);
                }
            }
        });
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
