package com.nangua.xiaomanjflc.ui;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.widget.FontTextView;

public class AnnoContentActivity extends KJActivity {

	@BindView(id = R.id.title)
	private TextView mTitle;
	@BindView(id = R.id.data)
	private TextView mData;
	@BindView(id = R.id.content)
	private WebView mContent;

	private String title;
	private String data;
	private String content;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_anno_content);
		UIHelper.setTitleView(this, "公告列表", "平台公告");
	}

	@Override
	public void initData() {
		super.initData();
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		data = intent.getStringExtra("data");
		content = intent.getStringExtra("content");
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mTitle.setText(title);
		mData.setText(data);
		String html = "<html><style> p a { display:block;overflow:hidden;text-overflow:ellipsis; white-space:nowrap;}</style><head>"
				+ "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"
				+
				// "<meta name='viewport' content='width=640, initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0, user-scalable=yes'>"
				// +
				"<title>平台公告</title>"
				+ "</head><body>"
				+ content
				+ "<script type='text/javascript'>var list=document.getElementsByTagName('img');for (var i = 0; i < list.length; i++) {list[i].width=300;}</script>"
				+ "</body></html>";
//		System.out.println(html);
		WebSettings ws = mContent.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setSupportZoom(false);
		ws.setLoadWithOverviewMode(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mContent.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
	}

}
