package com.klcxkj.rs.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.klcxkj.rs.R;
import com.klcxkj.rs.util.DensityUtil;

public class SideBar2 extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public final static String[] letter = {"!", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private String[] b = {"!"};
	private int choose = -1;// 选中
	private Paint paint = new Paint();

	private TextView mTextDialog;
	private Context mContext;
	private Bitmap mbitmap = BitmapFactory.decodeResource(getResources(),
			R.mipmap.login_index_icon);;
	
	private int type = 1;
	/**
	 * 为SideBar设置显示字母的TextView
	 * @param mTextDialog
	 */
	
	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public void setB(String[] b) {
		this.b = b;
	}

	public void setType(int type) {
		this.type = type;
	}


	public SideBar2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public SideBar2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public SideBar2(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (b == null) {
			b = letter;
		}
		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.list_letter));
		paint.setTextSize(DensityUtil.dip2px(mContext, 14));
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setStyle(Style.FILL);		
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		float widthCenter = getMeasuredWidth() / 2;
		
		float BitmapCenter = (getMeasuredWidth() - mbitmap.getWidth()) / 2;
		
		if (b.length > 0) {
			float height = (getMeasuredHeight())/ 26;
			for (int i = 0; i < b.length; i++) {
				if (i == 0 && type != 2) {
					canvas.drawBitmap(mbitmap, BitmapCenter, (i + 1)
							* height - height / 2, paint);
				} else
					canvas.drawText(String.valueOf(b[i]), widthCenter,
							(i + 1) * height, paint);
			}
		}
		paint.reset();
		
		// 获取焦点改变背景颜色.
//		int julishangbian = DensityUtil.dip2px(mContext, 25);
//		int height = getHeight();// 获取对应高度
//		int width = getWidth(); // 获取对应宽度
//		int singleHeight = (height -  mbitmap.getHeight() - julishangbian)/ letter.length;// 获取每一个字母的高度
//
//		for (int i = 0; i < b.length; i++) {
//			paint.setColor(getResources().getColor(R.color.list_letter));
//			paint.setTypeface(Typeface.DEFAULT_BOLD);
//			paint.setAntiAlias(true);
//			paint.setTextSize(DensityUtil.dip2px(mContext, 14));
//			// 选中的状态
//			if (i == choose) {
//				paint.setColor(Color.parseColor("#3399ff"));
//				paint.setFakeBoldText(true);
//			}
//			if (i == 0 && type != 2) {
////				canvas.drawBitmap(mbitmap, widthCenter - 7, (i + 1)
////						* height - height / 2, paint);
//				float xPos = width / 2 - mbitmap.getWidth() / 2;
//				float yPos = singleHeight * i + julishangbian;
//				canvas.drawBitmap(mbitmap, xPos, yPos, paint);
//				paint.reset();// 重置画笔
//			} else {
//			// x坐标等于中间-字符串宽度的一半.
//				float xPos = width / 2 - paint.measureText(b[i]) / 2;
//				float yPos = singleHeight * i + singleHeight + julishangbian;
//				canvas.drawText(b[i], xPos, yPos, paint);
//				paint.reset();// 重置画笔
//			}
//		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight()* 26);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundColor(getResources().getColor(R.color.letter_bg));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
//			setBackgroundResource(R.drawable.sidebar_background);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					
					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}