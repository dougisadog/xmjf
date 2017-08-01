package com.nangua.xiaomanjflc.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.UIHelper;

public class PasswordActivity extends KJActivity {

	@BindView(id = R.id.pwd_old, click = true)
	private EditText mPwd_old;
	@BindView(id = R.id.pwd_new, click = true)
	private EditText mPwd_new;
	@BindView(id = R.id.pwd_confirm, click = true)
	private EditText mPwd_confirm;
	@BindView(id = R.id.confirm, click = true)
	private TextView mConfirm;
	@BindView(id = R.id.hint)
	private TextView mHint;

	private String pwd_old;
	private String pwd_new;
	private String pwd_confirm;

	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_password_v2);
		UIHelper.setTitleView(this, "账户中心", "修改密码");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.confirm:
			comfirm();
			break;
		}
	}
	
	private void requestChangePassword() {
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("originPassword", pwd_old);
		params.put("newPassword", pwd_new);
		params.put("confirmNewPassword", pwd_confirm);
		kjh.post(AppConstants.CHANGEPWD, params, new HttpCallBack(
				PasswordActivity.this) {
			
			
			@Override
			public void failure(JSONObject ret) {
//					super.failure(ret);
				if (!ret.isNull("msg")) {
					try {
						String msg = ret.getString("msg");
							if (msg.equals("not login")) {
								ApplicationUtil.restartApplication(PasswordActivity.this);
							} else {
								if ("".equals(msg))
									msg = "密码修改失败";
								LoudingDialogIOS ldc = new LoudingDialogIOS(PasswordActivity.this);
								ldc.showConfirmHint(msg);
								mHint.setVisibility(View.GONE);
							}
					} catch (JSONException e) {
						Toast.makeText(PasswordActivity.this, R.string.app_data_error,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(PasswordActivity.this, R.string.app_exception, Toast.LENGTH_SHORT)
							.show();
				}
			}


			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				LoudingDialogIOS ld = new LoudingDialogIOS(PasswordActivity.this);
				ld.showConfirmHintAndFinish("设置成功。");
				//修改成功后强制登出
				clearinfo();
				
			}
		});
	}

	private void comfirm() {
		pwd_old = mPwd_old.getText().toString();
		pwd_new = mPwd_new.getText().toString();
		pwd_confirm = mPwd_confirm.getText().toString();
		mHint.setVisibility(View.VISIBLE);
		if (StringUtils.isEmpty(pwd_old) || StringUtils.isEmpty(pwd_new)) {
			mHint.setText("请输入完整信息。");
		}
		else if (!StringUtils.isPasswordStrength(pwd_new)) {
			mHint.setText(getResources().getString(R.string.sign_up_pwd_hint));
		}
		else if (!pwd_new.equals(pwd_confirm)) {
			mHint.setText(getResources().getString(R.string.different_pwd));
		}
		else if (pwd_new.trim().equals(pwd_old.trim())) {
			mHint.setText("新旧密码不能相同");
		}
		else {
			mHint.setVisibility(View.GONE);
			requestChangePassword();
		}
	}
	
	private void clearinfo() {
		AppVariables.clear();
	}
	
}
