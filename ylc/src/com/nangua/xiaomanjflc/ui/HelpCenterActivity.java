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

public class HelpCenterActivity extends Activity {
	private WebView web;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.helpcenter);
		UIHelper.setTitleView(this, "返回", "帮助中心");
		web = (WebView) findViewById(R.id.helpcenter);
		WebSettings ws = web.getSettings();
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		// 设置支持缩放
		ws.setSupportZoom(true);
		ws.setUseWideViewPort(true);
		ws.setJavaScriptEnabled(true);
		ws.setLoadWithOverviewMode(true);
		web.loadUrl(AppConstants.FAQ);

		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
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
