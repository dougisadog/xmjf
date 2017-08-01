package com.nangua.xiaomanjflc.ui.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.DensityUtils;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConfig;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.utils.FormatUtils;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.jsonbean.Account;
import com.nangua.xiaomanjflc.bean.jsonbean.GainData;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.ApkInfo;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.ui.AboutListActivity;
import com.nangua.xiaomanjflc.ui.AccountActivity;
import com.nangua.xiaomanjflc.ui.BindActivity;
import com.nangua.xiaomanjflc.ui.CashActivity;
import com.nangua.xiaomanjflc.ui.ChargeActivity;
import com.nangua.xiaomanjflc.ui.InvestListActivity;
import com.nangua.xiaomanjflc.ui.MainActivity;
import com.nangua.xiaomanjflc.ui.MessageCenterActivity;
import com.nangua.xiaomanjflc.ui.NormalInviteActivity;
import com.nangua.xiaomanjflc.ui.RedActivity;
import com.nangua.xiaomanjflc.ui.SettingActivity;
import com.nangua.xiaomanjflc.ui.myabstract.HomeFragment;

public class AccountFragment extends HomeFragment {
	private View v;
	private TextView mTotal;
	private TextView mGain;
	private TextView mUnrepaid;
	private TextView mAmount;
	private TextView mBalance;
	private TextView mfrozeAmount;
	private RelativeLayout mAccount;
	private RelativeLayout mInvest;
	private RelativeLayout mInvite;
	private RelativeLayout mRed;
	private RelativeLayout settting;
	private RelativeLayout aboutList; 
	private TextView mCharge;
	private TextView mCash;
	
	private ImageView iv_red_point2;
	
	private TextView mDate;
	private int totalInterest;
	private int unrepaidInterest;
	private int available;
	private int investAmount;
	private int frozeAmount;
	private int noreadmessage;
	
	private TextView tel;
	private TextView idStatus;
	
	
	private int reqType = 0;
	
	private static final int CHARGE = 1;
	private static final int CASH = 2;
	private static final int BIND = 3;

	private KJHttp http;
	private HttpParams params;

	private int idValidated;
	private int status;
	private ImageView imgRight;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		initialed = true;
		
		v = inflater.inflate(R.layout.fragment_account_v2, null);
		UIHelper.setTitleView(v, "", "", "我的", 0);
		UIHelper.setBtnRight(v, R.drawable.icon_news, listener);
		ImageView imgLeft = (ImageView) v.findViewById(R.id.img_left);
		imgLeft.setVisibility(View.INVISIBLE);
		
		imgRight = (ImageView) v.findViewById(R.id.img_right);
		int paddingH = DensityUtils.dip2px(getActivity(), 15);
		int paddingW = DensityUtils.dip2px(getActivity(), 10);
		imgRight.setPadding(paddingW, paddingH, 0, paddingH);
		
		http = new KJHttp();
		params = new HttpParams();

		initView();
		getUserAccount(); // 获取用户信息

		return v;
	}


	private void initView() {
		mTotal = (TextView) v.findViewById(R.id.total);
		mGain = (TextView) v.findViewById(R.id.gain);
		mUnrepaid = (TextView) v.findViewById(R.id.unrepaid);
		mAmount = (TextView) v.findViewById(R.id.amount);
		mBalance = (TextView) v.findViewById(R.id.balance);
		mfrozeAmount = (TextView) v.findViewById(R.id.frozeAmount);
		mAccount = (RelativeLayout) v.findViewById(R.id.account);
		mAccount.setOnClickListener(listener);
		mInvest = (RelativeLayout) v.findViewById(R.id.invest);
		mInvest.setOnClickListener(listener);
		mRed = (RelativeLayout) v.findViewById(R.id.red);
		mRed.setOnClickListener(listener);
		mInvite = (RelativeLayout) v.findViewById(R.id.invite);
		mInvite.setOnClickListener(listener);
	    settting = (RelativeLayout) v.findViewById(R.id.settting);
		settting.setOnClickListener(listener);
		UIHelper.setBtnRight(v, R.drawable.icon_news, listener);
		
		aboutList = (RelativeLayout) v.findViewById(R.id.aboutList);
		aboutList.setOnClickListener(listener);

		mCharge = (TextView) v.findViewById(R.id.charge);
		mCharge.setOnClickListener(listener);
		mCash = (TextView) v.findViewById(R.id.cash);
		mCash.setOnClickListener(listener);
		
		iv_red_point2 = (ImageView) v.findViewById(R.id.iv_red_point2);
		
		mDate = (TextView) v.findViewById(R.id.date);
		
		idStatus = (TextView) v.findViewById(R.id.idStatus);
		tel = (TextView) v.findViewById(R.id.tel);
		
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.account:
				getActivity().startActivityForResult(new Intent(getActivity(), AccountActivity.class), MainActivity.REQUEST_CODE);
				break;
			case R.id.invest:
				startActivity(new Intent(getActivity(), InvestListActivity.class));
				break;
				//现金券
			case R.id.red:
				getActivity().startActivityForResult(new Intent(getActivity(), RedActivity.class), MainActivity.REQUEST_CODE);
				break;
			case R.id.invite:
				Intent intent = new Intent(getActivity(), NormalInviteActivity.class);
				intent.putExtra("activity", "account");
				startActivity(intent);
				break;
				
			case R.id.charge: // 充值
				if (idValidated == 1 && status == 2) {
					Intent charge = new Intent(getActivity(), ChargeActivity.class);
					charge.putExtra("balance", available);
					startActivity(charge);
				} else {
					reqType = CHARGE;
					getInfo();
				}
				break;

			case R.id.cash:
				if (idValidated == 1 && status == 2) {
					Intent cash = new Intent(getActivity(), CashActivity.class);
					cash.putExtra("balance", available);
					startActivity(cash);
				} else {
					reqType = CASH;
					getInfo();
				}
				break;
			case R.id.flright:
				noreadmessage = 0;
				startActivity(new Intent(getActivity(), MessageCenterActivity.class));
				break;
			case R.id.settting:
				getActivity().startActivityForResult(new Intent(getActivity(), SettingActivity.class),  MainActivity.REQUEST_CODE);
				break;
			case R.id.aboutList:
				startActivity(new Intent(getActivity(), AboutListActivity.class));
				break;
			}
		}
	};
	
	private void getIPSData() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.IPS_LOGIN, params, new HttpCallBack(getActivity(), false) {
			@Override
			public void onSuccess(String t) {
					Intent i = new Intent(getActivity() ,BindActivity.class);
					try {
						JSONObject ret = new JSONObject(t);
						if (ret.has("userName"))
							i.putExtra("userName", ret.getString("userName"));
						if (ret.has("url"))
							i.putExtra("url", ret.getString("url"));
						if (ret.has("merchantId")) 
							i.putExtra("merchantId", ret.getString("merchantId"));
						startActivity(i);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
	}
	
	private void refreshGainData(GainData gainData) {
		if (null == gainData) return;
		totalInterest = gainData.getTotalInterest();
		unrepaidInterest = gainData.getUnrepaidInterest();
		available = gainData.getAvailable();
		investAmount = gainData.getInvestAmount();
		frozeAmount = gainData.getFrozeAmount();
		noreadmessage = gainData.getNoreadmessage();
		mTotal.setText(
				FormatUtils.fmtMicrometer(FormatUtils.priceFormat(available + investAmount + frozeAmount)));
		mGain.setText(FormatUtils.fmtMicrometer(FormatUtils.priceFormat(totalInterest)));
		mUnrepaid.setText(FormatUtils.fmtMicrometer(FormatUtils.priceFormat(unrepaidInterest)));
		mAmount.setText(FormatUtils.fmtMicrometer(FormatUtils.priceFormat(investAmount)));
		mBalance.setText(FormatUtils.fmtMicrometer(FormatUtils.priceFormat(available)));
		mfrozeAmount.setText(FormatUtils.fmtMicrometer(FormatUtils.priceFormat(frozeAmount)));
		
		if (noreadmessage > 0) {
			imgRight.setImageResource(R.drawable.icon_news_on);
		} else {
			imgRight.setImageResource(R.drawable.icon_news);
		}
	}
	
	/**
	 * 获取个人面板信息
	 */
	private void getData() {
		GainData gain = CacheBean.getInstance().getGainData();
		if (gain != null && !AppVariables.forceUpdate && !CacheBean.checkNeedUpdate()) {
			refreshGainData(CacheBean.getInstance().getGainData());
			return;
		}
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.GAIN, params, new HttpCallBack(getActivity(), true) {

			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					GainData gainData = FormatUtils.jsonParse(ret.toString(), GainData.class);
					CacheBean.getInstance().setGainData(gainData);
					refreshGainData(gainData);
					//TODO 信心显示
//					noreadmessage = 1;
//					if (null == handler)
//						taskGotMessage();
					AppVariables.forceUpdate = false;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	@Override
	public void onResume() {
//		if (null == handler)
//			taskGotMessage();
		super.onResume();
	}

	private Handler handler; 
	private int times = 0;
	
	
	/**
	 * 来信闪烁
	 */
	private void taskGotMessage() {
		handler = new Handler(); 
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				if (noreadmessage == 0) {
					handler.removeCallbacks(this);
					handler = null;
					return;
				}
				times++;
				if (times%2 == 0) {
					imgRight.setImageResource(R.drawable.icon_news);
					imgRight.setVisibility(View.VISIBLE);
				}
				else {
					imgRight.setVisibility(View.INVISIBLE);	
				}
				handler.postDelayed(this, 1000);
				// TODO Auto-generated method stub
			}
		});
	}
	
	/**
	 * 表单验证
	 */
	private void submitValidate() {
		if (idValidated == 1) {
			if (reqType == BIND) {
				getIPSData();
			}
			else {
				if (status == 0) {
					final LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
					ld.showOperateMessage("您还没有绑卡，请绑定银行卡。");
					ld.setPositiveButton("前往", R.drawable.dialog_positive_btn, new OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(), SettingActivity.class));
							ld.dismiss();
						}
					});
				} else if (status == 1) {
					final LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
					ld.showConfirmHint("您的银行卡正在审核中，审核结果将通过短信通知您。");
				} else if (status == 2) {
					Intent i = new Intent();
					switch (reqType) {
					case CHARGE:
						i.setClass(getActivity(), ChargeActivity.class);
						i.putExtra("balance", available);
						startActivity(i);
						break;
					case CASH:
						i.setClass(getActivity(), CashActivity.class);
						i.putExtra("balance", available);
						startActivity(i);
						break;
					default:
						break;
					}
				}
			}
		} else {
			final LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
			ld.showOperateMessage("请先实名认证。");
			ld.setPositiveButton("前往", R.drawable.dialog_positive_btn, new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(), AccountActivity.class));
					ld.dismiss();
				}
			});
		}
	}

	/**
	 * 及时拉取服务器基本信息
	 */
	private void getInfo() // 实名认证、绑定银行卡
	{
		User user = CacheBean.getInstance().getUser();
		if (null != user && user.getUid().equals(AppVariables.uid)) {
			idValidated = user.getIdValidated();
			status = CacheBean.getInstance().getAccount().getCardStatus();
		}
		else {
			InfoManager.getInstance().getInfo(getActivity(), new TaskCallBack() {
				
				@Override
				public void taskSuccess() {
					idValidated = CacheBean.getInstance().getUser().getIdValidated();
					status = CacheBean.getInstance().getAccount().getCardStatus();
					submitValidate();
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

	
	public void refreshData() {
		if (CacheBean.checkNeedUpdate()) {
			getUserAccount(); // 获取用户信息
		}
	}
	
	public void refreshView() {
		if (null != iv_red_point2) {
			iv_red_point2.setVisibility(checkRed() ? View.VISIBLE : View.INVISIBLE);
		}
		getData();
	}
	
	private boolean checkRed() {
		ApkInfo apkInfo = ApplicationUtil.getApkInfo(getActivity());
		String noUpdateVersion = AppConfig.getAppConfig(getActivity()).get(
				AppConfig.NOT_UPDATE_DIALOG_VERSION);
		if (null == noUpdateVersion) {
			noUpdateVersion = apkInfo.versionCode + "";
			AppConfig.getAppConfig(getActivity()).set(
					AppConfig.NOT_UPDATE_DIALOG_VERSION, noUpdateVersion);
		}
		String lastVersion = CacheBean.getInstance().getRedConditions().get("lastVersion");
		if (!StringUtils.isEmpty(lastVersion) && Integer.parseInt(lastVersion) > Integer.parseInt(noUpdateVersion)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param true 强制更新  false 需要检测
	 */
	public void refreshData(boolean forceUpdate) {
		if (forceUpdate || CacheBean.checkNeedUpdate()) {
			getUserAccount(); // 获取用户信息
		}
	}

	
	/**
	 * 页面加载时服务器信息拉取
	 */
	private void getUserAccount() // 获取用户信息
	{
		InfoManager.getInstance().getInfo(getActivity(), new TaskCallBack() {
					
					@Override
					public void taskSuccess() {
						User user = CacheBean.getInstance().getUser();
						Account account = CacheBean.getInstance().getAccount();
						//企业
						if (user.getType() == 1) {
							mCash.setOnClickListener(null);
							mCharge.setOnClickListener(null);
							mCash.setTextColor(getResources().getColor(R.color.grey));
							mCharge.setTextColor(getResources().getColor(R.color.grey));
						}
						//个人 
						else if (user.getType() == 0){
							mCash.setOnClickListener(listener);
							mCharge.setOnClickListener(listener);
							mCash.setTextColor(getResources().getColor(R.color.orange));
							mCharge.setTextColor(getResources().getColor(R.color.orange));
						}
						idValidated = user.getIdValidated();
						if (idValidated == 1) {
							idStatus.setText("(已认证)");
							String realName = CacheBean.getInstance().getAccount().getRealName();
							tel.setText(TextUtils.isEmpty(realName) ? "未认证" : realName);
						}
						else {
							idStatus.setText("(未认证)");
							tel.setText(user.getPhone());
						}
						status = account.getCardStatus();
						refreshView();
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
