package com.klcxkj.rs.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.klcxkj.rs.R;
import com.klcxkj.rs.bo.MessageBean;
import com.klcxkj.rs.widget.CircleImageView;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/8
 * email:yinjuan@klcxkj.com
 * description:消息列表
 */

public class LMessageListAdapter  extends MyAdapter<MessageBean>{
    /**
     * 构造方法描述:基类构造方法
     *
     * @param mContext
     */
    public LMessageListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view ==null){
            view =View.inflate(mContext, R.layout.list_view_item_message,null);
        }
        CircleImageView headIcon =ViewHolder.get(view,R.id.message_img);
        TextView title =ViewHolder.get(view,R.id.text_title);
        TextView time =ViewHolder.get(view,R.id.text_content);
        MessageBean message =getItem(position);
        //头像
        Log.d("LMessageListAdapter", message.getIconUrl());
        Glide.with(mContext)
                .load(message.getIconUrl())
                .crossFade()
                .dontAnimate()//第一次加载图片
                .error(R.mipmap.home_klcxkj)
                .into(headIcon);
        //文字
        title.setText(message.getRepTitle());
        String dataTime =message.getRepDatetime().substring(0,message.getRepDatetime().length()-4);
        time.setText(dataTime);
        return view;
    }
}
