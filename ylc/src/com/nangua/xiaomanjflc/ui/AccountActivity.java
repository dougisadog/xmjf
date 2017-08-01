package com.nangua.xiaomanjflc.ui;

import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.jsonbean.Account;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.support.UIHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountActivity extends KJActivity {

	@BindView(id = R.id.rl_username, click = true)
	private RelativeLayout rl_username;
	@BindView(id = R.id.username)
	private TextView mUsername;
	@BindView(id = R.id.tel)
	private TextView mtel;
	@BindView(id = R.id.rl_tel)
	private RelativeLayout rl_tel;
	@BindView(id = R.id.imgGo)
	private ImageView imgGo;
	

	private String phone;
	private String realName;
	private int idValidated;
	private int nameValidated;
	private int accountStatus;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_account_v2);
		UIHelper.setTitleView(this, "我的账户", "个人信息");
	}
	
	@Override
	public void initData() {
		super.initData();
	}
	
	private void refresh() {
		if (AppVariables.forceUpdate || CacheBean.checkNeedUpdate()) {
			getInfo();
		}
		else {
			bindData();
			initView();
		}
	}

	/**
	 * 绑定私有变量
	 */
	private void bindData() {
		User u = CacheBean.getInstance().getUser();
		Account a = CacheBean.getInstance().getAccount();
		idValidated = u.getIdValidated();
		nameValidated = u.getNameValidated();
		phone = u.getPhone();

		if (idValidated == 1 || !"".equals(a.getIdCard())) {
			realName = a.getRealName();
			accountStatus = a.getAccountStatus();
		}
	}
	
	/**
	 * 请求个人信息
	 */
	private void getInfo() {
		InfoManager.getInstance().getInfo(this, new TaskCallBack() {

			@Override
			public void taskSuccess() {
				idValidated = CacheBean.getInstance().getUser().getIdValidated();
				bindData();
				initView();
			}

			@Override
			public void taskFail(String err, int type) {
			}

			@Override
			public void afterTask() {
			}
		});
	}

	/**
	 * 刷新UI
	 */
	private void initView() {
		mtel.setText(phone);
		if (idValidated == 1) {
			if (accountStatus == 0) {
				mUsername.setText("审核中");
			}
			else {
				mUsername.setText(TextUtils.isEmpty(realName) ? "未认证" : realName);
				imgGo.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.rl_username:
			if (idValidated != 1) {
				showActivity(AccountActivity.this, IdcardActivity.class);
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!AppVariables.isSignin) {
			setResult(AppConstants.FAILED);
			finish();
		}
		else {
			refresh();
		}
	}
	
}
