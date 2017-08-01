package com.nangua.xiaomanjflc.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.widget.TextView;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.widget.FontTextView;

/**
 * 消息中心详细内容
 * */
public class MessageCenterDetailActivity extends KJActivity {

	private HttpParams params;
	private KJHttp http;

	private TextView tv_message_title;
	private TextView tv_message_time;
	private TextView tv_message_content;

	private String message_title;
	private String message_time;
	private String message_content;
	private String id;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_message_center_detail);
		UIHelper.setTitleView(this, "", "消息详情");

		Intent intent = getIntent();
		message_title = intent.getStringExtra("message_title");
		message_time = intent.getStringExtra("message_time");
		message_content = intent.getStringExtra("message_content");
		id = intent.getStringExtra("id");

	}

	@Override
	public void initData() {
		super.initData();

		params = new HttpParams();
		http = new KJHttp();

		tv_message_title = (FontTextView) findViewById(R.id.tv_message_title);
		tv_message_time = (FontTextView) findViewById(R.id.tv_message_time);
		tv_message_content = (FontTextView) findViewById(R.id.tv_message_content);

		tv_message_title.setText(message_title);
		tv_message_time.setText(message_time);
		tv_message_content.setText(message_content);

		getData();
	}

	private void getData() {
		params.put("id", id);
		http.post(AppConstants.MESSAGE_CENTER_ALREAD, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			MessageCenterDetailActivity.this) {

		@Override
		public void failure(JSONObject ret) {
			super.failure(ret);
		}

		@Override
		public void success(JSONObject ret) {
			super.success(ret);
			try {
				ret.getBoolean("boo");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onFinish() {
			super.onFinish();
		}
	};

}
