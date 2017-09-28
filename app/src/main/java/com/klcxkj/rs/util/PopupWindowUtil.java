package com.klcxkj.rs.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.klcxkj.rs.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PopupWindowUtil {
	private static PopupWindow mPopupWindow;
	
	public static void showPopupWindow1(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater)       
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
		final View vPopupWindow=inflater.inflate(R.layout.popupwindow, null, false);    
		final PopupWindow pw= new PopupWindow(vPopupWindow,300,300,true);    
		pw.showAtLocation(parent, Gravity.CENTER, 0, 0);  
	}
	public static void showPopupWindow(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater)       
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
		final View vPopupWindow=inflater.inflate(R.layout.popupwindow, null, false);    
		final PopupWindow pw= new PopupWindow(vPopupWindow,LinearLayout.LayoutParams.FILL_PARENT,
			    LinearLayout.LayoutParams.WRAP_CONTENT);  
		pw.setBackgroundDrawable(new BitmapDrawable());   
		pw.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		pw.setOutsideTouchable(true);
		pw.showAsDropDown(parent);
		pw.update(); 
	}
	
	public static void showPopupWindowForRefresh(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater)       
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
		final View vPopupWindow=inflater.inflate(R.layout.popupwindow_refresh, null, false);    
		mPopupWindow = new PopupWindow(vPopupWindow,LinearLayout.LayoutParams.FILL_PARENT,
			    LinearLayout.LayoutParams.WRAP_CONTENT);  
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());   
		mPopupWindow.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.showAsDropDown(parent);
		mPopupWindow.update(); 
	}
	
	public static void showPopupWindowForBill(Context context, View parent, int resID) {
		LayoutInflater inflater = (LayoutInflater)       
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
		final View vPopupWindow=inflater.inflate(resID, null, false); 
		final PopupWindow pw= new PopupWindow(vPopupWindow,LinearLayout.LayoutParams.FILL_PARENT,
			    LinearLayout.LayoutParams.WRAP_CONTENT);  
		pw.setBackgroundDrawable(new BitmapDrawable());   
		pw.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		pw.setOutsideTouchable(true);
		pw.showAsDropDown(parent);
		pw.update(); 
	}
	
	public static void showPopupWindowForBillTypeTime(Context context, View parent) {
		LayoutInflater inflater = (LayoutInflater)       
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
		final View vPopupWindow=inflater.inflate(R.layout.popupwindow_type_time, null, false); 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		String timeFormat = formatter.format(curDate);
		String[] time = timeFormat.split("-");
		TextView text1 = (TextView)vPopupWindow.findViewById(R.id.textView1);
		text1.setText(getDataString(time));
		
		TextView text2 = (TextView)vPopupWindow.findViewById(R.id.textView2);
		time = getTime(time);
		text2.setText(getDataString(time));
		
		TextView text3 = (TextView)vPopupWindow.findViewById(R.id.textView3);
		time = getTime(time);
		text3.setText(getDataString(time));
		
		TextView text4 = (TextView)vPopupWindow.findViewById(R.id.textView4);
		time = getTime(time);
		text4.setText(getDataString(time));
		
		final PopupWindow pw= new PopupWindow(vPopupWindow,LinearLayout.LayoutParams.FILL_PARENT,
			    LinearLayout.LayoutParams.WRAP_CONTENT);  
		pw.setBackgroundDrawable(new BitmapDrawable());   
		pw.setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		pw.setOutsideTouchable(true);
		pw.showAsDropDown(parent);
		pw.update(); 
	}
	
	
	public static String[] getTime(String[] time) {
		if (Integer.parseInt(time[1]) == 1) {
			time[0] = (Integer.parseInt(time[0]) - 1) +"";
			time[1] = 12 + "";
			return time;
		} else {
			time[1] = (Integer.parseInt(time[1]) - 1) +"";
			return time;
		}
	}
	
	public static String getDataString(String[] time) {
		return new StringBuilder().append(time[0]).append("年")
				.append(time[1]).append("月").toString();
	}
	public static void dimiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}
}
