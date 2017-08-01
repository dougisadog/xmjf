package com.nangua.xiaomanjflc.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.louding.frame.KJActivity;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.ui.fragment.TransactionFragment;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.nangua.xiaomanjflc.widget.TitleTab;
import com.nangua.xiaomanjflc.widget.TitleTab.ItemCallBack;

public class TransactionNewActivity extends KJActivity implements
		OnClickListener {

	private TitleTab titleTab;
	
	@Override
	public void setRootView() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		UIHelper.setTitleView(this, "", "交易记录");
		
		titleTab = (TitleTab) findViewById(R.id.mytab);
		final List<String> names = new ArrayList<String>();
		names.add("充值记录");
		names.add("提现记录");
		names.add("投资流水");
		names.add("回款流水");
		names.add("返现流水");
		titleTab.setDatas(names, new ItemCallBack() {

			@Override
			public void onItemClicked(int position) {

				for (int i = 0; i < titleTab.getChildCount(); i++) {
					TextView tv = titleTab.getTextView(i);
					if (null != tv)
						tv.setTextColor(getResources().getColor(position == i ? R.color.orange : R.color.grey));
				}
				if (titleTab.getCurrentPosition() != position) {
					TransactionFragment newContent = new TransactionFragment((position + 1) + "");
					switchContent(newContent);
				}
			}
		});
		
		titleTab.clickItem(0);

	}

	/**
	 * 切换Fragment，也是切换视图的内容
	 */
	public void switchContent(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}



}
