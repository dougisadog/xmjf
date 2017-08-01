package com.nangua.xiaomanjflc.ui;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.louding.frame.KJActivity;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.widget.InviteCodeDialog;
import com.nangua.xiaomanjflc.widget.InviteCodeDialog.OnSubmitCallBack;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.nangua.xiaomanjflc.support.UIHelper;

public class SignupActivity extends KJActivity {

	@BindView(id = R.id.tel)
	private EditText mTel;
	@BindView(id = R.id.tel_verify)
	private EditText mTelVerify;
	@BindView(id = R.id.pwd)
	private EditText mPwd;
	@BindView(id = R.id.pwdconfirm)
	private EditText mPwdConfirm;
	@BindView(id = R.id.code, click = true)
	private TextView mCode;
	@BindView(id = R.id.inviteCode, click = true)
	private EditText mInviteCode;
	@BindView(id = R.id.signup, click = true)
	private TextView mSignup;
	@BindView(id = R.id.hint)
	private TextView mHint;
	@BindView(id = R.id.protocol, click = true)
	private TextView mProtocol;
	
	@BindView(id = R.id.imgCaptcha, click = true)
	private ImageView imgCaptcha;
	@BindView(id = R.id.txtCaptcha)
	private EditText txtCaptcha;
	

	private String tel;
	private String code;
	private String pwd;
	private String pwdc;
	private String sid;
	private String captcha;
	private String refCode;
	private boolean hascode;

	private KJHttp kjh;
	private KJDB kjdb;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_signup_v2);
		UIHelper.setTitleView(this, "返回", "注册");
	}

	@Override
	public void initData() {
		super.initData();
		hascode = false;
		sid = "";
		captcha = "";
		kjh = new KJHttp();
		kjdb = KJDB.create(this);
	}
	
	

	@Override
	public void initWidget() {
		getCaptcha();
		super.initWidget();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.code:
				tel = mTel.getText().toString();
				captcha = txtCaptcha.getText().toString();
				if (StringUtils.isEmpty(captcha)) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText("请先输入图片验证码。");
				} 
				else if (StringUtils.isEmpty(tel) || (tel.length() < 11)) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText(R.string.signup_code);
				} else {
					mHint.setVisibility(View.GONE);
					getCode();
				}
				break;
			case R.id.signup:
				tel = mTel.getText().toString();
				code = mTelVerify.getText().toString();
				pwd = mPwd.getText().toString();
				pwdc = mPwdConfirm.getText().toString();
				refCode = mInviteCode.getText().toString();
				if (StringUtils.isEmpty(tel) || (tel.length() < 11)) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText(R.string.signup_code);
				} 
				else if (!hascode) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText(R.string.signup_hascode);
				}
				else if (StringUtils.isEmpty(code)) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText("短信验证码不能为空");
				}
				else if (StringUtils.isEmpty(pwd)) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText("密码不能为空");
				}
				else if (!StringUtils.isPasswordStrength(pwd)) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText(getResources().getString(R.string.sign_up_pwd_hint));
				}
				else if (!pwd.equals(pwdc)) {
					mHint.setVisibility(View.VISIBLE);
					mHint.setText(getResources().getString(R.string.different_pwd));
				}
//				else if (refCode.length() != 8) {
//					mHint.setVisibility(View.VISIBLE);
//					mHint.setText(getResources().getString(R.string.invite_code_limit));
//				}
				else if (StringUtils.isEmpty(refCode)) {
					signup();
				}
				else {
					mHint.setVisibility(View.GONE);
					//refCode = "bcGfL";
					getInvitedUser();
				}
				break;
			case R.id.protocol:
				startActivity(new Intent(this, SignupProtocolActivity.class));
				break;
			case R.id.imgCaptcha:
				getCaptcha();
				break;
		}
	}
	
	private void getInvitedUser() {
		HttpParams params = new HttpParams();
		params.put("refCode", refCode);
		
		kjh.post(AppConstants.INVITED_USER, params, new HttpCallBack(
				SignupActivity.this) {
			@Override
			public void onFinish() {
				mSignup.setClickable(true);
				mSignup.setBackgroundResource(R.drawable.btn_blue);
				super.onFinish();
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				String name = "";
				String phone = "";
				try {
					name = ret.getString("name");
					phone = ret.getString("phone");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				showInvitedUserDialog(name, phone);
			}

			@Override
			public void failure(JSONObject ret) {
				mHint.setVisibility(View.VISIBLE);
				String msg = "邀请码错误";
				try {
					msg = ret.getString("msg");
				} catch (JSONException e) {
					mHint.setText(msg);
					e.printStackTrace();
				}
				mHint.setText(msg);
			}
			
			
		});
	}
	
	private void showInvitedUserDialog(String name, String phone) {
		final InviteCodeDialog inviteCodeDialog = new InviteCodeDialog(SignupActivity.this, name, phone);
		inviteCodeDialog.setCallBack(new OnSubmitCallBack() {
			
			@Override
			public void onSubmit() {
				signup();
				inviteCodeDialog.dismiss();
				
			}
		});
		inviteCodeDialog.show();
	}

	private void signup()
	{
		
		mSignup.setClickable(false);
		mSignup.setBackgroundResource(R.drawable.btn_grey);
		
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("phone", tel);
		params.put("password", pwd);
		params.put("phoneCode", code);
		params.put("captcha", captcha);
		params.put("inviteCode", refCode);
		
		kjh.post(AppConstants.SIGNUP, params, new HttpCallBack(
				SignupActivity.this) {
			@Override
			public void onFinish() {
				mSignup.setClickable(true);
				mSignup.setBackgroundResource(R.drawable.btn_blue);
				super.onFinish();
			}

			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				try {
					ret.getJSONObject("body");
					sid = ret.getString("sid");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				mHint.setVisibility(View.VISIBLE);
				mHint.setText("注册成功。");
				Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				// 注册成功后登录
				InfoManager.getInstance().loginNormal(SignupActivity.this, tel, pwd, new TaskCallBack() {
					
					@Override
					public void taskSuccess() {
					}
					
					@Override
					public void afterTask() {
						showActivity(SignupActivity.this, AccountActivity.class);
						setResult(AppConstants.SUCCESS);
						SignupActivity.this.finish();
					}

					@Override
					public void taskFail(String err, int type) {
						if (type == TaskCallBack.TXT) {
							Toast.makeText(SignupActivity.this, "数据解析错误", Toast.LENGTH_LONG).show();
						}
						else if (type == TaskCallBack.JSON) {
							Toast.makeText(SignupActivity.this, "数据拉取失败", Toast.LENGTH_LONG).show();
						}
						
					}
				});
			}
		});
	}
	

	private void getCode()
	{
		System.out.println("My: 获取手机验证码");
		
		captcha = txtCaptcha.getText().toString();
		HttpParams params = new HttpParams();
		params.put("phone", tel);
		params.put("sid", sid);
		params.put("captcha", captcha);
		String captchaKey = CacheBean.getInstance().getCaptchaKey();
		params.put("captchaKey", captchaKey);
		
		kjh.post(AppConstants.GETCODE_V2, params, new HttpCallBack(SignupActivity.this) 
		{
			@Override
			public void failure(JSONObject ret) 
			{								
				super.failure(ret);
				System.out.println("My: 获取手机验证码失败");
				
				try 
				{
					if (ret.getBoolean("needCaptcha")) {
						getCaptcha();
					}
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}
			}

			@Override
			public void success(JSONObject ret) 
			{							
				super.success(ret);
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(R.string.signup_code_success);
				hascode = true;
				buttonHandle.post(buttonControl);
			}
		});
	}

	Runnable buttonControl = new Runnable() {
		int sec = 60;

		@Override
		public void run() {
			Message msg = buttonHandle.obtainMessage();
			sec -= 1;
			msg.arg1 = sec;
			buttonHandle.sendMessage(msg);
			if (sec == 0) {
				sec = 60;// 读完秒 按下重新获取之后把sec重新设定为60
			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler buttonHandle = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mCode.setText(msg.arg1 + "秒后重发");
//			mCode.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//	                getResources().getDimensionPixelSize(R.dimen.font_hint_small));
			if (msg.arg1 == 0) {
				buttonHandle.removeCallbacks(buttonControl);
				mCode.setBackgroundResource(R.drawable.btn_blue);
				mCode.setText("获取验证码");
//				mCode.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//		                getResources().getDimensionPixelSize(R.dimen.font_hint));
				mCode.setClickable(true);
			} else {
				mCode.setClickable(false);
				mCode.setBackgroundResource(R.drawable.btn_grey);
				buttonHandle.postDelayed(buttonControl, 1000);
			}

		}

		;
	};
	
	private void getCaptcha() {
		kjh.post(AppConstants.CAPTCHA, new HttpParams(), new HttpCallBack(SignupActivity.this,false,true) {

			@Override
			public void onSuccess(InputStream input ,Map<String, List<String>> headers) {
				CacheBean.getInstance().setCaptcha(BitmapFactory.decodeStream(input));
				if (null != headers.get("captchaKey") && headers.get("captchaKey").size() > 0)
					CacheBean.getInstance().setCaptchaKey(headers.get("captchaKey").get(0));
				super.onSuccess(input);
			}

			@Override
			public void onSuccess(String t) {
				imgCaptcha.setImageBitmap(CacheBean.getInstance().getCaptcha());
			}
		
		});
	}
	
}
