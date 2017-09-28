package com.klcxkj.rs.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.klcxkj.rs.R;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:预约洗澡计时
 */
public class Xizao_CountActivity extends Activity {

    private Button btn;
    private int minute=7;
    private int second=59;
    private TextView time;

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                        second--;
                    if (second==0){
                        minute--;
                        second=60;
                        time.setText("0"+minute+"'"+"00"+"''");
                    }else if (second <10){
                        time.setText("0"+minute+"'"+"0"+second+"''");
                    }else {
                        time.setText("0"+minute+"'"+second+"''");
                    }

                    if (minute>0){
                        Message message =handler.obtainMessage(1);
                        handler.sendMessageDelayed(message,1000);
                    }else {
                        time.setText("00'00''");
                        finish();
                    }
                    break;
            }
        }
    };
    //计时器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xizao__count);
        btn = (Button) findViewById(R.id.xizao_count_btn);
        time = (TextView) findViewById(R.id.xizao_count_time);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Message mess =handler.obtainMessage(1);
        handler.sendMessageDelayed(mess,1000);
        digui(3);
    }

    //递归算法
    double count =10.00;
    private void digui(int i){
        for (int j = 0; j <i ; j++) {
            count=count*0.100+count;
            Log.d("-------", "count:" + count);
        }
        Toast.makeText(this, "count:" + count, Toast.LENGTH_SHORT).show();
    }
}
