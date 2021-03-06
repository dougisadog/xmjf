package com.nangua.xiaomanjflc.ui;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.widget.ContentView;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.nangua.xiaomanjflc.widget.Drawl.GestureCallBack;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.database.UserConfig;
import com.nangua.xiaomanjflc.bean.jsonbean.Account;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;

public class GestureActivity extends Activity {

	private LinearLayout headLinear;
	private FrameLayout body_layout;
	private ContentView content;
	private TextView hint;
	private TextView forget;
	private TextView operate;
	private TextView transfer;
	private TextView welcome;

	private KJHttp kjh;
	private KJDB kjdb;
	private boolean isSet;
	private int step;
	private String name = null;

	private String pwd;
	private UserConfig userConfig;
	
	public static final int GESTURE_CODE = 10002;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		
		Intent intent = getIntent();
		isSet = intent.getBooleanExtra("isSet", false);
		initview(isSet);
		body_layout = (FrameLayout) findViewById(R.id.body_layout);
		
		kjdb = KJDB.create(this);
		List<UserConfig> userConfigs = kjdb.findAllByWhere(UserConfig.class, "uid=" + AppVariables.uid);
		if (userConfigs.size() > 0) {
			userConfig = userConfigs.get(0);
		}
		else {
			userConfig = new UserConfig();
		}
		pwd = userConfig.getHandPwd();
		if (StringUtils.isEmpty(pwd)) {
			pwd = "";
			step = 2;
			setIconVisible(false);
			hint.setText("绘制解锁图案");
		} else {
			step = 1;
			setIconVisible(false);
			hint.setText("请输入手势密码");
		}
		// 初始化一个显示各个点的viewGroup
		content = new ContentView(this, pwd, new GestureCallBack() {

			@Override
			public void checkedSuccess() {
				if (!isSet) {
					finish();
				}
			}

			@Override
			public void checkedFail() {
				if (!isSet) {
					setIconVisible(true);
					hint.setText("手势密码错误");
				}
			}

			@Override
			public void gestureCode(String code) {
				if (isSet) {
					switch (step) {
					case 1:
						if (pwd.equals(code)) {
							step = 2;
							setIconVisible(false);
							hint.setText("绘制解锁图案");
						} else {
							setIconVisible(true);
							hint.setText("手势错误");
						}
						break;
					case 2:
						if (code.length() < 4) {
							setIconVisible(false);
							hint.setText("至少连接4个点，请重新绘制。");
						} else {
							pwd = code;
							setIconVisible(false);
							hint.setText("再次绘制解锁图案");
							step = 3;
						}
						break;
					case 3:
						if (pwd.equals(code)) {
							setIconVisible(false);
							hint.setText("设置成功。");
							
							//修改内存和db
							AppVariables.needGesture = false;
							userConfig.setNeedGesture(true);
							userConfig.setHandPwd(pwd);
							userConfig.setLastGestureCheckTime(new Date().getTime());
							kjdb.update(userConfig);
							
							new Handler().postDelayed(new Runnable() {
								public void run() {
									GestureActivity.this.finish();
								}
							}, 1000);
						} else {
							setIconVisible(false);
							hint.setText("与上一次绘制不一致，请重新绘制");
							step = 2;
						}
						break;
					}
				}
			}
		});
		// 设置手势解锁显示到哪个布局里面
		content.setParentView(body_layout);
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case VerifyPwd.SUCCESS:
			finish();
			break;
		case VerifyPwd.FAIL:
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	private void initview(boolean isSet) {
		headLinear = (LinearLayout) this.findViewById(R.id.headLinear);
		hint = (TextView) this.findViewById(R.id.hint);
		forget = (TextView) this.findViewById(R.id.forget);
		forget.setOnClickListener(listerner);
		operate = (TextView) this.findViewById(R.id.operate);
		transfer = (TextView) this.findViewById(R.id.transfer);
		transfer.setOnClickListener(listerner);
		welcome = (TextView) this.findViewById(R.id.welcome);
		getName();
		setIconVisible(false);
		if (isSet) {
			headLinear.setVisibility(View.VISIBLE);
			setTitleView();
			welcome.setVisibility(View.GONE);
			forget.setVisibility(View.GONE);
			operate.setVisibility(View.VISIBLE);
			transfer.setVisibility(View.GONE);
		} else {
			headLinear.setVisibility(View.GONE);
			welcome.setVisibility(View.VISIBLE);
			forget.setVisibility(View.VISIBLE);
			operate.setVisibility(View.GONE);
			transfer.setVisibility(View.VISIBLE);
		}
	}

	private OnClickListener listerner = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.transfer:
				final LoudingDialogIOS ld = new LoudingDialogIOS(GestureActivity.this);
				ld.showOperateMessage("确定更改账号？");
				ld.setPositiveButton("确定", R.drawable.dialog_positive_btn,
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								clearinfo();
								ld.dismiss();
								startActivity(new Intent(GestureActivity.this,
										SigninActivity.class));
								finish();
							}
						});
				break;
			case R.id.forget:
				Intent intent = new Intent(GestureActivity.this, VerifyPwd.class);
				intent.putExtra("reset", true);
				startActivityForResult(intent, GESTURE_CODE);
				break;
			case R.id.flleft:
			case R.id.flright:
				finish();
				break;
			}
		}
	};

	private void setTitleView() {
		TextView btnRitht = null;
		TextView titleTv = null;
		ImageView titleImage = null;
		btnRitht = (TextView) this.findViewById(R.id.title_right);
		titleTv = (TextView) this.findViewById(R.id.title_center);
		titleImage = (ImageView) this.findViewById(R.id.title_image);
		btnRitht.setText("跳过");
		titleImage.setVisibility(View.GONE);
		btnRitht.setVisibility(View.VISIBLE);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText("设置手势密码");
		FrameLayout flright = (FrameLayout) findViewById(R.id.flright);
		flright.setOnClickListener(listerner);
		FrameLayout flleft = (FrameLayout) findViewById(R.id.flleft);
		flleft.setOnClickListener(listerner);
	}

	private void setIconVisible(boolean visible) {
		if (visible) {
			Drawable icon;
			Resources res = getResources();
			icon = res.getDrawable(R.drawable.gesture_icon);
			icon.setBounds(0, 0, icon.getMinimumWidth(),
					icon.getMinimumHeight());
			hint.setCompoundDrawables(icon, null, null, null);
		} else {
			hint.setCompoundDrawables(null, null, null, null);
		}
	}

	@Override
	public void onBackPressed() {
		if (isSet) {
			finish();
		} else {
			final LoudingDialogIOS ld = new LoudingDialogIOS(this);
			ld.showOperateMessage("是否退出登录？");
			ld.setPositiveButton("确定", R.drawable.dialog_positive_btn,
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							clearinfo();
							ld.dismiss();
							finish();
						}
					});
		}
	}

	private void clearinfo() {
		AppVariables.clear();
	}

	private void getName() {
		InfoManager.getInstance().getInfo(this, new TaskCallBack() {

			@Override
			public void taskSuccess() {
				User user = CacheBean.getInstance().getUser();
				Account account = CacheBean.getInstance().getAccount();
				if (user.getNameValidated() == 1) {
					name = account.getRealName();
				} else {
					name = user.getPhone();
				}
				welcome.setText("欢迎您，" + name);
			}

			@Override
			public void taskFail(String err, int type) {
			}

			@Override
			public void afterTask() {
			}
		});
	}

}
