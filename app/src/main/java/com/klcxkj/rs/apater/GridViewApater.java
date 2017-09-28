package com.klcxkj.rs.apater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * autor:OFFICE-ADMIN
 * time:2017/7/6
 * email:jacob.kailu@outlook.com
 * description:
 */

public class GridViewApater extends BaseAdapter {

    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;



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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){

        }
        return null;
    }

    class ViewHold{
        ImageView iv;
        TextView tv;
    }
}
