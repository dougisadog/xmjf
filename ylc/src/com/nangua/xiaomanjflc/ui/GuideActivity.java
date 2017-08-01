package com.nangua.xiaomanjflc.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.nangua.xiaomanjflc.R;

/**
 * 引导页
 */
@SuppressLint("InflateParams")
public class GuideActivity extends Activity {

	private ViewPager mViewPager;
	private ArrayList<View> mViews;
	private PopupWindow mPopupWindow;
	private View mPopupView;
	private MyHandler mMyHandler;
	private LinearLayout mIndicatorLayout; // 用来装小圆点的Linearlayout
	private ArrayList<ImageView> mIndicatorList;// 装小圆点的集合

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 9:
				// 这里必须用handler来延迟启动不然会报异常
				mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
				mPopupWindow.showAtLocation(findViewById(R.id.txt_Activity),
						Gravity.CENTER, 0, 0);
				break;
			default:
				break;
			}
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏

		setContentView(R.layout.guide_laylout);

		mMyHandler = new MyHandler();
		mIndicatorList = new ArrayList<ImageView>();

		iniView();
		iniViewPager();// 初始化ViewPager

		new Thread(new Runnable() {
			public void run() {
				mMyHandler.sendEmptyMessageDelayed(9, 200);
			}
		}).start();
	}

	private void iniView() {
		mPopupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
		mViewPager = (ViewPager) mPopupView.findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(5);
		// 包裹小圆点的LinearLayout
		mIndicatorLayout = (LinearLayout) mPopupView
				.findViewById(R.id.indicatorLayout);

		mPopupWindow = new PopupWindow(mPopupView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void iniViewPager() {
		View v1 = getLayoutInflater().inflate(R.layout.layout_1, null);
		View v2 = getLayoutInflater().inflate(R.layout.layout_2, null);
		View v3 = getLayoutInflater().inflate(R.layout.layout_3, null);

		ImageView img1 = (ImageView) v1.findViewById(R.id.img1);
		ImageView img2 = (ImageView) v2.findViewById(R.id.img2);
		ImageView img3 = (ImageView) v3.findViewById(R.id.img3);

		// 设置图片透明度
		img1.setAlpha(180);
		img2.setAlpha(180);
		img3.setAlpha(180);

		// 点击第一个跳过
		Button btn_skip1 = (Button) v1.findViewById(R.id.btn_skip);
		btn_skip1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mainIntent = null;
				mainIntent = new Intent(GuideActivity.this, MainActivity.class);
				mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mainIntent);
				finish();
			}
		});
		// 点击第二个跳过
		Button btn_skip2 = (Button) v2.findViewById(R.id.btn_skip);
		btn_skip2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mainIntent = null;
				mainIntent = new Intent(GuideActivity.this, MainActivity.class);
				mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mainIntent);
				finish();
			}
		});
		// 点击第三个跳过
		Button btn_skip3 = (Button) v3.findViewById(R.id.btn_skip);
		btn_skip3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mainIntent = null;
				mainIntent = new Intent(GuideActivity.this, MainActivity.class);
				mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mainIntent);
				finish();
			}
		});
		// 点击最后一张图片的立即体验后退出
		Button start = (Button) v3.findViewById(R.id.txt_start);
		start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mainIntent = null;
				mainIntent = new Intent(GuideActivity.this, MainActivity.class);
				mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mainIntent);
				finish();
			}
		});

		mViews = new ArrayList<View>();
		mViews.add(v1);
		mViews.add(v2);
		mViews.add(v3);

		// 设置适配器
		mViewPager.setAdapter(new MyPagerAdapter());
		// 设置监听事件
		mViewPager.setOnPageChangeListener(new MyPagerChangeListener());

		// 加入小圆点
		for (int i = 0; i < mViews.size(); i++) {
			ImageView indicator = new ImageView(this);
			if (i == 0) {
				indicator.setImageResource(R.drawable.point01);
			} else {
				indicator.setImageResource(R.drawable.point02);
			}

			indicator.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			mIndicatorLayout.addView(indicator);
			// 把小圆点图片存入集合,好让切换图案片的时候动态改变小圆点背景
			mIndicatorList.add(indicator);
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mViews.get(arg1));
		}

		public void finishUpdate(View arg0) {

		}

		public int getCount() {
			return mViews.size();
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mViews.get(arg1));
			return mViews.get(arg1);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			for (int i = 0; i < mViews.size(); i++) {

			}
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {

		}
	}

	private class MyPagerChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int position) {
			for (int i = 0; i < mViews.size(); i++) {
				mIndicatorList.get(i).setImageResource(R.drawable.point02);
			}
			mIndicatorList.get(position).setImageResource(R.drawable.point01);
		}
	}
}
