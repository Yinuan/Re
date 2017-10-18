package com.klcxkj.boss.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klcxkj.boss.activity.Boss_Consume;
import com.klcxkj.boss.activity.Boss_ProjectDetail;
import com.klcxkj.rs.R;
import com.klcxkj.rs.adapter.MyAdapter;
import com.klcxkj.rs.adapter.ViewHolder;
import com.klcxkj.rs.widget.CircleImageView;

/**
 * autor:OFFICE-ADMIN
 * time:2017/10/10
 * email:yinjuan@klcxkj.com
 * description:项目管理适配器
 */

public class LvProjectManageApater extends MyAdapter<String>{
    /**
     * 构造方法描述:基类构造方法
     *
     * @param mContext
     */
    public LvProjectManageApater(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
      if (view ==null){
          view =View.inflate(mContext, R.layout.b_weihu_pro_item,null);
      }
         String str =getItem(position);
        CircleImageView icon = ViewHolder.get(view,R.id.weihu_project_manage_img); //头像
        TextView projectName =ViewHolder.get(view,R.id.weihu_project_name); //工程名称
        TextView phone =ViewHolder.get(view,R.id.weihu_project_phone); //电话
        TextView perName =ViewHolder.get(view,R.id.weihu_project_man); //联系人
        TextView time =ViewHolder.get(view,R.id.weihu_project_time); //时间
        TextView watchNum =ViewHolder.get(view,R.id.weihu_project_water_watch); //水表编号数量
        LinearLayout listLayout =ViewHolder.get(view,R.id.weihu_project_list); //
        RelativeLayout highConsume =ViewHolder.get(view,R.id.weihu_project_consume_h); //最高消费
        RelativeLayout lowConsume =ViewHolder.get(view,R.id.weihu_project_consume_l); //最低消费
        projectName.setText(str);
        //点击事件
        highConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =new Intent(mContext, Boss_Consume.class);
                intent.putExtra("consme","high");
                mContext.startActivity(intent);
            }
        });
        lowConsume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext, Boss_Consume.class);
                intent.putExtra("consme","low");
                mContext.startActivity(intent);
            }
        });
        //项目详情
        listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext, Boss_ProjectDetail.class);
                intent.putExtra("prohectName","重庆大学");
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
