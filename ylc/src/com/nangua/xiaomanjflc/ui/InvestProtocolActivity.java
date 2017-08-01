package com.nangua.xiaomanjflc.ui;

import android.content.Intent;
import android.webkit.WebView;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;

public class InvestProtocolActivity extends KJActivity {

	@BindView(id = R.id.aboutylc)
	private WebView mContent;

	private KJHttp http;
	private HttpParams params;

	@Override
	public void setRootView() {
		setContentView(R.layout.about);
		UIHelper.setTitleView(this, "", "借款协议");
	}

	@Override
	public void initData() {
		super.initData();
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		http = new KJHttp();
		params = new HttpParams();
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.INVEST_PROTOCOL + id, params, new HttpCallBack(
				this) {
			@Override
			public void onSuccess(String t) {
				mContent.loadDataWithBaseURL(null, t, "text/html", "UTF-8",
						null);
			}
		});
	}

}
