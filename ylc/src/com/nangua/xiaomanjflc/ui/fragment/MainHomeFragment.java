package com.nangua.xiaomanjflc.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.DensityUtils;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.adapter.AnnoWheelAdapter;
import com.nangua.xiaomanjflc.bean.ADCycleItem;
import com.nangua.xiaomanjflc.bean.Announce;
import com.nangua.xiaomanjflc.bean.AnnounceList;
import com.nangua.xiaomanjflc.bean.CycleData;
import com.nangua.xiaomanjflc.bean.Product;
import com.nangua.xiaomanjflc.bean.ProductList;
import com.nangua.xiaomanjflc.bean.ShopADData;
import com.nangua.xiaomanjflc.bean.ShopADData.ShopADType;
import com.nangua.xiaomanjflc.bean.database.UPushMessage;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.dialog.DialogAlertFragment;
import com.nangua.xiaomanjflc.dialog.DialogAlertFragment.CallBackDialogConfirm;
import com.nangua.xiaomanjflc.dialog.DialogConfirmFragment;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.ui.AboutListActivity;
import com.nangua.xiaomanjflc.ui.AnnoActivity;
import com.nangua.xiaomanjflc.ui.GuardActivity;
import com.nangua.xiaomanjflc.ui.NewHandActivity;
import com.nangua.xiaomanjflc.ui.NormalInviteActivity;
import com.nangua.xiaomanjflc.ui.SigninActivity;
import com.nangua.xiaomanjflc.ui.TenderActivity;
import com.nangua.xiaomanjflc.ui.myabstract.HomeFragment;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.utils.FormatUtils;
import com.nangua.xiaomanjflc.widget.MutiCycleViewHome;
import com.nangua.xiaomanjflc.widget.SmartScrollView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment1;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import wheel.WheelView;

public class MainHomeFragment extends HomeFragment{
	
	private LinearLayout adContainer;
	private LinearLayout adContainerBottom;
	private LinearLayout anno;
	private ImageView pushMsg;
	private MutiCycleViewHome mcv;
	
	private MutiCycleViewHome mcvBottom;
	
	private ImageView pushImg;
	
	private WheelView wv;
	
	private LinearLayout llNewHand;//新手
	private LinearLayout llInvite; //邀请
	private LinearLayout llGuard; //安全保障
	
	
	private TextView tip1; //首标提示语
	private TextView annualizedGain1;//收益率
	private TextView annualizedGain2;
	private TextView annualizedGain3;
	
	private TextView percentIcon1;//收益率
	private TextView percentIcon2;
	private TextView percentIcon3;
	
	private TextView remainingInvestmentAmount1; //可投金额
	private TextView remainingInvestmentAmount2;
	private TextView remainingInvestmentAmount3;
	
	private TextView limitTime1; //期限
	private TextView limitTime2;
	private TextView limitTime3;
	
	private TextView txtAvailable1; //可投金额文字
	private TextView txtAvailable2;
	private TextView txtAvailable3;
	
	
	private LinearLayout add_v1; //额外加息区域
	private LinearLayout add_v2;
	private LinearLayout add_v3;
	
	private TextView add1; //额外加息值
	private TextView add2;
	private TextView add3;
	
	private TextView btn; //首标投资btn
	
	private RelativeLayout product1; //商品
	private LinearLayout product2;
	private LinearLayout product3;
	
	private LinearLayout llFunc; 
	
	private SmartScrollView sv; //垂直滚动条
	
	private KJHttp http;
	
	private List<Product> products = new ArrayList<Product>();
	private List<Announce> announces;
	
//	private int new_hand = 0;
	
	private int adHeight;
	private int adBottomHeight;
	
	private static int TOP = 0;
	private static int BOTTOM = 1;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initialed = true;
		
		View v = inflater.inflate(R.layout.fragment_main_home, null);
		adContainer = (LinearLayout) v.findViewById(R.id.adContainer);
		adContainerBottom = (LinearLayout) v.findViewById(R.id.adContainerBottom);
		anno = (LinearLayout) v.findViewById(R.id.anno);
		anno.setOnClickListener(listener);
		pushMsg = (ImageView) v.findViewById(R.id.pushImg);
		pushMsg.setOnClickListener(listener);
		
		pushImg = (ImageView) v.findViewById(R.id.pushImg);
		
		wv = (WheelView) v.findViewById(R.id.wv1);
		wv.setDrawShadows(false);
		wv.setWheelBackground(0);
		wv.setWheelForeground(R.drawable.wheel_val_none);
		wv.setVisibleItems(7);
		wv.setScollTime(500);
		//舍弃滑动 通过优先的touch来完成效果较好的点击事件
		wv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN :
						//TODO 暂定改为全部进入列表
						
//						if (null == announces) return true;
//						int itemIndex = wv.getCurrentItem();
//						Announce announce = null;
//						if (itemIndex < announces.size()) {
//							announce = announces.get(itemIndex);
//						}
//						else if (itemIndex == announces.size()) {
//							announce = announces.get(0);
//						}
//						Intent intent = new Intent(getActivity(),AnnoContentActivity.class);
//						intent.putExtra("title", announce.getTitle());
//						intent.putExtra("data", announce.getDateline());
//						intent.putExtra("content", announce.getContent());
//						startActivity(intent);
						AnnoActivity.startActivity(getActivity(), announces);
						return true;
					default :
						break;
				}
				return false;
			}
		});
//		wv.addClickingListener(new OnWheelClickedListener() {
//			
//			@Override
//			public void onItemClicked(WheelView wheel, int itemIndex) {
//				if (itemIndex >= announces.size()) return;
//				Announce announce = announces.get(itemIndex);
//				Intent intent = new Intent(getActivity(),AnnoContentActivity.class);
//				intent.putExtra("title", announce.getTitle());
//				intent.putExtra("data", announce.getDateline());
//				intent.putExtra("content", announce.getContent());
//				startActivity(intent);
//				
//			}
//		});
		llNewHand = (LinearLayout) v.findViewById(R.id.llNewHand);
		llNewHand.setOnClickListener(listener);
		llInvite = (LinearLayout) v.findViewById(R.id.llInvite);
		llInvite.setOnClickListener(listener);
		llGuard = (LinearLayout) v.findViewById(R.id.llGuard);
		llGuard.setOnClickListener(listener);
		
		tip1 = (TextView) v.findViewById(R.id.tip1);
		
		annualizedGain1 = (TextView) v.findViewById(R.id.annualizedGain1);
		annualizedGain2 = (TextView) v.findViewById(R.id.annualizedGain2);
		annualizedGain3 = (TextView) v.findViewById(R.id.annualizedGain3);
		
		remainingInvestmentAmount1 = (TextView) v.findViewById(R.id.remainingInvestmentAmount1);
		remainingInvestmentAmount2 = (TextView) v.findViewById(R.id.remainingInvestmentAmount2);
		remainingInvestmentAmount3 = (TextView) v.findViewById(R.id.remainingInvestmentAmount3);
		
		limitTime1 = (TextView) v.findViewById(R.id.limitTime1);
		limitTime2 = (TextView) v.findViewById(R.id.limitTime2);
		limitTime3 = (TextView) v.findViewById(R.id.limitTime3);
		
		txtAvailable1 = (TextView) v.findViewById(R.id.txtAvailable1);
		txtAvailable2 = (TextView) v.findViewById(R.id.txtAvailable2);
		txtAvailable3 = (TextView) v.findViewById(R.id.txtAvailable3);
		
		percentIcon1 = (TextView) v.findViewById(R.id.percentIcon1);
		percentIcon2 = (TextView) v.findViewById(R.id.percentIcon2);
		percentIcon3 = (TextView) v.findViewById(R.id.percentIcon3);
		
		add_v1 = (LinearLayout) v.findViewById(R.id.add_v1);
		add_v2 = (LinearLayout) v.findViewById(R.id.add_v2);
		add_v3 = (LinearLayout) v.findViewById(R.id.add_v3);
		
		add1 = (TextView) v.findViewById(R.id.add1);
		add2 = (TextView) v.findViewById(R.id.add2);
		add3 = (TextView) v.findViewById(R.id.add3);
		
		product1 = (RelativeLayout) v.findViewById(R.id.product1);
		product1.setOnClickListener(listener);
		product2 = (LinearLayout) v.findViewById(R.id.product2);
		product2.setVisibility(View.GONE);
		product3 = (LinearLayout) v.findViewById(R.id.product3);
		product3.setVisibility(View.GONE);
		
		llFunc = (LinearLayout) v.findViewById(R.id.llFunc);
		sv = (SmartScrollView) v.findViewById(R.id.sv);
		
		
		btn = (TextView) v.findViewById(R.id.btn);
		
		int width = ApplicationUtil.getApkInfo(getActivity()).width;
		adHeight = (int) (width /AppConstants.BANNER_SCALE);
		adBottomHeight = (int) (width /AppConstants.BANNER_SCALE_BOTTOM);
		
		initLayout();
		initView();
		initData();
		return v;
	}
	
	/**
	 * 初始化各部分高度 
	 */
	private void initLayout() {
		int annoHeight = DensityUtils.dip2px(getActivity(), 40);
		int product1Height = DensityUtils.dip2px(getActivity(), 220);
		int bottomTab = DensityUtils.dip2px(getActivity(), 60);//176  mainactivity下方tab
		int lines = DensityUtils.dip2px(getActivity(), 10);// 灰线高度补正
		int topStatus = UIHelper.getStatusHeight(getActivity());// 通知栏补正
		int llfuncHeight = ApplicationUtil.getApkInfo(getActivity()).height - adHeight - annoHeight - product1Height - bottomTab - topStatus - lines;
		//小屏手机适配
		if (llfuncHeight < 150) {
			llfuncHeight = LayoutParams.WRAP_CONTENT;
		}
		//高度设置方式2
//		int targetHeight = ApplicationUtil.getApkInfo(getActivity()).height/6;
//		if (llfuncHeight < targetHeight) {
//			if (targetHeight > 180) {
//				llfuncHeight = targetHeight;
//			}
//			else {
//				llfuncHeight = LayoutParams.WRAP_CONTENT;
//			}
//		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, llfuncHeight);
		llFunc.setLayoutParams(params);
//		llFunc.setPadding(0, 0, 0, DensityUtils.dip2px(getActivity(), 20));
		//高度监测
//				final ViewTreeObserver vto = sv.getViewTreeObserver();
//				vto.addOnPreDrawListener(new OnPreDrawListener() {
//					
//					@Override
//					public boolean onPreDraw() {
//						int annoHeight = anno.getHeight();
//						int product1Height = product1.getHeight();
//						int llfuncHeight = llFunc.getHeight();
//						int ah = mcv.getHeight();
//						getActivity().findViewById(R.id.tabwidget).getHeight();
//						return true;
//					}
//				});
	}

	/**
	 * 全部数据加载
	 */
	private void initData() {
		loadCycleADDatas(mcv, TOP);
		loadCycleADDatas(mcvBottom, BOTTOM);
		loadRecommendedProducts();
		loadAnnos();
	}
	
	
	
	@Override
	public void onResume() {
		refreshPushMsg();
		super.onResume();
	}

	/**
	 * 刷新推送消息标识
	 */
	private void refreshPushMsg() {
		UPushMessage msg = CacheBean.getInstance().getMsg();
		if (msg != null && msg.getShowed() == 0) {
			pushImg.setImageResource(R.drawable.icon_news);
			pushImg.setVisibility(View.VISIBLE);
		}
		else {
//			pushImg.setImageResource(0);
			pushImg.setVisibility(View.GONE);
		}
	}

	private void initView() {
		LinearLayout.LayoutParams params = new   LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,adHeight);
		
		mcv = new MutiCycleViewHome(getActivity());
		mcv.setLayoutParams(params);
		adContainer.addView(mcv);
		
		LinearLayout.LayoutParams params2 = new   LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,adBottomHeight);
		
		mcvBottom = new MutiCycleViewHome(getActivity());
		mcvBottom.setPointResourceBlur(0);
		mcvBottom.setPointResourceFocus(0);
		
		mcvBottom.setLayoutParams(params2);
		adContainerBottom.addView(mcvBottom);
		
		
	}
	private Handler handler; 
	/**
	 * 滚动公告
	 */
	private void initAnno() {
		final List<Announce> annos = new ArrayList<Announce>();
		annos.addAll(announces);
		if (announces.size() > 1)
		annos.add(announces.get(0));
		//刷新公告  保存上次滑动位置
		int current = wv.getCurrentItem();
		wv.setViewAdapter(new AnnoWheelAdapter(getActivity(), annos));
		if (null != handler) {
			handler.removeCallbacksAndMessages(null);
			if (current < annos.size() - 1) {
				wv.setCurrentItem(current);
			}
			else {
				wv.setCurrentItem(0);
			}
		}
			handler = new Handler(); 
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					int delayTime = 0;
					//最后一个广告后秒切回第一个
					if (wv.getCurrentItem() < annos.size() - 1) {
						wv.setCurrentItem(wv.getCurrentItem() + 1, true);
						delayTime = 3000;
					}
					else {
						wv.setCurrentItem(0);
						delayTime = 0;
					}
					handler.postDelayed(this, delayTime);
				}
			} , 1000);
	}
	
	/**
	 * 推荐商品
	 */
	private void initRecommendedProducts() {
		String[] arr = getResources().getStringArray(R.array.product_status);
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			String remainInvestmentAmount = FormatUtils.getAmount(product.getRemainingInvestmentAmount());
			String gain = product.getGain();
			String addGain = product.getActivityRate();
			
			String deadText = product.getDeadline() + product.getDeadlinedesc();
//			String recommendTitle = product.getRecommendTitle();
			String recommendBody = product.getRecommendBody();
			if (i==0) {
				//TODO 替换title
				tip1.setText(recommendBody);
				remainingInvestmentAmount1.setText(remainInvestmentAmount);
				if (product.getNewstatus() == 5 || product.getNewstatus() == 3) {
					remainingInvestmentAmount1.setText(remainInvestmentAmount);
					btn.setBackgroundResource(R.drawable.btn_blue);
					txtAvailable1.setVisibility(View.VISIBLE);
				}
				else {
					remainingInvestmentAmount1.setText(arr[product.getNewstatus()]);
					btn.setBackgroundResource(R.drawable.btn_grey);
					txtAvailable1.setVisibility(View.GONE);
				}
				limitTime1.setText(deadText);
				annualizedGain1.setText(gain);
				if (product.getActivity() != 0
						&& !StringUtils.isEmpty(product.getActivityRate())) {
					percentIcon1.setVisibility(View.GONE);
					add_v1.setVisibility(View.VISIBLE);
					add1.setText(addGain);
				}
				else {
					percentIcon1.setVisibility(View.VISIBLE);
					add_v1.setVisibility(View.GONE);
				}
			}
			if (i==1) {
				if (product.getNewstatus() == 5 || product.getNewstatus() == 3) {
					remainingInvestmentAmount2.setText(remainInvestmentAmount);
					txtAvailable2.setVisibility(View.VISIBLE);
				}
				else {
					remainingInvestmentAmount2.setText(arr[product.getNewstatus()]);
					txtAvailable2.setVisibility(View.GONE);
				}
				limitTime2.setText("期限" + deadText);
				annualizedGain2.setText(gain);
				if (product.getActivity() != 0
						&& !StringUtils.isEmpty(product.getActivityRate())) {
					percentIcon2.setVisibility(View.GONE);
					add_v2.setVisibility(View.VISIBLE);
					add2.setText(addGain);
				}
				else {
					percentIcon2.setVisibility(View.VISIBLE);
					add_v2.setVisibility(View.GONE);
				}
				product2.setOnClickListener(listener);
				product2.setVisibility(View.VISIBLE);
			}
			if (i==2) {
				if (product.getNewstatus() == 5 || product.getNewstatus() == 3) {
					remainingInvestmentAmount3.setText(remainInvestmentAmount);
					txtAvailable3.setVisibility(View.VISIBLE);
				}
				else {
					remainingInvestmentAmount3.setText(arr[product.getNewstatus()]);
					txtAvailable3.setVisibility(View.GONE);
				}
				limitTime3.setText("期限" + deadText);
				annualizedGain3.setText(gain);
				if (product.getActivity() != 0
						&& !StringUtils.isEmpty(product.getActivityRate())) {
					percentIcon3.setVisibility(View.GONE);
					add_v3.setVisibility(View.VISIBLE);
					add3.setText(addGain);
				}
				else {
					percentIcon3.setVisibility(View.VISIBLE);
					add_v3.setVisibility(View.GONE);
				}
				product3.setOnClickListener(listener);
				product3.setVisibility(View.VISIBLE);
				break;
			}
			
		}
		
	}
	
	private DialogFragment1 confirmDialog;
	/**
	 * 推送消息弹出
	 */
	private void initDialog() {
		final UPushMessage msg = CacheBean.getInstance().getMsg();
		//不为空 且未读
		if (msg != null && msg.getShowed() == 0) {
			pushImg.setImageResource(R.drawable.icon_news);
			if (msg.getType() == AppConstants.NotifyMsgType.TXT.getCode()) {
				confirmDialog = new DialogAlertFragment(new CallBackDialogConfirm() {
					
					@Override
					public void onSubmit(int position) {
						//推送消息存贮
						msg.setShowed(1);
						msg.setShowedTime(System.currentTimeMillis());
						CacheBean.getInstance().setMsg(msg);
						KJDB kjdb = KJDB.create(getActivity());
						kjdb.update(msg);
						confirmDialog.dismiss();
						confirmDialog = null;
//						pushImg.setImageResource(R.drawable.checkbox_none);
						pushImg.setVisibility(View.GONE);
					}
					
					@Override
					public void onKeyBack() {
						confirmDialog.dismiss();
						confirmDialog = null;
						
					}
				}, msg.getContent(), "", 0);
			}
			else {
				confirmDialog = new DialogConfirmFragment(new DialogConfirmFragment.CallBackDialogConfirm() {
					@Override
					public void onSubmit(int position) {
						if (null == confirmDialog) {
							return;
						}
						if (msg.getType() == AppConstants.NotifyMsgType.UPDATE.getCode()) {
							Intent i = new Intent(getActivity(), AboutListActivity.class);
							i.putExtra("update", true);
							getActivity().startActivity(i);
						}
//						else if (msg.getType() == MainHomeFragment.PRODUCT) {
//							Intent intent = new Intent(getActivity(),
//									TenderActivity.class);
//							intent.putExtra("id", msg.getProductId());
//							getActivity().startActivity(intent);
//						}
						//地址需要为以http开头的全称url
						else if (msg.getType() == AppConstants.NotifyMsgType.URL.getCode()) {
			            	try {
								Intent intent = new Intent();  
								intent.setAction(Intent.ACTION_VIEW);  
								intent.addCategory(Intent.CATEGORY_BROWSABLE); 
								intent.setData(Uri.parse(msg.getUrl()));
								getActivity().startActivity(intent);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						msg.setShowed(1);
						msg.setShowedTime(System.currentTimeMillis());
						CacheBean.getInstance().setMsg(msg);
						KJDB kjdb = KJDB.create(getActivity());
						kjdb.update(msg);
						confirmDialog.dismiss();
						confirmDialog = null;
//						pushImg.setImageResource(R.drawable.checkbox_none);
						pushImg.setVisibility(View.GONE);
					}
					
					@Override
					public void onKeyBack() {
						confirmDialog.dismiss();
						confirmDialog = null;
					}
					
					@Override
					public void onCancel() {
						confirmDialog.dismiss();
						confirmDialog = null;
					}
				}, msg.getContent(), " ", 0, "确定", "忽略");
			}
		}
		else {
//			pushImg.setImageResource(R.drawable.checkbox_none);
			pushImg.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 进行商品跳转
	 * @param product
	 */
	private void goForProduct(Product product) {
		Intent intent = new Intent(getActivity(), TenderActivity.class);
		intent.putExtra("product", product);
		startActivity(intent);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.anno:
				AnnoActivity.startActivity(getActivity(), announces);
				break;
			case R.id.pushImg:
				initDialog();
				if (null != confirmDialog) {
					confirmDialog.show(getFragmentManager(), "dialog");
				}
				break;
			case R.id.product1:
				if (products.size() > 0)
				goForProduct(products.get(0));
				break;
			case R.id.product2:
				goForProduct(products.get(1));
				break; 
			case R.id.product3:
				goForProduct(products.get(2));
				break;
			case R.id.llNewHand:
				startActivity(new Intent(getActivity(), NewHandActivity.class));
				break;
			case R.id.llInvite:
				Intent intent = null;
				if (!AppVariables.isSignin) {
					intent = new Intent(getActivity(), SigninActivity.class);
					startActivityForResult(intent, TenderActivity.REQUEST_SIGNIN);
					return;
				}
				intent = new Intent(getActivity(), NormalInviteActivity.class);
				intent.putExtra("activity", "account");
				startActivity(intent);
				break;
			case R.id.llGuard:
				intent = new Intent(getActivity(), GuardActivity.class);
				startActivity(intent);
				break;
				
			}
		}
	};
	

	public void refreshData() {
		if (!initialed) return;
		loadRecommendedProducts();
		loadAnnos();
	}
	
	/**
	 * 初始化轮播 控件
	 * @param cycleView  控件view
	 * @param cycleDatas 轮播信息
	 */
	private void initCycleViewData(MutiCycleViewHome cycleView, List<CycleData> cycleDatas) {
		final List<CycleData> showCycleDatas = new ArrayList<CycleData>();
		showCycleDatas.addAll(cycleDatas);
		
		cycleView.setImageResources(showCycleDatas, new MutiCycleViewHome.ImageCycleViewListener() {
			
			@Override
			public void onImageClick(int position, View imageView) {
				//根据类型执行 商品内部跳转 和外链跳转 
				CycleData cycleData = showCycleDatas.get(position);
	            if (cycleData.getType() == ShopADType.goods) {
//	            	CacheShop.getInstance().setFromType(CacheShop.home_banner);
//	            	CacheShop.getInstance().reqShopFrom(null);
//					AtyShopListNew.startAty(AtyHomeNew.this,cycleData.getLinkUrl());
	            }
	            else if (cycleData.getType() == ShopADType.url) {
	            	if (TextUtils.isEmpty(cycleData.getLinkUrl())) return;
	            	Intent intent = new Intent();  
	                intent.setAction(Intent.ACTION_VIEW);  
	                intent.addCategory(Intent.CATEGORY_BROWSABLE); 
	            	intent.setData(Uri.parse(cycleData.getLinkUrl()));
	            	startActivity(intent);
	            }
			}
			
			@Override
			public void displayImage(final CycleData cycleData, final ADCycleItem adCycleItem) {
				//TODO 修正图片显示
				adCycleItem.getImageView().setScaleType(ScaleType.FIT_XY);
				Glide.with(getActivity()).load(cycleData.getUrl()).into(adCycleItem.getImageView());
			}
		});
		cycleView.startImageTimerTask();
	} 
	
	/**
	 * 获取排序队列
	 * @param adDatas 
	 * @return
	 */
	private List<CycleData> getCycleADs(List<ShopADData> adDatas) {
		List<CycleData> shopADs = new ArrayList<CycleData>();
		Collections.sort(adDatas, new Comparator<ShopADData>() {

			@Override
			public int compare(ShopADData lhs, ShopADData rhs) {
				//首页广告为sortNo 从大到小排列
//				return rhs.getSortNo().compareTo(lhs.getSortNo());
				//首页广告为sortNo 从小到大排列
				return lhs.getSortNo().compareTo(rhs.getSortNo());
			}
		});
		shopADs.addAll(adDatas);
		return shopADs;
	}
	
	/**
	 * 拉取轮播广告
	 * @param mcv
	 * @param positon 0 top 1 bottom
	 */
	private void loadCycleADDatas(final MutiCycleViewHome mcv, final int positon) {
		List<ShopADData> ads = null;
		String adUrl = null;
		if (positon == TOP) {
			ads = CacheBean.getInstance().getShopADDatas();
			adUrl = AppConstants.GET_SLIDE_IMAGE;
		}
		else if (positon == BOTTOM) {
			ads = CacheBean.getInstance().getShopBottomADDatas();
			adUrl = AppConstants.GET_BOTTOM_IMAGE;
		}
		
		if (null != ads && ads.size() > 0) {
			initCycleViewData(mcv, getCycleADs(ads));
			return;
		}
		
			// 一步任务获取图片
			KJHttp kjh = new KJHttp();
			HttpParams params = new HttpParams();
			kjh.post(adUrl, params, new HttpCallBack(getActivity(), false) {

				@Override
				public void success(JSONObject ret) {
					try {
						List<ShopADData> shopADDatas = new ArrayList<ShopADData>();
						JSONArray ja = ret.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							ShopADData ad = new ShopADData();
							ad.setSortNo((double) i);
							ad.setImgUrl(ja.getJSONObject(i).getJSONObject("extra")
									.getString("img"));
							ad.setAndroidUrl(ja.getJSONObject(i).getJSONObject("extra")
									.getString("url"));
							ad.setType(ShopADType.url);
							shopADDatas.add(ad);
						}
//						shopADDatas.clear();
						if (shopADDatas.size() > 0) {
							if (positon == TOP) {
								CacheBean.getInstance().setShopADDatas(shopADDatas);
							}
							else if (positon == BOTTOM) {
								CacheBean.getInstance().setShopBottomADDatas(shopADDatas);
							}
							initCycleViewData(mcv, getCycleADs(shopADDatas));
						}
						else {
							mcv.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					super.success(ret);
				}
				
			});
		}
	
	/**
	 * 拉取推荐商品
	 */
	private void loadRecommendedProducts() {
			HttpParams params = new HttpParams();
			params.put("page", 1);
			params.put("sid", AppVariables.sid);
			params.put("loginVersionName", "Android"
					+ ApplicationUtil.getApkInfo(getActivity()).versionName);
			if (null == http) {
				http = new KJHttp();
			}
			http.post(AppConstants.PRODUCTS_RECOMMENDED, params, productCallback);
	}
	
	private HttpCallBack productCallback = new HttpCallBack(getActivity(), false) {
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
//				new_hand = ret.getInt("new_hand");
//				int state = ret.getInt("status");
//				if (state == 0) {
					products = new ProductList(ret.getJSONArray("product_list"))
							.getProducts();
					initRecommendedProducts();
//				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	/**
	 * 拉取公告
	 */
	private void loadAnnos() {
		loadAnnos(annoCallback);
	}
	
	private void loadAnnos(HttpCallBack callBack) {
		HttpParams params = new HttpParams();
		params.put("page", 1);
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.ANNOUNCE, params, callBack);
	}
	
	private HttpCallBack annoCallback = new HttpCallBack(getActivity(), false) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("articles");
				int page = articles.getInt("currentPage");
				JSONArray arr = articles.getJSONArray("items");
				if (null != arr && arr.length() > 0) {
					announces = new AnnounceList(arr).getAnnounces();
					initAnno();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("数据解析错误。");
				Toast.makeText(getActivity(), R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

	};

}
