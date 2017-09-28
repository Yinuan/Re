package com.klcxkj.rs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.klcxkj.rs.AppPreference;
import com.klcxkj.rs.R;
import com.zhy.guidepagerlib.GuideContoler;
import com.zhy.guidepagerlib.GuideContoler.ShapeType;

import java.util.ArrayList;
import java.util.List;
/**
 * author : yinjuan
 * time： 2017/6/9 13:54
 * email：yin.juan2016@outlook.com
 * Description:引导页
 */
public class ACT_GuidePage extends ACT_Base {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_guidepage);
		initViewPager();
		AppPreference.getInstance().setNotFirstTimeOpen();
	}
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//        	Intent intent = new Intent();
//			intent.putExtra("return", true);
//			setResult(RESULT_OK, intent);
//			ACT_GuidePage.this.finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
	/**使用写好的库初始化引导页面**/
	private void initViewPager() {
		GuideContoler contoler = new GuideContoler(this);
		contoler.setmShapeType(ShapeType.OVAL);//设置指示器的形状为矩形，默认是圆形
//		int[] imgIds = {R.drawable.guide_1,R.drawable.guide_2};
//		contoler.init(imgIds, view);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view1 = inflater.inflate(R.layout.first_guideview, null);
		View view2 = inflater.inflate(R.layout.second_guideview, null);//
		View view3 = inflater.inflate(R.layout.third_fuideview,null);
		List<View> list = new ArrayList<View>();
		list.add(view1);
		list.add(view2);
		//list.add(view3);
		contoler.init(list);
	}

	public void onClick_register(View view){
		Intent intent = new Intent(this, ACT_Register.class);
		startActivityForResult(intent, 1);
		finish();
	}

	public void onClick_login(View view){
		Intent intent = new Intent(this, ACT_Login.class);
		startActivityForResult(intent, 2);
		finish();
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		if (resultCode != this.RESULT_OK || data == null) {
//			return;
//		}
//		boolean isShowBotton = data.getBooleanExtra("return", false);
//		if (isShowBotton) {
//			Intent intent = new Intent();
//			intent.putExtra("return", true);
//			setResult(RESULT_OK, intent);
//			finish();
//		}
//	}
}
