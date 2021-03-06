package com.klcxkj.rs.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.fragment.HomeFragment;
import com.klcxkj.rs.fragment.MineFragment;


public class ACT_MainUI extends FragmentActivity implements View.OnClickListener{

    private View tView;
    private FragmentManager fragmentManager;// 碎片管理器
    private int pagerPostion = 0;// 保存当前显示的是第几页
    private FrameLayout main_UI;
    private HomeFragment fragment_home;
    private MineFragment fragment_mine;
    private RelativeLayout home,mine;
    private TextView home_tv,mine_tv;
    private ImageView home_iv,mine_iv;
    private TextView top_menu_title,top_menu_rightTxt;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__main_ui);
        // 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的 //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else {
            tView =findViewById(R.id.top_menu_view);
            tView.setVisibility(View.GONE);
        }
        if (savedInstanceState != null) {
            pagerPostion = savedInstanceState.getInt("hot_water");
        }
        userInfo =AppPreference.getInstance().getUserInfo();
        initView();
        //初始化碎片12
        initFragment();
        //监听注册
        bindClick();
        //默认选中home
        getcheck(true,false);
    }
    /**
     * 重写onSaveInstanceState方法，保存状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("hot_water", pagerPostion);
        super.onSaveInstanceState(outState);

    }

    private void initView() {
        home = (RelativeLayout) findViewById(R.id.fragment_home);
        mine = (RelativeLayout) findViewById(R.id.fragment_mine);
        home_iv = (ImageView) findViewById(R.id.home_iv);
        home_tv = (TextView) findViewById(R.id.home_tv);
        mine_iv = (ImageView) findViewById(R.id.mine_iv);
        mine_tv = (TextView) findViewById(R.id.mine_tv);
        main_UI = (FrameLayout) findViewById(R.id.fragment_UI);
        top_menu_title = (TextView) findViewById(R.id.top_title);
        LinearLayout backBtn = (LinearLayout) findViewById(R.id.top_btn_back);
        backBtn.setVisibility(View.GONE);
        top_menu_rightTxt = (TextView) findViewById(R.id.top_right_text);


    }

    private void bindClick() {

        home.setOnClickListener(this);
        mine.setOnClickListener(this);
         top_menu_rightTxt.setOnClickListener(this);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragment_home = (HomeFragment) fragmentManager.findFragmentByTag("home");
        fragment_mine = (MineFragment) fragmentManager.findFragmentByTag("mine");
        setTabSelection(pagerPostion);// 第一次启动时选中第0个tab
    }
    private void setTabSelection(int pagerPostion) {
        getcheck(false,false); //重置
        //碎片事物管理
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (pagerPostion){
            case 0:
                getcheck(true,false);
                String schoolName =userInfo.getPrjName();
               // String schoolName ="中国人民解放军防化指挥工程学院挥工程学院挥";
                /**
                 * 0-18  20
                 * 19-20  18
                 * 21-23 16
                 * 24-30 14
                 */

                int length =schoolName.length();
                Log.d("ACT_MainUI", "length:" + length);
                if (length>0 && length<=18){
                    top_menu_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                }else if (length>18 && length<=22 ){
                    top_menu_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                }else if (length>22 && length<=25){
                    top_menu_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                }else if (length>25){
                    top_menu_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                }
                //schoolName
                top_menu_title.setText(schoolName);

                top_menu_rightTxt.setVisibility(View.GONE);
                if (fragment_home ==null){
                    fragment_home =new HomeFragment();
                    transaction.add(R.id.fragment_UI,fragment_home,"home");
                }else {
                    transaction.show(fragment_home);
                }
                break;
            case 1:
                getcheck(false,true);
                top_menu_title.setText("个人中心");
                top_menu_rightTxt.setText("修改密码");
                top_menu_rightTxt.setVisibility(View.VISIBLE);

                if (fragment_mine ==null){
                    fragment_mine =new MineFragment();
                    transaction.add(R.id.fragment_UI,fragment_mine,"mine");
                }else {
                    transaction.show(fragment_mine);
                }
                break;
        }
        //提交事务
        transaction.commit();
    }
    private void hideFragment(FragmentTransaction transaction) {
        if (fragment_home !=null){
            transaction.hide(fragment_home);
        }
        if (fragment_mine!=null){
            transaction.hide(fragment_mine);
        }
    }

    private void getcheck(boolean b, boolean b1) {
        home_iv.setSelected(b);
        home_tv.setSelected(b);
        mine_tv.setSelected(b1);
        mine_iv.setSelected(b1);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_home:
                setTabSelection(0);
                pagerPostion =0;
                break;
            case R.id.fragment_mine:
                setTabSelection(1);
                pagerPostion =1;
                break;
            case R.id.top_right_text: //修改密码
                if (top_menu_rightTxt.getText().toString().equals("修改密码")) {
                    startActivity(new Intent(getApplicationContext(),
                            ACT_MeAccountChangePassword.class));
                }
                break;
        }

    }


}
