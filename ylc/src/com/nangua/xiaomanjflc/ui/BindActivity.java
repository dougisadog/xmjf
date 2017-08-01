package com.nangua.xiaomanjflc.ui;

import java.util.Date;

import com.louding.frame.KJActivity;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//关于一理财页面
public class BindActivity extends KJActivity {
	private WebView webView;
	
	private String userName;
	private String requestUrl;
	private String merchantId;
	
	private ValueCallback<Uri> mFilePathCallback4;
	private ValueCallback<Uri[]> mFilePathCallback5;
	
	private static final int REQUEST_FILE_PICKER = 11000;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_bind);
		UIHelper.setTitleView(this, "返回", "环迅管理");
		
		
		userName = getIntent().getStringExtra("userName");
		requestUrl = getIntent().getStringExtra("url");
		merchantId = getIntent().getStringExtra("merchantId");
		
		
		webView = (WebView) findViewById(R.id.bindWeb);
		WebSettings ws = webView.getSettings();
		
		webView.setWebChromeClient(new WebChromeClient()
		{
		    public void openFileChooser(ValueCallback<Uri> filePathCallback)
		    {
		        mFilePathCallback4 = filePathCallback;
		        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        intent.addCategory(Intent.CATEGORY_OPENABLE);
		        intent.setType("*/*");
		        startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
		    }

			public void openFileChooser(ValueCallback filePathCallback, String acceptType)
		    {
		        mFilePathCallback4 = filePathCallback;
		        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        intent.addCategory(Intent.CATEGORY_OPENABLE);
		        intent.setType("*/*");
		        startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
		    }

		    public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture)
		    {
		        mFilePathCallback4 = filePathCallback;
		        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        intent.addCategory(Intent.CATEGORY_OPENABLE);
		        intent.setType("*/*");
		        startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
		    }

		    @Override
		    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
		    {
		        mFilePathCallback5 = filePathCallback;
		        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        intent.addCategory(Intent.CATEGORY_OPENABLE);
		        intent.setType("*/*");
		        startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
		        return true;
		    }
		});

		
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		// 设置支持缩放
		ws.setSupportZoom(true);
		ws.setUseWideViewPort(true);
		ws.setAllowFileAccess(true);// 设置允许访问文件数据
		ws.setJavaScriptEnabled(true);
		ws.setLoadWithOverviewMode(true);
		webView.setWebViewClient(new MyClient());
		webView.loadUrl("file:///android_asset/IpsWebView.html");
	}

	final class MyClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			webView.loadUrl("javascript:connectIps('" + userName + "','" + merchantId + "','" + requestUrl + "')");
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		AppVariables.lastTime = new Date().getTime();
	}

	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {
		AppVariables.forceUpdate = true;
		super.onDestroy();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
	    if(requestCode==REQUEST_FILE_PICKER)
	    {
	        if(mFilePathCallback4!=null)
	        {
	            Uri result = intent==null || resultCode!=Activity.RESULT_OK ? null : intent.getData();
	            if(result!=null)
	            {
	                mFilePathCallback4.onReceiveValue(result);
	            }
	            else
	            {
	                mFilePathCallback4.onReceiveValue(null);
	            }
	        }
	        if(mFilePathCallback5!=null)
	        {
	            Uri result = intent==null || resultCode!=Activity.RESULT_OK ? null : intent.getData();
	            if(result!=null)
	            {
	                mFilePathCallback5.onReceiveValue(new Uri[]{ result });
	            }
	            else
	            {
	                mFilePathCallback5.onReceiveValue(null);
	            }
	        }

	        mFilePathCallback4 = null;
	        mFilePathCallback5 = null;
	    }
	}
	
}
