package com.klcxkj.rs.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * autor:OFFICE-ADMIN
 * time:2017/10/17
 * email:yinjuan@klcxkj.com
 * description:自定义的EDITETXT
 */

public class MEditText extends android.support.v7.widget.AppCompatEditText{
    public MEditText(Context context) {
        super(context);
    }

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart ==selEnd){
            setSelection(getText().length());
        }
    }
}
