package com.nangua.xiaomanjflc.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ips.p2p.StartPluginTools;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.bean.CItem;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.utils.FormatUtils;
import com.nangua.xiaomanjflc.utils.ToastUtil;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.umeng.analytics.MobclickAgent;
import com.nangua.xiaomanjflc.R;

public class ChargeActivity extends KJActivity {

	@BindView(id = R.id.charge, click = true)
	private TextView mCharge;
	@BindView(id = R.id.price)
	private EditText mPrice;
	@BindView(id = R.id.balance)
	private TextView mBalance;
	
	@BindView(id = R.id.supportTel)
	private TextView supportTel;
	
	@BindView(id = R.id.bankCode)
	private Spinner mbankCode;
	
	@BindView(id = R.id.thr)
	private LinearLayout thr;

	private KJHttp kjh;
	private LoudingDialogIOS ld;

	private int balance;
	private String price;
	private String bankCode;
	private List<CItem> data_list;
	
	private String type;    // 用户类型
	private String ipsAccount;    // 用户的环迅帐号
	private static final String LOGTAG = "ChargeActivity";

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_charge);
		UIHelper.setTitleView(this, "我的账户", "充值");
	}

	@Override
	public void initData()
	{
		super.initData();
		kjh = new KJHttp();
		Intent intent = getIntent();
		
		balance = intent.getIntExtra("balance", 0);
		type = intent.getStringExtra("type");
		ipsAccount = intent.getStringExtra("ipsAccount");
		
	}

	@Override
	public void initWidget() {
		super.initWidget();
		supportTel.setText("4.在充值过程如遇任何问题，请联系小满金服客服" + getResources().getString(R.string.support_tel_text2)+ "。");
		mBalance.setText(FormatUtils.fmtMicrometer(FormatUtils.priceFormat(balance)) + "元");
		thr.setVisibility(View.GONE);
		mPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						mPrice.setText(s);
						mPrice.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					mPrice.setText(s);
					mPrice.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						mPrice.setText(s.subSequence(0, 1));
						mPrice.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
//		getBankCode();
	}
	
	//暂定取消银行号参数获取 简化充值
	private void getBankCode() {
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		kjh.post(AppConstants.GET_BANKCODE, params, new HttpCallBack(
				ChargeActivity.this) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					JSONArray bankCodeArray = ret.getJSONArray("bankCode");

					data_list = new ArrayList<CItem>();
					for (int i = 0; i < bankCodeArray.length(); i++) {
						JSONObject o = (JSONObject) bankCodeArray.get(i);
						data_list.add(new CItem(o.getString("bankName"), o.getString("bankCode")));
					}
					if (data_list == null || data_list.isEmpty()) {
						 thr.setVisibility(View.GONE);
					} else {
						// 建立Adapter并且绑定数据源
						ArrayAdapter<CItem > adapter = new ArrayAdapter<CItem>(ChargeActivity.this, android.R.layout.simple_spinner_item, data_list);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						//绑定 Adapter到控件
						mbankCode.setAdapter(adapter);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			

		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);

		switch (v.getId()) {
		case R.id.charge: // 充值

			double priced = 0;

			String input = mPrice.getText().toString(); // 输入的金额

			if (null != input && !"".equals(input)) // 输入金额不为空
			{
				price = input;
//				CItem item = (CItem) mbankCode.getSelectedItem();
//				if (null != item) {
//					bankCode = item.getValue();
//				}
				priced = Double.parseDouble(price);
			}
			if (priced < 100) {
				ld = new LoudingDialogIOS(ChargeActivity.this);
				ld.showConfirmHint(getResources().getString(R.string.lesat_charge_amount));
				return;
			}

			if (priced < 10000000) {
				if (StringUtils.isEmpty(price)) {
					ld = new LoudingDialogIOS(ChargeActivity.this);
					ld.showConfirmHint("请输入金额。");
				} else {
					post();
				}
			} else {
				ToastUtil.showToast(ChargeActivity.this, "充值金额不得超过10,000,000元", Toast.LENGTH_SHORT);
			}

			break;
		}
	}
	
	
	/**
	 * 开启环迅插件支付
	 * @param server返回的支付信息json
	 */
	private void chargeAction(JSONObject ret) {
		try {
			Bundle bundle = new Bundle();
			bundle.putString("operationType", ret.getString("operationType"));
			bundle.putString("merchantID", ret.getString("merchantID"));
			bundle.putString("sign", ret.getString("sign"));
			bundle.putString("request", ret.getString("request"));
			bundle.putString("depositType", ret.getString("depositType"));//充值类型
			StartPluginTools.start_p2p_plugin(StartPluginTools.RECHARGE, ChargeActivity.this, bundle, AppConstants.IPS_VERSION);
			
			//Umeng事件统计
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("amount",price); //充值金额
			MobclickAgent.onEvent(this, StartPluginTools.RECHARGE, map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void post() {
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("amount", price);
//		if (!StringUtils.isEmpty(bankCode))
//		params.put("bankCode", bankCode);
		kjh.post(AppConstants.CHARGE, params, new HttpCallBack(
				ChargeActivity.this) {
			@Override
			public void success(JSONObject ret) {
				try {
					if ("ips".equals(ret.getString("pay.provider"))) {
						chargeAction(ret);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    protected void onNewIntent(Intent intent)    // 当插件调用完毕后返回时执行该方法
    {
    	AppVariables.forceUpdate = true;
        Bundle bundle = intent.getExtras();
        
        if (bundle != null)
        {
            printExtras(bundle);
            String resultCode = bundle.getString("resultCode");
            String resultMsg = bundle.getString("resultMsg");
            String merchantID = bundle.getString("merchantID");
            String sign = bundle.getString("sign");

            Log.e(LOGTAG, "resultCode" + ":" + resultCode);
            Log.e(LOGTAG, "resultMsg" + ":" + resultMsg);
            Log.e(LOGTAG, "merchantID" + ":" + merchantID);
            Log.e(LOGTAG, "sign" + ":" + sign);

        }
        finish();
    }
    
    protected void printExtras(Bundle extras) 
    {
        if (extras != null) 
        {
            Log.e(LOGTAG, "打印开始");
            for ( String key : extras.keySet() ) 
            {
                Log.i( LOGTAG, key + ": " + extras.get(key) );
            }
            Log.e(LOGTAG, "打印结束");
        } 
        else 
        {
            Log.w(LOGTAG, "Extras is null");
        }
    }
    
}
