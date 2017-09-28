package com.klcxkj.rs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.klcxkj.rs.R;
import com.klcxkj.rs.bo.Ban;

/**
 * autor:OFFICE-ADMIN
 * time:2017/8/28
 * email:yinjuan@klcxkj.com
 * description:故障报修里面的适配器
 */

public class GRepairApater extends MyAdapter<Ban>{
    /**
     * 构造方法描述:基类构造方法
     *
     * @param mContext
     */
    public GRepairApater(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view==null){
            view =View.inflate(mContext, R.layout.item_repair,null);
        }
        Button btn =ViewHolder.get(view,R.id.repair_btn);
        String str =getItem(position).getBuildingID();
        btn.setText(getItem(position).getBuildingName());
        if (str.equals("0")){
            btn.setBackgroundResource(R.drawable.bg_hint_xizao);
            btn.setTextColor(mContext.getResources().getColor(R.color.txt_three));
        }else {
            btn.setBackgroundResource(R.drawable.btn_repair);
            btn.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        return view;
    }
}
