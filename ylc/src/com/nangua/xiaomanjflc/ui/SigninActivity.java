package com.nangua.xiaomanjflc.ui;

import com.louding.frame.KJActivity;
import com.louding.frame.KJDB;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.utils.KeyboardUitls;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SigninActivity extends KJActivity {

	@BindView(id = R.id.tel)
	private EditText mTel;
	@BindView(id = R.id.pwd)
	private EditText mPwd;
	@BindView(id = R.id.verification)
	private EditText mVrify;
	@BindView(id = R.id.verifyimage, click = true)
	private ImageView mVrifyImage;
	@BindView(id = R.id.signin, click = true)
	private TextView mSignin;
	@BindView(id = R.id.title_right, click = true)
	private TextView mSignup;
	@BindView(id = R.id.verify1)
	private LinearLayout mVrify1;
	@BindView(id = R.id.verify2)
	private LinearLayout mVrify2;
	@BindView(id = R.id.hint)
	private TextView mHint;
	@BindView(id = R.id.losepwd, click = true)
	private TextView mLose;

	private String tel;
	private String pwd;
	private String code;
	private KJDB kjdb;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_signin_v2);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		UIHelper.setTitleView(this, "取消", "注册", "登录", 0);
		TextView btnLeft = (TextView) findViewById(R.id.title_left);
		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(AppConstants.FAILED);
				finish();
			}
		});
		ImageView imgLeft = (ImageView) findViewById(R.id.img_left);
		imgLeft.setVisibility(View.INVISIBLE);
	}

	@Override
	public void initData() {
		super.initData();
		kjdb = KJDB.create(this);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.signin:
			tel = mTel.getText().toString();
			pwd = mPwd.getText().toString();
			code = mVrify.getText().toString();
			if (StringUtils.isEmpty(tel) || StringUtils.isEmpty(pwd)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(R.string.sign_hint);
			} else {
				InfoManager.getInstance().loginNormal(SigninActivity.this, tel, pwd, new TaskCallBack() {
					
					@Override
					public void taskSuccess() {
						setResult(AppConstants.SUCCESS);
						SigninActivity.this.finish();
					}
					
					@Override
					public void taskFail(String err, int type) {
						mHint.setVisibility(View.GONE);
					}
					
					@Override
					public void afterTask() {
						// TODO Auto-generated method stub
						
					}
				});
			}
			KeyboardUitls.hideKeyboard(SigninActivity.this);
			break;
		case R.id.losepwd:
			showActivity(SigninActivity.this, FindPwdOneActivity.class);
			break;
		case R.id.title_right:
			Intent intent = new Intent(SigninActivity.this,
					SignupActivity.class);
			startActivityForResult(intent, 10002);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case AppConstants.SUCCESS:
			setResult(AppConstants.SUCCESS);
			finish();
			break;
		case AppConstants.FAILED:
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	@Override
	public void onBackPressed() {
		setResult(AppConstants.FAILED);
		finish();
	}

}
