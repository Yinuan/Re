package com.klcxkj.rs;

import android.app.Application;
import android.util.Log;

import com.klcxkj.rs.download.config.SystemParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.smtt.sdk.QbSdk;

import cn.jpush.android.api.JPushInterface;


public class RSApplication extends Application{
//	public final static String BASE_URL ="http://103.20.249.247:3002/";// "http://211.149.224.58:6002/"; //http://103.20.249.247:3002/  //http://klcxkj.com.cn:3002/  //  
//	public final static String BASE_URL ="http://211.149.224.58:6002/";
//	http://120.76.155.138:6002
	public final static String BASE_URL ="http://120.76.155.138:10002/";
		//"http://www.china-bzkj.cn:3002/";
	
//	public final static String BASE_URL ="http://localhost:8888/RoadStuAppS/";
                                     //得到ImageOptions对象
	public static String RedPoint ="";
	@Override
	public void onCreate() {
		super.onCreate();
		//缓存
		AppPreference.getInstance().init(getApplicationContext());
		//图片加载
		ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(config);     //UniversalImageLoader初始化
		//极光推送
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
		initX5();

		//下载的缓存初始化
		SystemParams.init(this);
	}

	private void initX5() {
		QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

			@Override
			public void onViewInitFinished(boolean arg0) {
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				Log.d("app", " onViewInitFinished is " + arg0);
			}

			@Override
			public void onCoreInitFinished() {
			}
		};
		//x5内核初始化接口
		QbSdk.initX5Environment(getApplicationContext(),  cb);
	}
	
}
