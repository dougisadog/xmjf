package com.nangua.xiaomanjflc.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConfig;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.receiver.AppReceiver;
import com.nangua.xiaomanjflc.ui.fragment.AccountFragment;
import com.nangua.xiaomanjflc.ui.fragment.MainHomeFragment;
import com.nangua.xiaomanjflc.ui.fragment.ProductFragment;
import com.nangua.xiaomanjflc.ui.myabstract.HomeFragment;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.UmengManager;
import com.nangua.xiaomanjflc.YilicaiApplication;
import com.nangua.xiaomanjflc.bean.database.UserConfig;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.file.FileUtils;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;
	private TabWidget mTabWidget;
	private TextView productTab;
	private TextView accountTab;
	private TextView MainHomeTab;

	private KJHttp kjh;
	private AppReceiver receiver;
	// 再按一次退出程序
	private long firstTime = 0;
	// 缓存上次所属page
	private int cacheCurrentPage = 0;

	public static final int REQUEST_CODE = 10001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();

		YilicaiApplication.getInstance().setActivity(this);
		YilicaiApplication.getInstance().addStackActivity(this);

		setContentView(R.layout.activity_main_v2);

		receiver = new AppReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SCREEN_OFF");
		filter.addAction("android.intent.action.SCREEN_ON");
		registerReceiver(receiver, filter);
		String sid = AppConfig.getAppConfig(this).get(AppConfig.SID);
		if (null != sid) {
			AppVariables.sid = sid;
		}
		if (!StringUtils.isEmpty(AppVariables.sid)) {
			isSignin();
		}
		else {
			AppVariables.isSignin = false;
		}
		mTabWidget = (TabWidget) findViewById(R.id.tabwidget);
		MainHomeTab = (FontTextView) getTvTab(R.string.home_v2,
				R.drawable.tab_main_home);
		mTabWidget.addView(MainHomeTab);
		MainHomeTab.setOnClickListener(mTabClickListener);

		productTab = (FontTextView) getTvTab(R.string.product_v2,
				R.drawable.tab_product_selecter);
		mTabWidget.addView(productTab);
		/*
		 * Listener必须在mTabWidget.addView()之后再加入，用于覆盖默认的Listener，
		 * mTabWidget.addView()中默认的Listener没有NullPointer检测。
		 */
		productTab.setOnClickListener(mTabClickListener);

		accountTab = (FontTextView) getTvTab(R.string.account_v2,
				R.drawable.tab_account_selecter);
		mTabWidget.addView(accountTab);
		accountTab.setOnClickListener(mTabClickListener);

	
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		// 初始化
		List<HomeFragment> fragments = new ArrayList<HomeFragment>();
		fragments.add(new MainHomeFragment());
		fragments.add(new ProductFragment());
		fragments.add(new AccountFragment());

		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
				fragments);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(mPageChangeListener);

		mTabWidget.setCurrentTab(0);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(2);
		
	}

	/**
	 * 判断登录状态
	 */
	private void isSignin() {
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		kjh.post(AppConstants.ISSIGNIN, params,
				new HttpCallBack(MainActivity.this) {
					@Override
					public void onSuccess(String t) {
						try {
							JSONObject ret = new JSONObject(t);
							JSONObject o = ret.getJSONObject("body");
							AppVariables.isSignin = o.getBoolean("isLogin");
							if (AppVariables.isSignin) {
								AppVariables.uid = o.getInt("uid");
								AppVariables.needGesture = true;
								if (o.has("new_hand"))
									AppVariables.newHand = o.getInt("new_hand");
								KJDB kjdb = KJDB.create(MainActivity.this);
								List<UserConfig> userConfigs = kjdb
										.findAllByWhere(UserConfig.class,
												"uid=" + AppVariables.uid);
								UserConfig userConfig = null;
								if (userConfigs.size() > 0) {
									userConfig = userConfigs.get(0);
									userConfig.setLastGestureCheckTime(
											new Date().getTime());
									kjdb.update(userConfig);
								}
								else {
									// 正常情况不会数据库没有数据
									userConfig = new UserConfig();
									userConfig.setUid(AppVariables.uid);
									userConfig.setNeedGesture(false);
									userConfig.setLastGestureCheckTime(
											new Date().getTime());
									kjdb.save(userConfig);
								}
								AppVariables.tel = userConfig.getTel();
								notifyViewPagerDataSetChanged();
								// 验证登录 也需要更新资产信息（因为时间检测不起作用
								// 在getName里已经修改了最近的登录时间）
								HomeFragment current = mPagerAdapter.getItem(2);
								if (current.isInitialed()) {
									AccountFragment accountFrag = (AccountFragment) current;
									accountFrag.refreshData(
											AppVariables.forceUpdate);
								}

								CacheBean.syncCookie(MainActivity.this);
								if (ApplicationUtil
										.isNeedGesture(MainActivity.this)) {
									startActivity(new Intent(MainActivity.this,
											GestureActivity.class));
								}
							}
						}
						catch (JSONException e) {
							this.onFinish();
							Toast.makeText(MainActivity.this, "数据解析错误",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					}
				});
	}




	private OnClickListener mTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == productTab) {
				mViewPager.setCurrentItem(1);
			}
			else if (v == accountTab) {
				if (!AppVariables.isSignin) {
					Intent intent = new Intent(MainActivity.this,
							SigninActivity.class);
					startActivityForResult(intent, REQUEST_CODE);
				}
				else {
					mViewPager.setCurrentItem(2);
				}
			}
			else if (v == MainHomeTab) {
				mViewPager.setCurrentItem(0);
			}
		}
	};

	/**
	 * 翻页逻辑
	 */
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int current) {
			// 当进入home页时 判断登录
			if (current == 2) {
				if (!AppVariables.isSignin) {
					startActivityForResult(
							new Intent(MainActivity.this, SigninActivity.class),
							REQUEST_CODE);
					return;
				}
				else {
					//改为不刷新商品列表之刷新我的信息
					mPagerAdapter.getItem(current).refreshData();
				}
			}
			else if (current == 0) {
				mPagerAdapter.getItem(current).refreshData();
			}
			mTabWidget.setCurrentTab(current);
			cacheCurrentPage = current;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private TextView getTvTab(int txtId, int resId) {
		FontTextView tv = new FontTextView(this);
		tv.setFocusable(true);
		tv.setText(getString(txtId));
		tv.setTextColor(
				getResources().getColorStateList(R.drawable.tab_font_selecter));
		tv.setTextSize(14);
		Drawable icon = getResources().getDrawable(resId);
		icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
		tv.setCompoundDrawables(null, icon, null, null); // 设置上图标
		tv.setCompoundDrawablePadding(5);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		private List<HomeFragment> fragments;
		public MyPagerAdapter(FragmentManager fm,
				List<HomeFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public HomeFragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}
	private void notifyViewPagerDataSetChanged() {

		mPagerAdapter.notifyDataSetChanged();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (UmengManager.analyticsStatus == UmengManager.AnalyticsOn)
	        MobclickAgent.onResume(this);
		YilicaiApplication.getInstance().setCurrentRunningActivity(this);
		if (ApplicationUtil.isNeedGesture(this)) {
			startActivity(new Intent(this, GestureActivity.class));
		}
		int currentItem = mViewPager.getCurrentItem();
		HomeFragment current = mPagerAdapter.getItem(currentItem);
		if (!current.isInitialed())
			return;
		if (currentItem == 2) {
			AccountFragment accountFrag = (AccountFragment) current;
			accountFrag.refreshData(AppVariables.forceUpdate);
		}
		else if (currentItem == 0) {
			current.refreshData();
		}
		//暂时取消商品列表的刷新 方式返回后位置错误
//		else {
//			mPagerAdapter.getItem(currentItem).refreshData();
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (UmengManager.analyticsStatus == UmengManager.AnalyticsOn)
	        MobclickAgent.onPause(this);
		if (YilicaiApplication.getInstance().getCurrentRunningActivity()
				.equals(this)) {
			YilicaiApplication.getInstance().setCurrentRunningActivity(null);
		}
		AppVariables.lastTime = new Date().getTime();
	}

	// 再按一次退出程序
	@Override
	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			firstTime = secondTime;// 更新firstTime
		}
		else { // 两次按键小于2秒时，退出应用
			MobclickAgent.onKillProcess(this);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		switch (resultCode) {
			case AppConstants.SUCCESS :
				// 如果当前属于 account页 则 setCurrentItem 不会触发 OnPageChangeListener的监听
				// 需要手动更新内容
				if (mViewPager.getCurrentItem() == 2) {
					mTabWidget.setCurrentTab(2);
					cacheCurrentPage = 2;
				}
				else {
					mViewPager.setCurrentItem(2);
				}
				break;
			case AppConstants.FAILED :
				if (mViewPager.getCurrentItem() == 2) {
					if (!AppVariables.isSignin) {
						// 当因为登录状态弹出登录窗时，返回的缓存fragment应为产品productFragment 即第0页
						mViewPager.setCurrentItem(0);
					}
				}
				else {
					mViewPager.setCurrentItem(cacheCurrentPage);
				}
				break;
			case RedActivity.TAB :
				mViewPager.setCurrentItem(1);
				break;
			default :
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
