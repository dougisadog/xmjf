package com.nangua.xiaomanjflc.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ips.p2p.StartPluginTools;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.umeng.analytics.MobclickAgent;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.support.UIHelper;

public class IdcardActivity extends KJActivity {

	private static final String LOGTAG = "IdcardActivity";
	
	@BindView(id = R.id.post, click = true)
	private TextView mPost;
	@BindView(id = R.id.name)
	private EditText mName;
	@BindView(id = R.id.id)
	private EditText mId;

	private KJHttp kjh;
	private LoudingDialogIOS ld;

	private String idcard;
	private String realName;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_idcard);
		UIHelper.setTitleView(this, "账户中心", "实名认证");
		kjh = new KJHttp();
	}
	
    @Override
    public void initWidget() {
    	String groupValided = CacheBean.getInstance().getAccount().getGroupValided();
    	if ("1".equals(groupValided)) {
    		mName.setText(CacheBean.getInstance().getAccount().getGroupUserName());
    		mId.setText(CacheBean.getInstance().getAccount().getGroupIdCard());
    		mName.setEnabled(false);
    		mId.setEnabled(false);
    	}
    }

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.post:
			realName = mName.getText().toString();
			idcard = mId.getText().toString();
			if (StringUtils.isEmpty(realName) || StringUtils.isEmpty(idcard)) {
				ld = new LoudingDialogIOS(IdcardActivity.this);
				ld.showConfirmHint("请填写完整信息");
			} else if (!StringUtils.isIdCard(idcard)) {
				ld = new LoudingDialogIOS(IdcardActivity.this);
				ld.showConfirmHint("请填写正确的身份证");
			}
			else if (!"1".equals(CacheBean.getInstance().getAccount().getGroupValided())) {
				final LoudingDialogIOS ld = new LoudingDialogIOS(this);
				ld.showOperateMessage("请确认您的姓名和身份证信息，\n确认后将不可更改\n"
						+ "姓名：" + realName + 
						"\n身份证：" + idcard);
				ld.setPositiveButton("继续", R.drawable.dialog_positive_btn, new OnClickListener() {
					@Override
					public void onClick(View v) {
						post();
						ld.dismiss();
					}
				});
			}
			else {
				post();
			}
			break;
		}
	}
	
	
	/**
	 * 开启环迅插件 开户
	 * @param server返回的支付信息json
	 */
	private void createAccountAction(JSONObject ret) {
		try {
			Bundle bundle = new Bundle();
			bundle.putString("operationType", ret.getString("operationType"));
			bundle.putString("merchantID", ret.getString("merchantID"));
			bundle.putString("sign", ret.getString("sign"));
			bundle.putString("request", ret.getString("request"));
			StartPluginTools.start_p2p_plugin(StartPluginTools.CREATE_ACCT, IdcardActivity.this, bundle, AppConstants.IPS_VERSION);
			
			//Umeng事件统计
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("idCard", idcard);
			map.put("realName", realName);
			MobclickAgent.onEvent(this, StartPluginTools.CREATE_ACCT, map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void post() {
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("idCard", idcard);
		params.put("realName", realName);
		kjh.post(AppConstants.IDCARD, params, new HttpCallBack(
				IdcardActivity.this) {
			@Override
			public void success(JSONObject ret) {
				try {
					if ("ips".equals(ret.getString("pay.provider"))) {
						createAccountAction(ret);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failure(JSONObject ret) {
				try {
					if ("8000".equals(ret.getString("status"))) {
						final String groupValided = ret.getString("groupValided");
						if (!StringUtils.isEmpty(groupValided)) {
							String groupUserName = ret.getString("groupUserName");
							String groupIdCard = ret.getString("groupIdCard");
							CacheBean.getInstance().getAccount().setGroupValided(groupValided);
							CacheBean.getInstance().getAccount().setGroupUserName(groupUserName);
							CacheBean.getInstance().getAccount().setGroupIdCard(groupIdCard);
						}
						final LoudingDialogIOS ldc = new LoudingDialogIOS(IdcardActivity.this);
						ldc.setTitle(R.string.dialog_title, R.color.black);
						ldc.setMessage(ret.getString("msg"), R.color.black);
						ldc.setPositiveButton(
								getResources().getString(R.string.dialog_confirm),
								null, new OnClickListener() {
									@Override
									public void onClick(View arg0) {
										ldc.dismiss();
										if ("1".equals(groupValided)) {
											initWidget();
										}
										else {
											AppVariables.forceUpdate = true;
											finish();
										}
									}
								});
					}
					else {
						super.failure(ret);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	
	// 当插件调用完毕后返回时执行该方法
    protected void onNewIntent(Intent intent) {
    	AppVariables.forceUpdate = true;
    	Bundle bundle = intent.getExtras();
    	if (bundle != null) {
    		printExtras(bundle);
    		String resultCode= bundle.getString("resultCode");
            String resultMsg= bundle.getString("resultMsg");
            String merchantID= bundle.getString("merchantID");
            String sign= bundle.getString("sign");
            
            Log.e(LOGTAG, "resultCode"+":"+resultCode);
			Log.e(LOGTAG, "resultMsg"+":"+resultMsg);
			Log.e(LOGTAG, "merchantID"+":"+merchantID);
			Log.e(LOGTAG, "sign"+":"+sign);
    	}
    	finish();
    }
	
    protected void printExtras(Bundle extras) {
        if (extras != null) {
            Log.e(LOGTAG, "╔*************************打印开始*************************╗");
            for (String key : extras.keySet()) {
                Log.i(LOGTAG, key + ": " + extras.get(key));
            }
            Log.e(LOGTAG, "╚═════════════════════════打印结束═════════════════════════╝");
        } else {
            Log.w(LOGTAG, "Extras is null");
        }
    }

}
