package com.klcxkj.rs.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.klcxkj.rs.adapter.ViewHolder;
import com.klcxkj.rs.bean.RechargeRecording;

import java.util.List;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/7
 * email:yinjuan@klcxkj.com
 * description:
 */

public class RecordingAdpater extends RecyclerView.Adapter<ViewHolder>  {

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private Context mContext;
    private List<RechargeRecording> mDataList;
    private LayoutInflater mLayoutInflater;



    private LinearLayout linearLayout;


    public RecordingAdpater(Context mContext, List<RechargeRecording> mDataList, LinearLayout linearLayout) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.linearLayout = linearLayout;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return null;//
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RechargeRecording entity = mDataList.get(position);

    }



    @Override
    public int getItemCount() {
        Log.d("mDataList 的长度：", mDataList.size() + "");
//        if (mDataList.size() == 0)
//            linearLayout.setBackgroundResource(R.mipmap.null_record);
//        else
//            linearLayout.setBackgroundResource(0);
        return mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        if (position == 0){}
            return GROUP_ITEM;

     //   String currentDate = mDataList.get(position).getTime();
       // int prevIndex = position - 1;
   //     boolean isDifferent = !mDataList.get(prevIndex).getTime().equals(currentDate);
    //    return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
    }



   
}
