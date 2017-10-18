package com.klcxkj.rs.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.klcxkj.rs.bo.CardInfo;
import com.klcxkj.rs.util.DigitalTrans;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ACT_TCode extends ACT_Network {

    private ImageView tcode;
    private CardInfo cardInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__tcode);
        showMenu("自助领卡");
        tcode = (ImageView) findViewById(R.id.icon_tcode);
        cardInfo = AppPreference.getInstance().getCardInfo();
        String pass =AppPreference.getInstance().getPassWord();
        String cardId =cardInfo.getCardID()+"";
        StringBuffer sb =new StringBuffer();

        switch (cardId.length()){
            case 0:
                toast("卡片信息不存在");
                break;
            case 1:
                sb.append("0000000");
                break;
            case 2:
                sb.append("000000");
                break;
            case 3:
                sb.append("00000");
                break;
            case 4:
                sb.append("0000");
                break;
            case 5:
                sb.append("000");
                break;
            case 6:
                sb.append("00");
                break;
            case 7:
                sb.append("0");
                break;
            case 8:
                break;

        }
        sb.append(cardId);
        StringBuffer sb2 =new StringBuffer();
        sb2.append("NO:"+sb+",KEY:"+pass+"00");
        Log.d("ACT_TCode", "sb2:" + sb2);
        //
        byte[] key =new byte[8];
        key[0] =0x30;
        key[1] =0x31;
        key[2] =0x32;
        key[3] =0x33;
        key[4] =0x34;
        key[5] =0x35;
        key[6] =0x36;
        key[7] =0x37;
        //数据转字节数组
        byte[] bytes =new byte[24];
        bytes =sb2.toString().getBytes();
        //字节数组转16进制字符串
        String hexString =DigitalTrans.byte2hex(bytes);
        Log.d("ACT_TCode", "16:进制"+hexString.toString());
        //16进制字符串转16进制字节数组
        byte[] items =DigitalTrans.hex2byte(hexString);
        Log.d("ACT_TCode", "items:" + items);
       // byte[] items =hexString.getBytes();
        for (int i = 0; i <items.length ; i++) {
            Log.d("ACT_TCode", "items[i]:" + items[i]);
        }
      byte[] b=  DigitalTrans.encrypt(items,key);
        for (int i = 0; i <b.length ; i++) {
            Log.d("ACT_TCode", "b[i]:" + b[i]);
        }
        String code =DigitalTrans.bytesToHexString(b).substring(0,24);
        Log.d("ACT_TCode", "加密后的数据："+DigitalTrans.bytesToHexString(b));
      //  byte[] items = DigitalTrans.hexStringToBytes(sb2.toString());
        //byte[] bytes =DigitalTrans.byte2hex(items);
        //BitmapFactory.decodeResource(getResources(), R.mipmap.cantact_us)
        Bitmap bitmap = CodeUtils.createImage(code,220,220, null);
        tcode.setImageBitmap(bitmap);

    }

    @Override
    protected int parseJson(JSONObject result, String url) throws JSONException {
        return 0;
    }

    @Override
    protected void loadDatas() {

    }

    @Override
    protected void loadError(JSONObject result) {

    }
}
