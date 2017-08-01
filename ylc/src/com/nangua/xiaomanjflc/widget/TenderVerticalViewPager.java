package com.nangua.xiaomanjflc.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * tenderactivity专用 解决 VerticalViewPager 与scrollview的冲突
 * @author Administrator
 *
 */
public class TenderVerticalViewPager extends VerticalViewPager {  
  
    public TenderVerticalViewPager(Context context) {  
        super(context);  
        init();  
    }  
  
    public TenderVerticalViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();  
    }  
  
  
    
    PointF downP = new PointF();
   	/** 触摸时当前的点 **/
   	PointF curP = new PointF();
   	
   	//获取当前点击事件的SmartScrollView
   	private static SmartScrollView actionDown;
   	
	public static SmartScrollView getActionDown() {
		return actionDown;
	}

	public static void setActionDown(SmartScrollView actionDown) {
		TenderVerticalViewPager.actionDown = actionDown;
	}
   	
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev){
    
        boolean intercepted = super.onInterceptTouchEvent((ev)); 
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        	downP.x = ev.getX();
        	downP.y = ev.getY();
        }
        else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
    		curP.x = ev.getX();
    		curP.y = ev.getY();
    		float distanceX = curP.x - downP.x;
    		float distanceY = curP.y - downP.y;
    		if (Math.abs(distanceX) < Math.abs(distanceY) && distanceY > 0) {
    			//当当前scrollview不可滑动 或处于顶端则终止事件传播 直接截获，并执行VerticalViewPager本身的onTouchEvent逻辑
    			if (null == actionDown || (!actionDown.canScroll() || actionDown.isScrolledToTop())) {
    				return true;
    			}
    		}
    	}
        return intercepted;  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent((ev));  
    }

    
}  