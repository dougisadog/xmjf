package com.nangua.xiaomanjflc.ui;

import org.json.JSONObject;

import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConfig;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.AutoUpdateManager;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.StartApplication;
import com.nangua.xiaomanjflc.AutoUpdateManager.UpdateCallback;
import com.nangua.xiaomanjflc.bean.Update;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.ApkInfo;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.support.UpdateManager;
import com.nangua.xiaomanjflc.support.UpdateManager.CheckVersionInterface;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutListActivity extends KJActivity implements CheckVersionInterface{
	
	@BindView(id = R.id.txtVersion, click = true)
	private TextView txtVersion;
	@BindView(id = R.id.about, click = true)
	private RelativeLayout about;
	@BindView(id = R.id.report, click = true)
	private RelativeLayout report;
	@BindView(id = R.id.checkVersion, click = true)
	private RelativeLayout checkVersion;
	@BindView(id = R.id.iv_red_point, click = true)
	private ImageView iv_red_point;
	
	@BindView(id = R.id.help, click = true)
	private RelativeLayout help;
	@BindView(id = R.id.comment, click = true)
	private RelativeLayout comment;
	
	
	
	private Update u;
	private JSONObject versionInfo;

	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_about_list);
		UIHelper.setTitleView(this, "", "关于");
	}
	
	@Override
	public void initWidget() {
		super.initWidget();
		ApkInfo apkInfo = ApplicationUtil.getApkInfo(this);
		txtVersion.setText("当前版本为" + apkInfo.versionName);
		
		String noUpdateVersion = AppConfig.getAppConfig(AboutListActivity.this).get(
				AppConfig.NOT_UPDATE_DIALOG_VERSION);
		if (null == noUpdateVersion) {
			noUpdateVersion = apkInfo.versionCode + "";
			AppConfig.getAppConfig(AboutListActivity.this).set(
					AppConfig.NOT_UPDATE_DIALOG_VERSION, noUpdateVersion);
		}
		String lastVersion = CacheBean.getInstance().getRedConditions().get("lastVersion");
		if (!StringUtils.isEmpty(lastVersion) && Integer.parseInt(lastVersion) > Integer.parseInt(noUpdateVersion)) {
			iv_red_point.setVisibility(View.VISIBLE);
		}
		else {
			iv_red_point.setVisibility(View.INVISIBLE);
		}
		boolean checkUpdate = getIntent().getBooleanExtra("update", false);
		if (checkUpdate) {
			checkUpdate();
		}
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.about :
				startActivity(new Intent(AboutListActivity.this, AboutActivity.class));
				break;
			case R.id.report :
				startActivity(new Intent(AboutListActivity.this, ReportActivity.class));
				break;
			case R.id.checkVersion :
				AppConfig.getAppConfig(AboutListActivity.this).set(
						AppConfig.NOT_UPDATE_DIALOG_VERSION,
						CacheBean.getInstance().getApkInfo().versionCode + "");
				checkUpdate();
				break;
			case R.id.help:
				startActivity(new Intent(AboutListActivity.this, HelpCenterActivity.class));
				break;
			case R.id.comment:
				final LoudingDialogIOS ldcall = new LoudingDialogIOS(AboutListActivity.this);
				ldcall.showOperateMessage("确定拨打电话" + getResources().getString(R.string.support_tel_text2) + "？");
				ldcall.setPositiveButton("确定", R.drawable.dialog_positive_btn,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse("tel:" + getResources().getString(R.string.support_tel2)));
								startActivity(intent);
								ldcall.dismiss();
							}
						});
				break;
			default :
				break;
		}
	}
	
	private void checkUpdate() {
		AutoUpdateManager.getInstance().setUpdateCallback(new UpdateCallback() {
			
			@Override
			public void onUpdated() {
			}
			
			@Override
			public void onNoUpdate() {
			}
			
			@Override
			public void onBeforeUpdate() {
			}
		});
//		AutoUpdateManager.getInstance().setShowMsg(true);
		StartApplication.parseChannel(AboutListActivity.this);
		String lastVersion = CacheBean.getInstance().getRedConditions().get("lastVersion");
		if (lastVersion == null) {
			lastVersion = ApplicationUtil.getApkInfo(this).versionCode + "";
			LoudingDialogIOS ld = new LoudingDialogIOS(this);
			ld.showConfirmHint("当前为最新版本");
		}
		AppConfig.getAppConfig(AboutListActivity.this).set(
				AppConfig.NOT_UPDATE_DIALOG_VERSION, lastVersion);
		AppVariables.forceUpdate = true;
		iv_red_point.setVisibility(View.INVISIBLE);
	}

	@Override
	public Update checkVersion() throws Exception {
		try {
			u = new Update(versionInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}

}
