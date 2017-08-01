package com.nangua.xiaomanjflc.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.louding.frame.KJActivity;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.database.UserConfig;
import com.nangua.xiaomanjflc.bean.jsonbean.Account;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends KJActivity {

	@BindView(id = R.id.rl_idcard, click = true)
	private RelativeLayout rl_idcard;
	@BindView(id = R.id.idcard)
	private TextView mIdcard;
	@BindView(id = R.id.rl_bankcard, click = true)
	private RelativeLayout rl_bankcard;
	@BindView(id = R.id.bankcard)
	private TextView mBankcard;
	@BindView(id = R.id.handimg, click = true)
	private ImageView mHandimg;
	@BindView(id = R.id.hand)
	private TextView mHand;
	@BindView(id = R.id.rl_pwd, click = true)
	private RelativeLayout rl_pwd;
	@BindView(id = R.id.pwd)
	private TextView mPwd;
	@BindView(id = R.id.idNotValid)
	private TextView idNotValid;
	@BindView(id = R.id.bankNotValid)
	private TextView bankNotValid;

	@BindView(id = R.id.idcardimg)
	private ImageView mIdcardImg;
	@BindView(id = R.id.bankcardimg)
	private ImageView mBankcardImg;
	
	@BindView(id = R.id.signout, click = true)
	private TextView signout;
	@BindView(id = R.id.idcard_title)
	private TextView idcard_title;
	
	private String idCard;
	private String bankCard;
	private int idValidated;
	private int bankValidated;
	private int accountStatus;

	private boolean opened;

	private KJHttp kjh;
	private KJDB kjdb;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_setting_v2);
		UIHelper.setTitleView(this, "", "设置");
	}
	
	private void refreshHandView() {
		boolean needGesture = false;
		List<UserConfig> userConfigs = kjdb.findAllByWhere(UserConfig.class, "uid=" + AppVariables.uid);
		UserConfig userConfig = null;
		if (userConfigs.size() > 0) {
			userConfig = userConfigs.get(0);
		}
		if (null != userConfig) {
			needGesture = userConfig.isNeedGesture();
		}
		if (!needGesture) {
			mHand.setText("启用手势密码");
			mHandimg.setImageResource(R.drawable.gesture_close);
			opened = false;
		} else {
			mHand.setText("关闭手势密码");
			mHandimg.setImageResource(R.drawable.gesture_open);
			opened = true;
		}
	}

	@Override
	public void initData() {
		super.initData();
		kjh = new KJHttp();
		kjdb = KJDB.create(this);
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

		if (idValidated == 1 || !"".equals(a.getIdCard())) {
			idCard = a.getIdCard();
			bankValidated = a.getCardStatus();
			accountStatus = a.getAccountStatus();
			if (bankValidated == 2) {
				bankCard = a.getBankAccount();
			}
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
		idcard_title.setText("身份认证");
		if (idValidated == 1) {
			if (accountStatus == 0) {
				mIdcard.setText("审核中");
				mIdcardImg.setVisibility(View.GONE);
			} else {
				mIdcard.setText(idCard);
				idcard_title.setText("身份证号");
				mIdcardImg.setVisibility(View.GONE);
			}
			idNotValid.setVisibility(View.GONE);
		}
		if (bankValidated == 1) {
			mBankcard.setText("审核中");
			bankNotValid.setVisibility(View.GONE);
		} else if (bankValidated == 2) {
			mBankcard.setText(bankCard);
			bankNotValid.setVisibility(View.GONE);
//			mBankcardImg.setVisibility(View.GONE);
		}
	}

	@Override
	public void widgetClick(View v) {
		Intent intent;
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.rl_idcard:
			if (idValidated != 1) {
				showActivity(SettingActivity.this, IdcardActivity.class);
			}
			break;
		case R.id.rl_bankcard:
//			if (bankValidated == 0) {
				if (idValidated != 1) {
					LoudingDialogIOS ld = new LoudingDialogIOS(SettingActivity.this);
					ld.showConfirmHint("请先实名认证。");
				} else {
					getIPSData();
				}
//			}
			break;
		case R.id.handimg:
			if (opened) {
				intent = new Intent(SettingActivity.this,
						GestureCloseActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(SettingActivity.this, GestureActivity.class);
				intent.putExtra("isSet", true);
				startActivity(intent);
			}
			break;
		case R.id.rl_pwd:
			showActivity(SettingActivity.this, PasswordActivity.class);
			break;
		case R.id.signout:
			final LoudingDialogIOS ldsignout = new LoudingDialogIOS(SettingActivity.this);
			ldsignout.showOperateMessage("确定退出登录？");
			ldsignout.setPositiveButton("确定",
					R.drawable.dialog_positive_btn, new OnClickListener() {
						@Override
						public void onClick(View v) {
							AppVariables.clear();
							signout.setVisibility(View.GONE);
							AppVariables.isSignin = false;
							ldsignout.dismiss();
							setResult(AppConstants.FAILED);
							finish();
						}
					});
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
			refreshHandView();
			refresh();
		}
	}
	
	private void getIPSData() {
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		kjh.post(AppConstants.IPS_LOGIN, params, new HttpCallBack(SettingActivity.this, false) {
			
			@Override
			public void success(JSONObject ret) {
				Intent i = new Intent(SettingActivity.this ,BindActivity.class);
				try {
					if (ret.has("userName"))
						i.putExtra("userName", ret.getString("userName"));
					if (ret.has("url"))
						i.putExtra("url", ret.getString("url"));
					if (ret.has("merchantId")) 
						i.putExtra("merchantId", ret.getString("merchantId"));
					startActivity(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
