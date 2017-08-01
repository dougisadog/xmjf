package com.nangua.xiaomanjflc.ui;

import android.webkit.WebView;

import com.louding.frame.KJActivity;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;

public class SignupProtocolActivity extends KJActivity {
	private WebView web;

	@Override
	public void setRootView() {
		setContentView(R.layout.about);
		UIHelper.setTitleView(this, "注册", "注册协议");
		web = (WebView) findViewById(R.id.aboutylc);
		web.loadUrl("file:///android_asset/register.htm");
	}

}
