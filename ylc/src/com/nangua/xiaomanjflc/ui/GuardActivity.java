package com.nangua.xiaomanjflc.ui;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;

//关于一理财页面
public class GuardActivity extends Activity {
	private WebView web;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_guard);
		
		UIHelper.setTitleView(this, "返回", "安全保障");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ApplicationUtil.isNeedGesture(this)) {
			startActivity(new Intent(this, GestureActivity.class));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppVariables.lastTime = new Date().getTime();
	}

}
