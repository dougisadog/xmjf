package com.nangua.xiaomanjflc.ui;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;

import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;

public class TenderProtocolActivity extends KJActivity {

	@BindView(id = R.id.aboutylc)
	private WebView mContent;

	private String products_type;// 产品类型01:融资产品,02:债权产品
	private String url;

	@Override
	public void setRootView() {
		setContentView(R.layout.about);
		UIHelper.setTitleView(this, "", "服务协议");
	}

	@Override
	public void initData() {
		super.initData();
		Intent i = getIntent();
		int pid = i.getIntExtra("pid", 0);
		products_type = i.getStringExtra("products_type");
		int uid = AppVariables.uid;
		int amt = i.getIntExtra("amt", 0);
		mContent.getSettings().setJavaScriptEnabled(true);
//		mContent.getSettings().setBuiltInZoomControls(true); // 设置显示缩放按钮  
//		mContent.getSettings().setSupportZoom(true); // 支持缩放  
		
		mContent.getSettings().setLoadWithOverviewMode(false);
		mContent.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mContent.getSettings().setUseWideViewPort(true);
		

		mContent.setWebViewClient(new WebViewClient());
		
		url = AppConstants.SERVICE_PROTOCOL + "?pid=" + pid + "&uid=" + uid
				+ "&amt=" + amt;
		mContent.loadUrl(url);
	}

}
