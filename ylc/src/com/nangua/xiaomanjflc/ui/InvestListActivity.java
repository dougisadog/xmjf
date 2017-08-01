package com.nangua.xiaomanjflc.ui;

import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;
import android.view.View;
import android.widget.RelativeLayout;

public class InvestListActivity extends KJActivity {

	@BindView(id = R.id.invest, click = true)
	private RelativeLayout invest;
	@BindView(id = R.id.trans, click = true)
	private RelativeLayout trans;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_investlist);
		UIHelper.setTitleView(this, "", "我的投资");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.invest :
				showActivity(InvestListActivity.this, InvestActivity.class);
				break;
			case R.id.trans :
				showActivity(InvestListActivity.this, TransactionNewActivity.class);
				break;
		}
	}

}
