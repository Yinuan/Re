package com.klcxkj.rs.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.bo.UserBo;

public class CookieRequest extends JsonObjectRequest {
	private final static String COOKIE_TICKET_KEY =  "ticket|" ;
	private Map<String,String> mHeaders=new HashMap<String,String>(1);

    public CookieRequest(String url, JSONObject jsonRequest, Listener listener,
                                   ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    
    /*@Override
	public RetryPolicy getRetryPolicy() {
    	RetryPolicy retryPolicy = new DefaultRetryPolicy(5000,1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    	return retryPolicy;
	}*/


	public CookieRequest(int method, String url, JSONObject jsonRequest, Listener listener,
                                   ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }
    
    public void setCookie(String cookie){
        mHeaders.put("Cookie", COOKIE_TICKET_KEY + cookie);
       
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
    	UserBo loginUser = AppPreference.getInstance().getLoginUser();
    	if(loginUser != null){
    		mHeaders.put("Cookie", COOKIE_TICKET_KEY +loginUser.getEmployeeID());
    		mHeaders.put("ServerIP",loginUser.getServerIP());
    		mHeaders.put("ServerPort",loginUser.getServerPort());
    	}
        return mHeaders;
    }

}
