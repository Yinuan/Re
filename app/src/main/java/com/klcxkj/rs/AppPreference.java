package com.klcxkj.rs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.klcxkj.rs.bean.UserInfo;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.bo.UserBo;
import com.klcxkj.rs.util.SimpleCrypto;
import com.klcxkj.rs.util.StringUtils;

public class AppPreference {
    public static final String COUPON_CFG_XML = "klcxkj_config.xml";
    
    private static final String SEED = "klcxkj";
    
    private static final String USER_ID = "user_id";
    private static final String IS_FIRST_TIME_OPEN = "IS_FIRST_TIME_OPEN";
    
    private SharedPreferences mCfgPreference;
    
    
    private static AppPreference mInstance;

    private AppPreference() {
    }

    public void init(Context ctx) {
        if (mCfgPreference == null) {
            mCfgPreference = ctx.getSharedPreferences(COUPON_CFG_XML, Context.MODE_PRIVATE);
        }
    }

    public static AppPreference getInstance() {
        if (mInstance == null) {
            mInstance = new AppPreference();
        }
        return mInstance;
    }
    
    public void clear() {
    	SharedPreferences.Editor editor = mCfgPreference.edit();
    	editor.clear();
    }

    public boolean save(String key, String value){
    	SharedPreferences.Editor editor = mCfgPreference.edit();
    	editor.putString(key, value);
    	return editor.commit();
    }

    public boolean save(String key, boolean value){
    	SharedPreferences.Editor editor = mCfgPreference.edit();
    	editor.putBoolean(key, value);
    	return editor.commit();
    }
    
    public String get(String key, String defaultValue){
    	return mCfgPreference.getString(key, defaultValue);
    }
    
    public boolean get(String key, boolean defaultValue){
    	return mCfgPreference.getBoolean(key, defaultValue);
    }
    
    public boolean isFirstTimeOpen(){
    	return mCfgPreference.getBoolean(IS_FIRST_TIME_OPEN, true);
    }
    
    public void setNotFirstTimeOpen(){
    	save(IS_FIRST_TIME_OPEN, false);
    }


    /**
     * 保存密码
     * @param passWord
     */
    public void savePassWord(String passWord){
        if (mCfgPreference != null) {
            SharedPreferences.Editor localEditor = mCfgPreference.edit();
            try {
                localEditor.putString("passWord", SimpleCrypto.encrypt(SEED, passWord));
            } catch (Exception e) {
                e.printStackTrace();
            }
            localEditor.commit();
        }
    }

    /**
     * 获取密码
     * @return
     */
    public String getPassWord(){
        if (mCfgPreference != null) {
            String passWord = mCfgPreference.getString("passWord", "");
            if(!StringUtils.isEmpty(passWord))
                try {
                    String json = SimpleCrypto.decrypt(SEED, passWord);
                    return json;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 2017/8/31
     * 保存用户信息
     * @param userInfo
     */
    public void saveLoginUser(UserInfo userInfo) {
        if (mCfgPreference != null) {
            SharedPreferences.Editor localEditor = mCfgPreference.edit();
            try {
                Gson gson = new Gson();
                localEditor.putString("userinfo_new", SimpleCrypto.encrypt(SEED, gson.toJson(userInfo)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            localEditor.commit();
        }
    }

    /**
     * 2017/8/31
     * 获取用户信息
     * @return
     */
    public UserInfo getUserInfo() {
        if (mCfgPreference != null) {
            String encyptPhone = mCfgPreference.getString("userinfo_new", "");
            if(!StringUtils.isEmpty(encyptPhone))
                try {
                    String json = SimpleCrypto.decrypt(SEED, encyptPhone);
                    Gson gson = new Gson();
                    return gson.fromJson(json, UserInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     * 删除用户信息
     * 2017/9/8
     */
    public void deleteLoginUser() {
        if (mCfgPreference != null) {
            SharedPreferences.Editor localEditor = mCfgPreference.edit();
            localEditor.remove("userinfo_new");
            localEditor.commit();
        }
    }

    /**
     * 保存卡片信息
     * 2017/9/8
     * @param cardJson
     */
    public void saveCardInfo(String cardJson) {
        if (mCfgPreference != null) {
            SharedPreferences.Editor localEditor = mCfgPreference.edit();
            try {
				localEditor.putString("CardInfo", SimpleCrypto.encrypt(SEED, cardJson));
			} catch (Exception e) {
				e.printStackTrace();
			}
            localEditor.commit();
        }
    }


    /**
     * 保存卡片信息
     * 2017/9/8
     * @param cardJson
     */
    public void saveCardInfos(CardInfo cardJson) {
        if (mCfgPreference != null) {
            SharedPreferences.Editor localEditor = mCfgPreference.edit();
            try {
                Gson gson =new Gson();
                localEditor.putString("CardInfo", SimpleCrypto.encrypt(SEED, gson.toJson(cardJson)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            localEditor.commit();
        }
    }



    /**
     * 获取卡片信息
     * 2017/9/8
     * @return
     */
    public CardInfo getCardInfo() {
        if (mCfgPreference != null) {
        	String encyptPhone = mCfgPreference.getString("CardInfo", "");
        	if(!StringUtils.isEmpty(encyptPhone))
				try {
					String json = SimpleCrypto.decrypt(SEED, encyptPhone);
					Gson gson = new Gson();
					return gson.fromJson(json, CardInfo.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
        }
        return null;
    }

    /**
     * 删除卡片信息
     * 2017/9/8
     */
    public void deleteCardInfo() {
   	 if (mCfgPreference != null) {
   		 SharedPreferences.Editor localEditor = mCfgPreference.edit();
   		 localEditor.remove("CardInfo");
   		 localEditor.commit();
        }
   }

    /**
     * 废弃的获取用户信息
     * 2017/9/8
     * @return
     */
    public UserBo getLoginUser() {
        if (mCfgPreference != null) {
        	String encyptPhone = mCfgPreference.getString("userinfo", "");
        	if(!StringUtils.isEmpty(encyptPhone))
				try {
					String json = SimpleCrypto.decrypt(SEED, encyptPhone);
					Gson gson = new Gson();
					return gson.fromJson(json, UserBo.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
        }
        return null;
    }
    

    
}
