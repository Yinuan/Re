package com.klcxkj.rs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klcxkj.rs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:预约洗澡选择
 */
public class XizaoActivity extends Activity {

    private Button btn;
    private GridView gridView;
    LayoutInflater inflater;
    GridViewSim gridViewSim;
    private List<String> datas;
    private List<String> img_datas;
    private RelativeLayout relayout;
    private ImageView img;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xizao);
        initview();
        bindevent();
    }

    private void bindevent() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str ="0";
                for (int i = 0; i <img_datas.size() ; i++) {
                    if (img_datas.get(i).equals("1")){
                        str ="1";

                    }
                }
                if (str.equals("1")){
                    startActivity(new Intent(XizaoActivity.this,Xizao_CountActivity.class));
                }else {
                    Toast.makeText(XizaoActivity.this,"请选择浴室号",Toast.LENGTH_SHORT).show();
                }

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    if (img_datas.get(position).equals("0")){
                        for (int i = 0; i <img_datas.size() ; i++) {
                            if (img_datas.get(i).equals("1")){
                                img_datas.set(i,"0");
                            }
                        }
                       img_datas.set(position,"1");
                    }else {
                        img_datas.set(position,"0");
                    }
                    gridViewSim.notifyDataSetChanged();
            }
        });
        //弹出popwindow
        relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(R.mipmap.arrow_up);
                showpop();
            }
        });
    }



    private void initview() {
        tv = (TextView) findViewById(R.id.relayout_txt);
        img = (ImageView) findViewById(R.id.relayout_arrow);
        relayout = (RelativeLayout) findViewById(R.id.xizao_choose);
        btn = (Button) findViewById(R.id.xizao_btn_yuyue);
        gridView= (GridView) findViewById(R.id.grifview);
        inflater =LayoutInflater.from(this);
        datas =new ArrayList<>();
        img_datas =new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            datas.add("0"+i);
            img_datas.add("0");
        }
        gridViewSim =new GridViewSim(this,datas,img_datas);
        gridView.setAdapter(gridViewSim);
    }
    private void showpop() {
        View view =LayoutInflater.from(XizaoActivity.this).inflate(R.layout.item_pop,null);
        final PopupWindow popwindow =new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popwindow.setContentView(view);
        RelativeLayout line1 = (RelativeLayout) view.findViewById(R.id.line11);
        RelativeLayout line2 = (RelativeLayout) view.findViewById(R.id.line22);
        final TextView tv_1 = (TextView) view.findViewById(R.id.pop_tv_1);
        final TextView tv_2 = (TextView) view.findViewById(R.id.pop_tv_2);
        line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(R.mipmap.arrow_down);
                popwindow.dismiss();
                tv.setText(tv_1.getText().toString());
            }
        });
        line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.setImageResource(R.mipmap.arrow_down);
                popwindow.dismiss();
                tv.setText(tv_2.getText().toString());
            }
        });
      popwindow.showAsDropDown(relayout);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
       popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
           @Override
           public void onDismiss() {
               WindowManager.LayoutParams lp = getWindow().getAttributes();
               lp.alpha = 1f;
               getWindow().setAttributes(lp);
               img.setImageResource(R.mipmap.arrow_down);
           }
       });
    }

    class GridViewSim extends BaseAdapter {
        private Context context=null;
        private List<String> data;
        private List<String> img_data;


        private class Holder{

            ImageView item_img;
            TextView item_tex;

            public ImageView getItem_img() {
                return item_img;
            }

            public void setItem_img(ImageView item_img) {
                this.item_img = item_img;
            }

            public TextView getItem_tex() {
                return item_tex;
            }

            public void setItem_tex(TextView item_tex) {
                this.item_tex = item_tex;
            }




        }
        //构造方法
        public GridViewSim(Context context, List<String> data,List<String> data_img) {
            this.context = context;
            this.data = data;
            this.img_data =data_img;
        }


        @Override
        public int getCount() {


            return data.size();

        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            Holder holder;
            if(view==null){
                view=inflater.inflate(R.layout.item_xizao,null);
                holder=new Holder();
                holder.item_img=(ImageView)view.findViewById(R.id.item_img);
                holder.item_tex=(TextView)view.findViewById(R.id.item_tv);
                view.setTag(holder);
            }else{
                holder=(Holder) view.getTag();
            }
            holder.item_tex.setText(data.get(position));
            if (img_data.get(position).equals("0")){
                //默认
                holder.item_img.setImageResource(R.mipmap.shower_usable_icon);
            }else {
                holder.item_img.setImageResource(R.mipmap.shower_choice_icon);
            }

            return view;
        }
    }


}
