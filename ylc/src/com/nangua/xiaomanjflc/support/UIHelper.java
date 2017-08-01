package com.nangua.xiaomanjflc.support;

import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class UIHelper {

	/**
	 * 设置titleView的通用方法
	 *
	 * @param aty
	 *            activity对象
	 * @param textContent
	 *            中间TextView的文字
	 */
	public static void setTitleView(final Activity aty, String back,
			String textContent) {
		TextView btnLeft = null;
		TextView titleTv = null;
		ImageView titleImage = null;
		ImageView leftImage = null;
		FrameLayout flleft = (FrameLayout) aty.findViewById(R.id.flleft);
		btnLeft = (TextView) aty.findViewById(R.id.title_left);
		titleTv = (TextView) aty.findViewById(R.id.title_center);
		titleImage = (ImageView) aty.findViewById(R.id.title_image);
		leftImage = (ImageView) aty.findViewById(R.id.img_left);
		//暂时删除内容
//		btnLeft.setText(back);
		btnLeft.setText("");
		titleImage.setVisibility(View.GONE);
		btnLeft.setVisibility(View.VISIBLE);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(textContent);
		flleft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aty.finish();
			}
		});
	}
	
	public static void setTitleView(final Activity aty, String back, String rightText,
			String textContent, int drawable) {
		setTitleView(aty, back, rightText, textContent, drawable, null, null);
	}
	
	public static void setTitleView(final Activity aty, String back, String rightText,
			String textContent, int drawable, OnClickListener left, OnClickListener right) {
		TextView btnLeft = null;
		TextView btnRight = null;
		TextView titleTv = null;
		ImageView titleImage = null;
		ImageView rightImage = null;
		ImageView leftImage = null;
		btnLeft = (TextView) aty.findViewById(R.id.title_left);
		btnRight = (TextView) aty.findViewById(R.id.title_right);
		titleTv = (TextView) aty.findViewById(R.id.title_center);
		titleImage = (ImageView) aty.findViewById(R.id.title_image);
		rightImage = (ImageView) aty.findViewById(R.id.img_right);
		leftImage = (ImageView) aty.findViewById(R.id.img_left);
		if (drawable == 0) {
			titleImage.setVisibility(View.GONE);
		}
		else {
			titleImage.setImageResource(drawable);
		}
		if (!StringUtils.isEmpty(rightText)) {
			btnRight.setText(rightText);
			btnRight.setVisibility(View.VISIBLE);
			rightImage.setVisibility(View.INVISIBLE);
		}
		else {
			btnRight.setVisibility(View.INVISIBLE);
			rightImage.setVisibility(View.VISIBLE);
		}
		if (null == left) {
			left = new OnClickListener() {
				@Override
				public void onClick(View v) {
					aty.finish();
				}
			};
		}
		FrameLayout flleft = (FrameLayout) aty.findViewById(R.id.flleft);
		flleft.setOnClickListener(left);
		FrameLayout flright = (FrameLayout) aty.findViewById(R.id.flright);
		flright.setOnClickListener(right);
		
		if (!StringUtils.isEmpty(back)) {
			btnLeft.setText(back);
			btnLeft.setVisibility(View.VISIBLE);
			leftImage.setVisibility(View.INVISIBLE);
		}
		else {
			btnLeft.setVisibility(View.INVISIBLE);
			leftImage.setVisibility(View.VISIBLE);
		}
		if (!StringUtils.isEmpty(textContent)) {
			titleTv.setVisibility(View.VISIBLE);
			titleTv.setText(textContent);
		}
		else {
			titleTv.setVisibility(View.INVISIBLE);
		}
		
	}
	
	public static void setTitleView(final View v, String leftText, String rightText,
			String textContent, int drawable) {
		setTitleView(v, leftText, rightText, textContent, drawable, null, null);
	}
	
	public static void setTitleView(final View v, String leftText, String rightText,
			String textContent, int drawable, OnClickListener left, OnClickListener right) {
		TextView btnLeft = null;
		TextView btnRight = null;
		TextView titleTv = null;
		ImageView titleImage = null;
		btnLeft = (TextView) v.findViewById(R.id.title_left);
		btnRight = (TextView) v.findViewById(R.id.title_right);
		titleTv = (TextView) v.findViewById(R.id.title_center);
		titleImage = (ImageView) v.findViewById(R.id.title_image);
		FrameLayout flleft = (FrameLayout) v.findViewById(R.id.flleft);
		flleft.setOnClickListener(left);
		FrameLayout flright = (FrameLayout) v.findViewById(R.id.flright);
		flright.setOnClickListener(right);
		if (drawable == 0) {
			titleImage.setVisibility(View.GONE);
		}
		else {
			titleImage.setImageResource(drawable);
		}
		if (!StringUtils.isEmpty(rightText)) {
			btnRight.setText(rightText);
			btnRight.setVisibility(View.VISIBLE);
		}
		else {
			btnRight.setVisibility(View.INVISIBLE);
		}
		if (!StringUtils.isEmpty(leftText)) {
			btnLeft.setText(leftText);
			btnLeft.setVisibility(View.VISIBLE);
		}
		else {
			btnLeft.setVisibility(View.INVISIBLE);
		}
		if (!StringUtils.isEmpty(textContent)) {
			titleTv.setVisibility(View.VISIBLE);
			titleTv.setText(textContent);
		}
		else {
			titleTv.setVisibility(View.INVISIBLE);
		}
	}
	
	public static void setBtnRight(View v,int drawable, OnClickListener listener) {
		TextView btnRight = (TextView) v.findViewById(R.id.title_right);
		btnRight.setVisibility(View.INVISIBLE);
		ImageView imgRight = (ImageView) v.findViewById(R.id.img_right);
		if (drawable != 0) {
		imgRight.setImageResource(drawable);
		imgRight.setVisibility(View.VISIBLE);
		}
		FrameLayout flright = (FrameLayout) v.findViewById(R.id.flright);
		flright.setOnClickListener(listener);
		
	}
	
	public static void setBtnRight(Activity aty,int drawable, OnClickListener listener) {
		TextView btnRight = (TextView) aty.findViewById(R.id.title_right);
		btnRight.setVisibility(View.INVISIBLE);
		ImageView imgRight = (ImageView) aty.findViewById(R.id.img_right);
		if (drawable != 0) {
			imgRight.setImageResource(drawable);
			imgRight.setVisibility(View.VISIBLE);
		}
		FrameLayout flright = (FrameLayout) aty.findViewById(R.id.flright);
		flright.setOnClickListener(listener);
		
	}
	
	public static void setBtnLeft(View v,int drawable, OnClickListener listener) {
		TextView btnLeft = (TextView) v.findViewById(R.id.title_left);
		btnLeft.setVisibility(View.INVISIBLE);
		ImageView imgLeft = (ImageView) v.findViewById(R.id.img_left);
		if (drawable != 0) {
		imgLeft.setImageResource(drawable);
		imgLeft.setVisibility(View.VISIBLE);
		}
		FrameLayout flleft = (FrameLayout) v.findViewById(R.id.flleft);
		flleft.setOnClickListener(listener);
		
	}
	
	public static void setBtnLeft(Activity aty,int drawable, OnClickListener listener) {
		TextView btnLeft = (TextView) aty.findViewById(R.id.title_left);
		btnLeft.setVisibility(View.INVISIBLE);
		ImageView imgLeft = (ImageView) aty.findViewById(R.id.img_left);
		if (drawable != 0)
		imgLeft.setImageResource(drawable);
		imgLeft.setVisibility(View.VISIBLE);
		FrameLayout flleft = (FrameLayout) aty.findViewById(R.id.flleft);
		flleft.setOnClickListener(listener);
		
	}
	
	/**
	 * 创建现金券背景  ——————
	 * 			|	  /
	 * 			|	  \
	 * 			 ——————
	 * @param context
	 * @param w 宽
	 * @param h 高
	 * @param color 背景颜色
	 * @return Bitmap 图片资源
	 */
//	public static Bitmap makeRedBg(Context context, int w, int h, int color) {
//
//		Bitmap iconBmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
//		Canvas canvas = new Canvas(iconBmp);
//
//		Paint paint = new Paint();
//		paint.setAntiAlias(true);
//		paint.setColor(color);
//		Path path = new Path();
//		path.moveTo(0, 0);
//		path.lineTo(w, 0);
//		path.lineTo(w - h / 2 * tan(35), h / 2); //默认转折角35°
//		path.lineTo(w, h);
//		path.lineTo(0, h);
//		path.lineTo(0, 0);
//		path.close();
//		// 绘制五角形
//		canvas.drawPath(path, paint);
//		canvas.drawBitmap(iconBmp, 0, 0, paint);
//
//		return iconBmp;
//
//	}
	
	/**
	 * 现金劵背景 款式2
	 * @param context
	 * @param w
	 * @param h
	 * @param color
	 * @return
	 */
	public static Bitmap makeRedBg2(Context context, int w ,int h, int color){
		float c = 70f/160; //半圆中心位置
		float r = 5f/32 * h;
		Bitmap iconBmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(iconBmp);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
	    paint.setColor(color); 
	    
	    RectF rect = new RectF(0, 0, w, h);
	    canvas.drawRect(rect, paint);
	    
	    paint.setColor(Color.WHITE); 
	    RectF rectRoundRight = new RectF(w-r, h * c - r, w+r, h * c + r);   
        
	    canvas.drawArc(rectRoundRight, //弧线所使用的矩形区域大小   
	            90,  //开始角度   
	            270, //扫过的角度   
	            true, //是否使用中心   
	            paint);   
	    
	    RectF rectRoundLeft = new RectF(-r, h * c - r, r, h * c + r);  
	    canvas.drawArc(rectRoundLeft, //弧线所使用的矩形区域大小   
	            0,  //开始角度   
	            360, //扫过的角度   
	            true, //是否使用中心   
	            paint);   
	    
	    paint.setStrokeWidth((float) r/8); //线宽度暂定半径/8 
	    float lineWidth = (w - r * 4)/20; //10条线
	    for (int i = 0; i < 20; i=i + 2) {
			
	    	canvas.drawLine(r* 2 + lineWidth* i, h * c, r* 2 + lineWidth* (i+1), h * c, paint);
		}
        canvas.drawBitmap(iconBmp, 0, 0, paint); 
		
		return iconBmp;

}
	
	static float sin(int num){  
        return (float) Math.sin(num*Math.PI/180);  
    }  
  
    /** 
     * 与sin同理 
     */  
	static float cos(int num){  
        return (float) Math.cos(num*Math.PI/180);  
    }  
    
    static float tan(int num){  
        return (float) Math.tan(num*Math.PI/180);  
    } 
    
    public static int getStatusHeight(Context context) {
    	 
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    
}
