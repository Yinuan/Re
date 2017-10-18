package com.klcxkj.rs.util;

import android.content.Context;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DensityUtil {
	/** 
	     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	     */  
	    public static int dip2px(Context context, float dpValue) {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (dpValue * scale + 0.5f);  
	    }  
	  
	    /** 
	     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	     */  
	    public static int px2dip(Context context, float pxValue) {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (pxValue / scale + 0.5f);  
	    }

	/**
	 * 二维码加密
	 * @param datasource
	 * @param key
	 * @return
	 */
	public static byte[] encrypt(byte[] datasource, byte[] key) {
		try{
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key);
			//创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			//Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			//用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			//现在，获取数据并加密
			//正式执行加密操作
			return cipher.doFinal(datasource);
		}catch(Throwable e){
			e.printStackTrace();
		}
		return null;
	}



}
