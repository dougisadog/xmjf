package com.nangua.xiaomanjflc.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.InfoManager;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.support.InfoManager.TaskCallBack;
import com.nangua.xiaomanjflc.ui.fragment.DetailFragmentIntroduction;
import com.nangua.xiaomanjflc.ui.fragment.DetailFragmentTender;
import com.nangua.xiaomanjflc.ui.myabstract.HomeFragment;
import com.nangua.xiaomanjflc.ui.myabstract.VerticalScrollFragment.ScollCallBack;
import com.nangua.xiaomanjflc.utils.CommonUtils;
import com.nangua.xiaomanjflc.utils.FormatUtils;
import com.nangua.xiaomanjflc.utils.ToastUtil;
import com.nangua.xiaomanjflc.widget.LoudingDialogIOS;
import com.nangua.xiaomanjflc.widget.TenderVerticalViewPager;
import com.nangua.xiaomanjflc.adapter.fragment.HomeFragmentAdapter;
import com.nangua.xiaomanjflc.bean.DetailProduct;
import com.nangua.xiaomanjflc.bean.Product;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.cache.CacheBean;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TenderActivity extends KJActivity {


	private static final String LOGTAG = "TenderActivity";
	
	private TenderVerticalViewPager vp;
	
	private List<HomeFragment> homeFragments;
	
	private TextView mBuy;
	
	private HomeFragmentAdapter adapter;
	
	private int id;
	private KJHttp http;
	private HttpParams params;
	
	private DetailProduct dProduct;
	
	public static int REQUEST_RED = 1;
	public static int REQUEST_SIGNIN = 99;
	
	private int confine; //专享类型
	private int status; //标状态 5为正在售卖
	
	private Product product;
	
	//TODO 滚动回调 在优化后可舍弃 此处在最终执行处注释掉 详见BaseTextFragment和ProductRecordFragment，以后可酌情吧全部相关回调删除
	private ScollCallBack sc = new ScollCallBack() {
		
		@Override
		public void onScrollTop() {
			vp.setCurrentItem(0);
		}
		
		@Override
		public void onScrollBottom() {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void changePage(int page) {
		vp.setCurrentItem(page);
	}
	
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_tender_v2);
		UIHelper.setTitleView(this, "", "项目详情");
		vp = (TenderVerticalViewPager) findViewById(R.id.vp);
		mBuy = (TextView) findViewById(R.id.buy);
		
		//TODO DELETE 测试用
//		AppVariables.newHand = 0;
		
		//监听键盘隐藏投资按钮防止遮挡EditText
		LinearLayout mainContent = (LinearLayout) findViewById(R.id.mainContent);
		mainContent.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			
			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom,
					int oldLeft, int oldTop, int oldRight, int oldBottom) {
				if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > 0)){
					mBuy.setVisibility(View.INVISIBLE);
		          
		        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > 0)){  
		              
		        	mBuy.setVisibility(View.VISIBLE);
		        }  
				
			}
		}); 
		homeFragments = new ArrayList<HomeFragment>();
		adapter = new HomeFragmentAdapter(getSupportFragmentManager(), homeFragments);
		vp.setAdapter(adapter);
		CommonUtils.controlViewPagerSpeed(this, vp, 500);//设置你想要的时间
		product = (Product) getIntent().getSerializableExtra("product");
		if (null == product) {
			finish();
			return;
		}
		confine = product.getConfine();
		id = product.getId();
		status = product.getNewstatus();
		
		http = new KJHttp();
		params = new HttpParams();
		getData();
		refreshBuyBtn();
		mBuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!AppVariables.isSignin) {
					Intent intent = new Intent(TenderActivity.this, SigninActivity.class);
					startActivityForResult(intent, TenderActivity.REQUEST_SIGNIN);
					return;
				}
				User user = CacheBean.getInstance().getUser();
				if (null == user) {
					InfoManager.getInstance().getInfo(TenderActivity.this, new TaskCallBack() {
						
						@Override
						public void taskSuccess() {
							User cUser = CacheBean.getInstance().getUser();
							if (null == cUser) {
								Toast.makeText(TenderActivity.this, "用户信息拉取异常", Toast.LENGTH_LONG).show();
							}
							else {
								checkUser(CacheBean.getInstance().getUser());
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
				else {
					checkUser(user);
				}
			}
		});

	}
	
	private void checkUser(User user) {
		//企业
		if (user.getType() == 1) {
			ToastUtil.showToast(TenderActivity.this, "企业用户不能投资",Toast.LENGTH_SHORT);
			return;
		}
		if (status != 5) return;
		vp.setCurrentItem(0);
		if (adapter.getCount() >0) {
			DetailFragmentTender fragment = (DetailFragmentTender) adapter.getItem(0);
			fragment.getInfo();
		}
	}
	
	/**
	 * 刷新购买按钮状态
	 */
	private void refreshBuyBtn() {
		int i = status;
		String[] arr = getResources().getStringArray(R.array.product_status);
		// 使用预约时间开始时间替换 原预约2字
		String content = "";
		if (i == 3 && null != product.getFinanceStartDate()) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm开始");
			content = format.format(product.getFinanceStartDate());
//			mBuy.setTextSize(10);
			mBuy.setText(content);
//			mBuy.setTextColor(getResources().getColor(R.color.white));
		}
		else if (i != 5) {
			mBuy.setText(arr[i]);
//			mBuy.setTextColor(getResources().getColor(R.color.grey));
		}
		mBuy.setBackgroundResource(i == 5 ? R.color.orange : R.color.grey_btn);
	}
	
	/**
	 * @return true 新手标且不为新手
	 */
	public boolean  checkNewHand() {
		return (confine == 1 || confine == 6 ) && AppVariables.newHand != 0;
	}
	
	private void getData() {
		 getData(httpCallback);
	}
	
	private void getData(HttpCallBack callback) {
		params.put("sid", AppVariables.sid);
		params.put("id", id);
		http.post(AppConstants.DETAIL_PRODUCT + id, params, callback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(this) {
		public void success(org.json.JSONObject ret) {
			try {
				dProduct = new DetailProduct(ret);
				CacheBean.getInstance().setProduct(ret);
				homeFragments.clear();
				homeFragments.add(new DetailFragmentTender());
				homeFragments.add(new DetailFragmentIntroduction(dProduct, sc));
				adapter.setList(homeFragments);
				adapter.getItem(0).refreshData();
				showNewHandTips();
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(TenderActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public void showNewHandTips() {
		if (AppVariables.isSignin && checkNewHand()) {
			LoudingDialogIOS ld = new LoudingDialogIOS(TenderActivity.this);
			ld.showConfirmHint("新手标只能投资一次");
			mBuy.setBackgroundResource(R.color.grey_btn);
			mBuy.setEnabled(false);
		}
	}


	// 当插件调用完毕后返回时执行该方法
	protected void onNewIntent(Intent intent) {
		AppVariables.forceUpdate = true;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			// 更新数据
			//TODO 页面返回刷新逻辑
			if (confine == 1 || confine == 6 ) {
				AppVariables.newHand = 1;
				showNewHandTips();				
			}
			getData(new HttpCallBack(this) {
				public void success(org.json.JSONObject ret) {
					try {
						dProduct = new DetailProduct(ret);
						CacheBean.getInstance().setProduct(ret);
						DetailFragmentTender dft = (DetailFragmentTender) adapter.getItem(0);
						dft.refreshData();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(TenderActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
					}
				}
			});
			

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//现金券
		if (requestCode == REQUEST_RED && resultCode == AppConstants.SUCCESS) {
			int cashid = 0;
			int cash_price = 0;
			if (data != null) {
				cashid = data.getIntExtra("cash", 0);
				cash_price = data.getIntExtra("price", 0);
			}
			//TODO 
			DetailFragmentTender dft = (DetailFragmentTender) adapter.getItem(0);
			dft.refreshCashRed(cashid, cash_price);
		}
		//登录
		else if (requestCode == REQUEST_SIGNIN && resultCode == AppConstants.SUCCESS) {
			getData(new HttpCallBack(this) {
				public void success(org.json.JSONObject ret) {
					try {
						dProduct = new DetailProduct(ret);
						CacheBean.getInstance().setProduct(ret);
						adapter.getItem(0).refreshData();
						status = dProduct.getNewstatus();
						refreshBuyBtn();
						if (AppVariables.isSignin && checkNewHand()) {
							LoudingDialogIOS ld = new LoudingDialogIOS(TenderActivity.this);
							ld.showConfirmHint("新手标只能投资一次");
							mBuy.setBackgroundResource(R.color.grey_btn);
							mBuy.setEnabled(false);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(TenderActivity.this, R.string.app_data_error, Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}

	
}
