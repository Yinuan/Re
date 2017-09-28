package com.klcxkj.rs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.klcxkj.rs.R;
import com.klcxkj.rs.bean.RechargeRecording;

import java.util.List;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/7
 * email:yinjuan@klcxkj.com
 * description:
 */

public class RrAdapter extends BaseAdapter {

    private Context mContext;
    private List<RechargeRecording> mDataList;
    private LayoutInflater mLayoutInflater;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view ==null){
            view =mLayoutInflater.inflate(R.layout.list_item_recharge_recording,null);
             holder =new Holder();
            holder.layout = (LinearLayout) view.findViewById(R.id.item_recording_top_layout);
            holder.title = (TextView) view.findViewById(R.id.item_recording_month);
            holder.content = (TextView) view.findViewById(R.id.item_recording_title);
            holder.yTime = (TextView) view.findViewById(R.id.item_recharge_time_mon);
            holder.value = (TextView) view.findViewById(R.id.item_recharge_value);
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }
        RechargeRecording recording = (RechargeRecording) getItem(position);
        holder.title.setText(recording.getFlagName());
        return view;
    }

    class  Holder{
        LinearLayout layout;
        TextView title;
        TextView content;
        TextView yTime;
        TextView value;
    }
}
