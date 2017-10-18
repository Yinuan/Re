package com.klcxkj.boss.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.klcxkj.boss.fragment.Consume_Month;
import com.klcxkj.boss.fragment.Consume_More;
import com.klcxkj.boss.fragment.Cosume_Day;
import com.klcxkj.rs.R;

public class Boss_Consume extends AppCompatActivity {

    private View tView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss__consume);
        // 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的 //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else {
            tView =findViewById(R.id.top_menu_view);
            tView.setVisibility(View.GONE);
        }
        initview();
    }


    private void initview() {
        Intent intent =getIntent();
        String name =intent.getStringExtra("consme");
        TextView title = (TextView) findViewById(R.id.top_title);
        LinearLayout backBtn = (LinearLayout) findViewById(R.id.top_btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (name.equals("high")){
            title.setText("最高消费");
        }else if (name.equals("low")){
            title.setText("最低消费");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_boss);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_boss);


        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Cosume_Day();
                case 1:
                    return new Consume_Month();
                case 2:
                default:
                    return new Consume_More();

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "天";
                case 1:
                    return "月";
                case 2:
                default:
                    return "更多";
            }
        }
    }


}
