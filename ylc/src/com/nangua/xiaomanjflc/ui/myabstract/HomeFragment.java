package com.nangua.xiaomanjflc.ui.myabstract;

import com.nangua.xiaomanjflc.UmengManager;
import com.umeng.analytics.MobclickAgent;

import android.support.v4.app.Fragment;

public abstract class HomeFragment extends Fragment{
	
	//该页是否已加载完成
	protected boolean initialed = false;
	
	public abstract void refreshData();

	public boolean isInitialed() {
		return initialed;
	}
	
	public void onResume() {
	    super.onResume();
	    if (UmengManager.analyticsStatus == UmengManager.AnalyticsOn) {
	    	String name = this.getClass().getSimpleName();
	    	MobclickAgent.onPageStart(name); //统计页面，"MainScreen"为页面名称，可自定义
	    }
	}
	public void onPause() {
	    super.onPause();
	    if (UmengManager.analyticsStatus == UmengManager.AnalyticsOn) {
	    	String name = this.getClass().getSimpleName();
	    	MobclickAgent.onPageEnd(name); 
	    }
	}

}
