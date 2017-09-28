package com.klcxkj.rs.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.klcxkj.rs.ImageCacheInstance;
import com.klcxkj.rs.R;
import com.klcxkj.rs.bo.BaseBo;
import com.klcxkj.rs.network.CookieRequest;
import com.klcxkj.rs.network.PostRequest;
import com.klcxkj.rs.util.NetWorkUtil;
import com.klcxkj.rs.widget.LoadingDialogProgress;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * 抽象类，用于网络请求时用到的json和图片等
 */
public abstract class ACT_Network extends ACT_Base {

	protected RequestQueue mQueue;
	protected ImageLoader mImageLoader;
	protected UploadManager uploadManager;
    private LoadingDialogProgress progress;
	protected boolean aVersion =false; //安卓版本，默认小于4.4
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			aVersion =true;
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}else {
			aVersion=false;
			View tView =findViewById(R.id.top_menu_view);
			tView.setVisibility(View.GONE);
		}
		mQueue = Volley.newRequestQueue(getApplicationContext());// thread
																	// pool(4)
		mImageLoader = new ImageLoader(mQueue, ImageCacheInstance.getInstance()
				.getImageCache());

		Configuration config = new Configuration.Builder()
				.chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
				.putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
				.connectTimeout(6)           // 链接超时。默认10秒
				.useHttps(true)               // 是否使用https上传域名
				.responseTimeout(10)          // 服务器响应超时。默认60秒
				.recorder(null)           // recorder分片上传时，已上传片记录器。默认null
				.recorder(null, null)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
				.zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
				.build();

		// 重用uploadManager。一般地，只需要创建一个uploadManager对象
		 uploadManager = new UploadManager(config);
	}

	protected void showMenu(String str){
		TextView title = (TextView) findViewById(R.id.top_title);
		title.setText(str);
		LinearLayout backBtn = (LinearLayout) findViewById(R.id.top_btn_back);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
	protected void initDatas(String url) {
		if (!NetWorkUtil.isNetworkAvailable(ACT_Network.this)) {
			toast("当前网络不可用，请检查您的网络");
			if (progress !=null){
                progress.dismiss();
            }
			return;
		}
		CookieRequest jsonRequet = new CookieRequest(Method.GET, url, null,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject result) {
						try {
							int code = parseJson(result, null);
							if (code != 0) {
								toast(R.string.server_error);
								loadError(result);
							} else {
								loadDatas();
							}
						} catch (JSONException e) {
							Log.e(TAG, "initDatas JSON EXCEPTION", e);
							toast(R.string.json_error);
							loadError(null);
						}
					}

				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						loadError(null);
						if (error instanceof TimeoutError) {
							toast(R.string.timeout_error);
						} else {
							Log.d(TAG, "tttt onErrorResponse: " + error.toString());
							toast(R.string.server_error);
						}
					}
				});
		jsonRequet.setTag(TAG);
		mQueue.add(jsonRequet);
	}

	protected void sendPostRequest(final String url,
			HashMap<String, String> params) {
		if (!NetWorkUtil.isNetworkAvailable(ACT_Network.this)) {
			toast("当前网络不可用，请检查您的网络");
			return;
		}
		PostRequest request = new PostRequest(url, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				handleResponse(url, response);
			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handleErrorResponse(url, error);
			}
		}, params);
		request.setTag(TAG);
		mQueue.add(request);
	}

	protected void handleResponse(String url, JSONObject json) {
	}

	protected void handleErrorResponse(String url, VolleyError error) {
	}

	protected void sendGetRequest(final String url) {
		if (!NetWorkUtil.isNetworkAvailable(ACT_Network.this)) {
			toast("当前网络不可用，请检查您的网络");
			try {
				if (progress!=null){
                    progress.dismiss();
                }
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return;
		}
		Log.e("water", "sendGetRequest url = " + url);
		CookieRequest jsonRequet = new CookieRequest(Method.GET, url, null,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject result) {
						Log.d("ACT_Network", "result:" + result);
						try {
							Gson gson = new Gson();
							BaseBo resp = gson.fromJson(result.toString(),
									BaseBo.class);
							Log.d("ACT_Network", "resp.isSuccess():" + resp.isSuccess());
							if (!resp.isSuccess()) {
								if (!TextUtils.isEmpty(resp.getMsg())) {
									toast(resp.getMsg());
								}
								loadError(result);
								if (url.contains(ACT_Register.send_sms_url)){

								}
							} else {
								// toast(resp.getMsg());

								parseJson(result, url);
								if (!url.contains(ACT_Register.send_sms_url)) {
									loadDatas();
								}
							}
						} catch (Exception e) {
							e.fillInStackTrace();
							toast(R.string.json_error);
							loadError(null);
						}
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						loadError(null);
						if (error instanceof TimeoutError) {
							toast(R.string.timeout_error);
						} else {
							toast(R.string.server_error);
						}
					}
				});
		jsonRequet.setTag(TAG);
		mQueue.add(jsonRequet);
	}

	protected String upLoadImage(String filePath,String token,String fileName){
		final String[] pathName = {""};
		Log.d("ACT_Network", "filePath:=="+filePath);
		Log.d("ACT_Network", "token:=="+token);
		File file =new File(filePath);
		uploadManager.put(file, fileName, token, new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo info, JSONObject response) {
                Log.d("ACT_Network", "key:="+key+"info:=="+info+"response:=="+response);
				if (info.isOK()){
					Log.d("ACT_Network","url..=:"+ key);
					pathName[0] =key;
				}

			}
		},null);
		return pathName[0];
	}

	public void onStop() {
		super.onStop();
		mQueue.cancelAll(TAG);
	}

	protected abstract int parseJson(JSONObject result, String url)
			throws JSONException;

	protected abstract void loadDatas();

	protected abstract void loadError(JSONObject result);
}
