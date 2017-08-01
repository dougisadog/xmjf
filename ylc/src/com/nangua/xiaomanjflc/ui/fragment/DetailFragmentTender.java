package com.nangua.xiaomanjflc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import m.framework.utils.Utils;
import android.view.ViewGroup;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.ips.p2p.StartPluginTools;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.KJLoger;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.ui.AccountActivity;
import com.nangua.xiaomanjflc.ui.CashActivity;
import com.nangua.xiaomanjflc.ui.ChargeActivity;
import com.nangua.xiaomanjflc.ui.SettingActivity;
import com.nangua.xiaomanjflc.ui.SigninActivity;
import com.nangua.xiaomanjflc.ui.TenderActivity;
import com.nangua.xiaomanjflc.ui.TenderProtocolActivity;
import com.nangua.xiaomanjflc.ui.UseRedActivity;
import com.nangua.xiaomanjflc.ui.myabstract.HomeFragment;
import com.nangua.xiaomanjflc.utils.FormatUtils;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.umeng.analytics.MobclickAgent;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.DetailProduct;
import com.nangua.xiaomanjflc.bean.Product;
import com.nangua.xiaomanjflc.bean.jsonbean.Account;
import com.nangua.xiaomanjflc.bean.jsonbean.GainData;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.cache.CacheBean;

public class DetailFragmentTender extends HomeFragment{
	
	// 产品详情
	private TextView name;
	private TextView totalInvestment;
	private TextView totalInvestmentunit;
	private TextView investmentPeriodDesc;
	private TextView investmentPeriodDescunit;
	private TextView annualizedGain;
	private TextView percentIcon;
	private TextView guaranteeModeName;
	private TextView repaymentMethodName;
	private TextView expirationDate;
	private TextView remainingInvestmentAmount;
	private TextView singlePurchaseLowerLimit;
	private TextView percentagetxt;
	private ProgressBar percentagepb;
	
	private TextView confine;

	private LinearLayout tender_charge;
	private LinearLayout llUserRed;
	private EditText mPrice;
	private TextView cash_state;
	private TextView protocol;
	private TextView cash_use;
	private TextView mAvaliable;
	private TextView cash_discount;
	private TextView pay;
	private ImageView mCheckbox;

	private LinearLayout add_v;
	private TextView add;
	
	
	private KJHttp http;
	private HttpParams params;
	private DetailProduct product;
	private int id;
	private int mul;
	private int max;
	private int min;
	private int available = 0;
	private int price;
	private int cashid = 0;
	private int cash_price = 0;
	private int cash_count;
	private int cash_sum;
	private String url;
	private String agreement;// 我同意内容
	private String products_type;// 产品类型 01:融资产品,02:债权产品
	private boolean checkbox = true;

	private int idValidated = 0;
	private int status;
	private boolean isCharge;

	private String orderId;
	
	private Product oProduct;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initialed = true;
		
		View v = inflater.inflate(R.layout.fragment_detail_tender, null);
		bindView(v);
		
//		Intent intent = getActivity().getIntent();
//		id = intent.getIntExtra("id", 0);
//		price = intent.getIntExtra("price", 0);
		oProduct = (Product) getActivity().getIntent().getSerializableExtra("product");
		id = oProduct.getId();
//		price = oProduct.get
		http = new KJHttp();
		params = new HttpParams();
		
		return v;
	}
	
	private void bindView(View v) {
		name = (TextView) v.findViewById(R.id.name);
		totalInvestment = (TextView) v.findViewById(R.id.totalInvestment);
		totalInvestmentunit = (TextView) v.findViewById(R.id.totalInvestmentunit);
		investmentPeriodDesc = (TextView) v.findViewById(R.id.investmentPeriodDesc);
		investmentPeriodDescunit = (TextView) v.findViewById(R.id.investmentPeriodDescunit);
		annualizedGain = (TextView) v.findViewById(R.id.annualizedGain);
		percentIcon = (TextView) v.findViewById(R.id.percentIcon);
		guaranteeModeName = (TextView) v.findViewById(R.id.guaranteeModeName);
		repaymentMethodName = (TextView) v.findViewById(R.id.repaymentMethodName);
		expirationDate = (TextView) v.findViewById(R.id.expirationDate);
		remainingInvestmentAmount = (TextView) v.findViewById(R.id.remainingInvestmentAmount);
		singlePurchaseLowerLimit = (TextView) v.findViewById(R.id.singlePurchaseLowerLimit);
		percentagetxt = (TextView) v.findViewById(R.id.percentagetxt);
		percentagepb = (ProgressBar) v.findViewById(R.id.percentagepb);
		llUserRed = (LinearLayout) v.findViewById(R.id.llUserRed);
		llUserRed.setOnClickListener(listener);
		
		confine = (TextView) v.findViewById(R.id.confine);
		//TODO 暂定隐藏
//		confine.setVisibility(View.GONE);
		
		tender_charge = (LinearLayout) v.findViewById(R.id.tender_charge);
		tender_charge.setOnClickListener(listener);
		mPrice = (EditText) v.findViewById(R.id.price);
		
		cash_state = (TextView) v.findViewById(R.id.cash_state);
		cash_state.setOnClickListener(listener);
		protocol = (TextView) v.findViewById(R.id.protocol);
		protocol.setOnClickListener(listener);
		cash_use = (TextView) v.findViewById(R.id.cash_use);
		mAvaliable = (TextView) v.findViewById(R.id.available);
		cash_discount = (TextView) v.findViewById(R.id.cash_discount);
		cash_discount.setOnClickListener(listener);
		pay = (TextView) v.findViewById(R.id.pay);
		mCheckbox = (ImageView) v.findViewById(R.id.checkbox);
		mCheckbox.setOnClickListener(listener);
		add_v = (LinearLayout) v.findViewById(R.id.add_v);
		add = (TextView) v.findViewById(R.id.add);
		refreshPriceListener();
		
		
	}
	
	
	private OnTouchListener onTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN :
					if (!AppVariables.isSignin) {
						Intent intent = new Intent(getActivity(), SigninActivity.class);
						getActivity().startActivityForResult(intent, TenderActivity.REQUEST_SIGNIN);
					}
					break;

				default :
					break;
			}
			return false;
		}
	};
	/**
	 * 刷新金额输入框监听
	 */
	private void refreshPriceListener() {
		if (!AppVariables.isSignin) {
			//禁止键盘弹出
			mPrice.setInputType(InputType.TYPE_NULL);
			//此处弃用OnClickListener 因为OnTouchListener的响应效果较好 通过ACTION_DOWN 从而值响应一次
			mPrice.setOnTouchListener(onTouchListener);
		}
		else {
			mPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
			mPrice.setOnClickListener(null);
		}
	}
	
	/**
	 * 刷新现金券状态
	 * @param cashid
	 * @param cash_price
	 */
	public void refreshCashRed(int cashid, int cash_price) {
		this.cashid= cashid;
		this.cash_price = cash_price;
		if (cashid == 0) {
			cash_discount.setText("0元");
			int p = 0;
			String sp = mPrice.getText().toString();
			if (!Utils.isNullOrEmpty(sp)) {
				p = Integer.parseInt(sp);
			}
			pay.setText(p + "元");
		} else {
			cash_discount.setText(cash_price + "元");
			int p = 0;
			String sp = mPrice.getText().toString();
			if (!Utils.isNullOrEmpty(sp)) {
				p = Integer.parseInt(sp);
			}
			pay.setText((p - cash_price) + "元");
		}
	}

	private void initView() {
		protocol.setText(agreement);
		// 产品详情
		name.setText(product.getName());
		
		//普通activity 中的product数据
//		int confineNum = getActivity().getIntent().getIntExtra("confine", 0);
		int confineNum = oProduct.getConfine();
		switch (confineNum) {
		case 0:
			confine.setVisibility(View.GONE);
			break;
		case 1:
			confine.setText("新手");
			break;
		case 2:
			confine.setText("手机专享");
			break;
		case 3:
			confine.setText("活动专享");
			break;
		case 4:
			confine.setText("活动专享");
			break;
		case 5:
			confine.setText("VIP专享");
			break;
		case 6:
			confine.setText("新手推荐");
			break;
		case 7:
			confine.setText("推荐");
			break;
		case 8:
			confine.setText("手机推荐");
			break;
		}
		
		totalInvestment.setText(product.getTotalInvestment());
		totalInvestmentunit.setText(product.getTotalInvestmentunit());
		investmentPeriodDesc.setText(product.getInvestmentPeriodDesc());
		investmentPeriodDescunit.setText(product.getInvestmentPeriodDescunit());
		if (!"".equals(product.getTenderAward())) {
			add_v.setVisibility(View.VISIBLE);
			add.setText(product.getTenderAward());
			
			percentIcon.setVisibility(View.GONE);
//			int size = getResources().getDimensionPixelSize(R.dimen.font_app);
//			annualizedGain.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
//			add.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
		else {
			percentIcon.setVisibility(View.VISIBLE);
			add_v.setVisibility(View.GONE);
		}

		annualizedGain.setText(product.getAnnualizedGain());
		guaranteeModeName.setText(product.getGuaranteeModeName());
		repaymentMethodName.setText(product.getRepaymentMethodName());
		//TODO product.enday
		expirationDate.setText(product.getEndDay() + "截止");
		remainingInvestmentAmount.setText(product.getRemainingInvestmentAmount());
		singlePurchaseLowerLimit.setText(product.getSinglePurchaseLowerLimit());
		percentagetxt.setText(product.getInvestmentProgress() + "%");
		percentagepb.setProgress(product.getInvestmentProgress());
		mCheckbox.setImageResource(R.drawable.checkbox);

		mPrice.setHint("输入金额为" + mul + "的整数倍");

		if (cash_count > 0) {
			cash_state.setText("现金券使用(共" + cash_count + "张可用)");
			cash_use.setText("使用");
			cash_use.setTextColor(getResources().getColor(R.color.orange));
		} else {
			cash_state.setText("现金券使用（无可用）");
			cash_use.setText("使用");
			cash_use.setTextColor(getResources().getColor(R.color.app_bg));
		}
		mCheckbox.setImageResource(R.drawable.checkbox);
		
		refreshEditStatus();
		mPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			/**
			 * 限制输入长度
			 */
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				Editable editable = mPrice.getText();
				// 输入字符
				String str = editable.toString();
				if (Utils.isNullOrEmpty(str)) {
					pay.setText((0L - (long) cash_price) + "元");
					cash_discount.setText(cash_price + "元");
					return;
				}
				if (str.startsWith("0")) {
					LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
					ld.showConfirmHint("请输入至少100元");
					mPrice.setText("");
					return;
				}
				pay.setText((Utils.isNullOrEmpty(str) ? 0L : (Long.parseLong(str) - (long) cash_price)) + "元");
				cash_discount.setText(cash_price + "元");
				editable = mPrice.getText();

				// 新字符串的长度
				int newLen = editable.length();
				// 设置新光标所在的位置
				Selection.setSelection(editable, newLen);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!AppVariables.isSignin) {
				Intent intent = new Intent(getActivity(), SigninActivity.class);
				getActivity().startActivityForResult(intent, TenderActivity.REQUEST_SIGNIN);
				return;
			}
			switch (v.getId()) {
				case R.id.tender_charge:
					if (idValidated == 1 && status == 2) {
						Intent charge = new Intent(getActivity(), ChargeActivity.class);
						charge.putExtra("balance", available);
						startActivity(charge);
					} else {
						isCharge = true;
						getInfo1();
					}
					break;
				case R.id.llUserRed:
					if (cash_count < 1) {
						break;
					}
					String p = mPrice.getText().toString();
					if (StringUtils.isEmpty(p)) {
						LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
						ld.showConfirmHint("请输入金额。");
						break;
					}
					if (Integer.parseInt(p) % mul > 0) {
						LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
						ld.showConfirmHint("请输入" + mul + "的整数倍");
						break;
					}
					Intent intent = new Intent(getActivity(), UseRedActivity.class);
					intent.putExtra("amount", Integer.parseInt(p));
					intent.putExtra("productid", id);
					intent.putExtra("cashid", cashid);
					getActivity().startActivityForResult(intent, TenderActivity.REQUEST_RED);
					break;
				case R.id.protocol:
					Intent protocol = new Intent(getActivity(), TenderProtocolActivity.class);
					protocol.putExtra("pid", id);
					protocol.putExtra("products_type", products_type);
					String amt = mPrice.getText().toString();
					if (StringUtils.isEmpty(amt)) {
						final LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
						ld.showConfirmHint("请输入金额。");
						break;
					}
					protocol.putExtra("amt", Integer.parseInt(amt));
					startActivity(protocol);
					break;
				case R.id.checkbox:
					if (checkbox) {
						checkbox = false;
						mCheckbox.setImageResource(R.drawable.checkbox_none);
						
					} else {
						checkbox = true;
						mCheckbox.setImageResource(R.drawable.checkbox);
					}
					break;
				}
		}
	};
	
	/**
	 * 本地数据验证
	 * @return
	 */
	private boolean tenderValidtation() {
		LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
		if (null == product) {
			ld.showConfirmHint("当前产品数据拉取失败。");
			return false;
		}
		String p = mPrice.getText().toString();
		if (!checkbox) {
			ld.showConfirmHint("请先同意相关协议。");
			return false;
		}
		if (StringUtils.isEmpty(p)) {
			ld.showConfirmHint("请输入金额");
			return false;
		}
		int price = Integer.parseInt(p);
		if (price > max) {
			ld.showConfirmHint("输入的金额超过可投金额，请重新输入！");
			return false;
		} else if (price < min) {
			ld.showConfirmHint("请大于最小投资金额");
			return false;
		} else if ((price % mul) > 0) {
			ld.showConfirmHint("请输入" + mul + "的整数倍");
			return false;
		}
		ld.dismiss();
		return true;
	}
	
	/**
	 * 开启环迅插件支付
	 * 
	 * @param server返回的支付信息json
	 */
	private void biddingAction(JSONObject ret) {
		try {
			Bundle bundle = new Bundle();
			bundle.putString("operationType", ret.getString("operationType"));
			bundle.putString("merchantID", ret.getString("merchantID"));
			bundle.putString("sign", ret.getString("sign"));
			bundle.putString("request", ret.getString("request"));
			StartPluginTools.start_p2p_plugin(StartPluginTools.BIDDING, getActivity(), bundle, AppConstants.IPS_VERSION);
			
			//Umeng事件统计
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("orderId",orderId); //订单号
			map.put("cashId",cashid + ""); //现金券id
			MobclickAgent.onEvent(getActivity(), StartPluginTools.BIDDING, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getInfo() {
		if (!tenderValidtation()) return;
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.GAIN, params, new HttpCallBack(getActivity()) {
			@Override
			public void onSuccess(String t) {
				KJLoger.debug(t);
				try {
					JSONObject ret = new JSONObject(t);
					available = ret.getInt("available");
					String p = mPrice.getText().toString();
					int price = Integer.parseInt(p);
					if (price - cash_price > available/100) {
						LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
						ld.showConfirmHint("可用余额不足,请先充值。");
						return;
					}
					params.put("sid", AppVariables.sid);
					params.put("id", id);
					params.put("amount", price / mul);
					params.put("cash", cashid);
					//购买回调
					http.post(AppConstants.BUY + id + "/order/pay", params, new HttpCallBack(getActivity(), false) {
						@Override
						public void success(JSONObject ret) {
							try {
								url = ret.getString("url");
								String[] as = url.split("/");
								for (int i = 0; i < as.length; i++) {
									KJLoger.debug(i + "===>" + as[i]);
								}
								orderId = as[2];
								params = new HttpParams();
								params.put("sid", AppVariables.sid);
								params.put("id", as[2]);
								
								//当没有插件时，不进行代金劵的锁定
								int r = StartPluginTools.getStateOfAppInstallation(getActivity().getPackageManager(), AppConstants.IPS_PACKAGE_NAME, AppConstants.IPS_VERSION);
								if (r == StartPluginTools.PLUGIN_IS_AVAILABLE) {
									//进入环迅插件前 锁定该代金劵
									params.put("cash", cashid);
								}
								//投资参数回调
								http.post(AppConstants.HOST + url, params, new HttpCallBack(getActivity()) {
									@Override
									public void success(JSONObject ret) {
										try {
											if ("ips".equals(ret.getString("pay.provider"))) {
												biddingAction(ret);
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void getInfo1() {

		InfoManager.getInstance().getInfo(getActivity(), new TaskCallBack() {

			@Override
			public void taskSuccess() {
				User user = CacheBean.getInstance().getUser();

				idValidated = user.getIdValidated();
				if (idValidated == 1) {
						Account account = CacheBean.getInstance().getAccount();
						status = account.getCardStatus();
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
							Intent i = new Intent(getActivity(), ChargeActivity.class);
							i.putExtra("balance", available);
							startActivity(i);
						}
				} else {
					final LoudingDialogIOS ld = new LoudingDialogIOS(getActivity());
					ld.showOperateMessage("请先实名认证。");
					ld.setPositiveButton("前往", R.drawable.dialog_positive_btn, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(), AccountActivity.class));
							ld.dismiss();
						}
					});
				}
			}

			@Override
			public void taskFail(String err, int type) {
			}

			@Override
			public void afterTask() {
			}
		});

	}
	
	
	private void getAvailable() {
		//未登录
		if (AppVariables.isSignin == false) {
			mAvaliable.setText("请先登录");
			mAvaliable.setOnClickListener(listener);
			return;
		}
		mAvaliable.setOnClickListener(null);
		GainData gainData = CacheBean.getInstance().getGainData();
		if (null != gainData) {
			refreshGainData(gainData);
			return;
		}
		params.put("sid", AppVariables.sid);
		//获取账户信息 刷新余额
		http.post(AppConstants.GAIN, params, new HttpCallBack(getActivity(), false) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					GainData gainData = FormatUtils.jsonParse(ret.toString(), GainData.class);
					CacheBean.getInstance().setGainData(gainData);
					refreshGainData(gainData);
//					AppVariables.forceUpdate = true;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 刷新可用余额相关
	 * @param gainData
	 */
	private void refreshGainData(GainData gainData) {
		if (null == gainData) return;
		available = gainData.getAvailable();
		mAvaliable.setText(FormatUtils.fmtMicrometer(available / 100 + "") + "." + available % 100 / 10
				+ available % 10 + "元");
	}
	
	private void refreshEditStatus() {
		if (AppVariables.isSignin && ((TenderActivity)getActivity()).checkNewHand()) {
			mPrice.setEnabled(false);
			protocol.setEnabled(false);
		}
		else if (oProduct.getNewstatus() != 5) {
			mPrice.setEnabled(false);
			protocol.setEnabled(false);
		}
		else {
			mPrice.setEnabled(true);
			protocol.setEnabled(true);
		}
	}
	

	/**
	 * 刷新基础数据 重置UI
	 */
	public void refreshData() {
		if (!initialed) return;
		refreshPriceListener();
		refreshEditStatus();
		try {
			JSONObject ret = CacheBean.getInstance().getProduct();
			if (null != ret) {
				product = new DetailProduct(ret);
				cash_count = ret.getInt("cashCount");
				cash_sum = ret.getInt("cashSum");
				JSONObject p = ret.getJSONObject("product");
				agreement = p.getString("agreement");
				products_type = p.getString("products_type");
				mul = (p.getInt("baseLimitAmount")) / 100;
				max = Integer.parseInt(p.getString("remainingInvestmentAmount")) / 100;
				min = Integer.parseInt(p.getString("singlePurchaseLowerLimit")) / 100;
				initView();
				getAvailable();
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
