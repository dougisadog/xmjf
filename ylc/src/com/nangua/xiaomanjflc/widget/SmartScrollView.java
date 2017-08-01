package com.nangua.xiaomanjflc.widget;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 监听ScrollView滚动到顶部或者底部做相关事件拦截
 */
public class SmartScrollView extends ScrollView {

	public SmartScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private boolean isScrolledToTop = true; // 初始化的时候设置一下值
	private boolean isScrolledToBottom = false;
	public SmartScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private ISmartScrollChangedListener mSmartScrollChangedListener;

	/** 定义监听接口 */
	public interface ISmartScrollChangedListener {
		void onScrolledToBottom();
		void onScrolledToTop();
	}

	public void setScanScrollChangedListener(
			ISmartScrollChangedListener smartScrollChangedListener) {
		mSmartScrollChangedListener = smartScrollChangedListener;
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (scrollY == 0) {
			isScrolledToTop = clampedY;
			isScrolledToBottom = false;
		}
		else {
			isScrolledToTop = false;
			isScrolledToBottom = clampedY;
		}
		notifyScrollChangedListeners();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (android.os.Build.VERSION.SDK_INT < 9) { // API
													// 9及之后走onOverScrolled方法监听
			if (getScrollY() == 0) { // getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确
				isScrolledToTop = true;
				isScrolledToBottom = false;
			}
			else if (getScrollY() + getHeight() - getPaddingTop()
					- getPaddingBottom() == getChildAt(0).getHeight()) {
				isScrolledToBottom = true;
				isScrolledToTop = false;
			}
			else {
				isScrolledToTop = false;
				isScrolledToBottom = false;
			}
			notifyScrollChangedListeners();
		}
		
	}

	private void notifyScrollChangedListeners() {
		if (isScrolledToTop) {
			if (mSmartScrollChangedListener != null) {
				mSmartScrollChangedListener.onScrolledToTop();
			}
		}
		else if (isScrolledToBottom) {
			if (mSmartScrollChangedListener != null) {
				mSmartScrollChangedListener.onScrolledToBottom();
			}
		}
	}

	public boolean isScrolledToTop() {
		return isScrolledToTop;
	}

	public boolean isScrolledToBottom() {
		return isScrolledToBottom;
	}

	public boolean canScroll() {
		View child = getChildAt(0);
		if (child != null) {
			int childHeight = child.getHeight();
			return getHeight() < childHeight;
		}
		return false;
	}

	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		TenderVerticalViewPager.setActionDown(this);
		if (!canScroll()) {
			return false;
		}
		return super.onTouchEvent(ev);
	}
	
	



}