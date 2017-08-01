package com.nangua.xiaomanjflc.ui;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;

public class IPSActivity extends Activity {

	private WebView webView;

	private String operationType;
	private String merchantID;
	private String sign;
	private String request;
	private String uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ips);
		UIHelper.setTitleView(this, "返回", "小满金服");
		Intent intent = getIntent();
		operationType = intent.getStringExtra("operationType");
		merchantID = intent.getStringExtra("merchantID");
		request = intent.getStringExtra("request");
		sign = intent.getStringExtra("sign");
		uri = intent.getStringExtra("uri");
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);

		webView.setWebViewClient(new MyClient());

		webView.loadUrl("file:///android_asset/IpsWebView.html");
	}

	final class MyClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			webView.loadUrl("javascript:connectIps('" + operationType + "','" + merchantID + "','" +sign + "','" +request + "','" + uri + "')");
		}
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
