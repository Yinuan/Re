package com.klcxkj.rs.network;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.klcxkj.rs.R;
import com.klcxkj.rs.bo.BaseBo;

/**
 * 网络操作类的请求
 * @author goldong
 *
 */
public class OperRequest{
	private Context mContext;
	private RequestQueue mQueue;
	
	public OperRequest(Context context, RequestQueue queue){
		this.mContext = context;
		this.mQueue = queue;
	}
	
	public void send(final String url){
		send(url, null, null);
	}
	
	public void send(final String url, final SuccCallBack succCallback, final FailCallBack failCallback){
		CookieRequest jsonRequet = new CookieRequest(Method.GET, url,
                null, new Listener<JSONObject>() {
                    public void onResponse(JSONObject result) {
                        try {
                        	Gson gson = new Gson();
                        	BaseBo bo = gson.fromJson(result.toString(), BaseBo.class);			                        	
                            if (!bo.isSuccess()) {
                            	if(failCallback !=null){
                            		failCallback.afterFail();
                            	}else{
                            		Toast.makeText(mContext, mContext.getString(R.string.operate_error)+":"+bo.getMsg(), Toast.LENGTH_LONG).show();			                                
                            	}
                            }else{
                            	if(succCallback != null){
                            		succCallback.afterSucc();
                            	}else{                            		
                            		Toast.makeText(mContext, "操作成功", Toast.LENGTH_LONG).show();
                            	}
                            }
                        } catch (Exception e) {
                        	if(failCallback !=null){
                        		failCallback.afterFail();
                        	}else{
                        		Toast.makeText(mContext, mContext.getString(R.string.operate_error)+":"+e.getStackTrace(), Toast.LENGTH_LONG).show();			                                
                        	}
                        }
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    	Log.d("OperRequest", "onErrorResponse: "+error.toString()+" url:"+url);
                    	if(failCallback !=null){
                    		failCallback.afterFail();
                    	}else{
                    		Toast.makeText(mContext, "网络错误: "+error.getMessage(), Toast.LENGTH_LONG).show();			                                
                    	}
                    }
                });
        jsonRequet.setTag("OperRequest");
        mQueue.add(jsonRequet);
	}
	
	public static abstract class SuccCallBack{
		public abstract void afterSucc();
	}
	
	public static abstract class FailCallBack{
		public abstract void afterFail();
	}
}