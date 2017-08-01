package com.nangua.xiaomanjflc.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.StringUtils;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppConstants.SortType;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.adapter.SlideAdapter;
import com.nangua.xiaomanjflc.adapter.ViewHolder;
import com.nangua.xiaomanjflc.bean.Product;
import com.nangua.xiaomanjflc.bean.ProductList;
import com.nangua.xiaomanjflc.dialog.TabsFragment;
import com.nangua.xiaomanjflc.dialog.TabsFragment.CallBackDialogConfirm;
import com.nangua.xiaomanjflc.ui.TenderActivity;
import com.nangua.xiaomanjflc.ui.myabstract.HomeFragment;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.utils.FormatUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductFragment extends HomeFragment {

	private KJListView listview;
	private KJHttp http;

	private SlideAdapter<Product> adapter;
	private List<Product> data;

	private int page = 1;
//	private int new_hand = 0;
	private boolean noMoreData;
	
	private RelativeLayout all;
	private RelativeLayout gain;
	private RelativeLayout limitTime;
	private RelativeLayout singlePurchase;
	private RelativeLayout btnSearch;
//	private LayoutInflater inflater;
	private ViewGroup container;
	
	private ImageView imgSearch;
	
	private LinearLayout llButton;
	
	private TextView tips;
	private TextView mEmpty;
	private HashMap<Integer, RelativeLayout> topButtons = new HashMap<Integer, RelativeLayout>();

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initialed = true;
//		this.inflater = inflater;
		this.container = container;
		data = new ArrayList<Product>();
		http = new KJHttp();
		getData(1);
		View v = inflater.inflate(R.layout.fragment_product, null);
		initView(v);
		listview = (KJListView) v.findViewById(R.id.listview);
		adapter = new SlideAdapter<Product>(getActivity(),
				R.layout.item_product_v2) {
			@Override
			public void canvas(ViewHolder holder, Product item) {
				holder.addClick(R.id.item);
				holder.setText(R.id.name, item.getName(), false);
				
				//活动标签
				TextView ni = holder.getView(R.id.activityName);
				TextView gainCent = holder.getView(R.id.gainCent);
				holder.setText(R.id.gain, item.getGain(), false);
				LinearLayout l = holder.getView(R.id.add_v);
				gainCent.setVisibility(View.VISIBLE);
				if (item.getActivity() == 0) {
					
					ni.setVisibility(View.GONE);
					l.setVisibility(View.GONE);
				} else {
					if (!StringUtils.isEmpty(item.getActivityRate())) {
						gainCent.setVisibility(View.GONE);
						l.setVisibility(View.VISIBLE);
						holder.setText(R.id.add,item.getActivityRate(), false);
					} else {
						l.setVisibility(View.GONE);
					}
					ni.setVisibility(View.VISIBLE);
					ni.setText(item.getNameInfo());
				}
				holder.setText(R.id.deadline, item.getDeadline(), false);
				holder.setText(R.id.deadlinedesc, item.getDeadlinedesc(), false);
//				holder.setText(R.id.repayMethod, item.getRepayMethod(), false);
//				holder.setText(R.id.singlePurchaseLowerLimit,
//						item.getSinglePurchaseLowerLimit() + "元起投", false);
//				holder.setText(R.id.guaranteeModeName,
//						item.getGuaranteeModeName(), false);

				int i = item.getNewstatus();
				TextView status = holder.getView(R.id.percnt);
				String[] arr = getResources().getStringArray(R.array.product_status);
				//使用预约时间开始时间替换 原预约2字
				String content = "";
				if (i == 3 && null != item.getFinanceStartDate()) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd\nHH:mm开始");
					content = format.format(item.getFinanceStartDate());
					status.setTextSize(10);
					status.setText(content);
					status.setTextColor(getResources().getColor(R.color.orange));
				}
				else if (i == 5) { // 正在售卖
					//将可投总额隐藏
//					content = "可投金额" + remainInvestmentAmount
//							+ "/"
//							+ FormatUtils.getAmount(item
//									.getTotalInvestment());
					String remainInvestmentAmount = FormatUtils.getAmount(item.getRemainingInvestmentAmount());
					content = "可投金额" + remainInvestmentAmount;
					status.setText(content);
					status.setTextColor(getResources().getColor(R.color.orange));
				} else {
					status.setText(arr[i]);
					status.setTextColor(getResources().getColor(R.color.grey));
				}
				//TODO 进度条
				holder.setText(R.id.percentage, item.getPercentage() + "%",
						false);
				ProgressBar pb = holder.getView(R.id.percentagepb);
				pb.setProgress(item.getPercentage());
				
				//TODO 推荐标签
				ImageView confine = holder.getView(R.id.confine);
				confine.setVisibility(View.VISIBLE);
				switch (item.getConfine()) {
				case 0:
					confine.setVisibility(View.INVISIBLE);
//					confine.setImageResource(R.drawable.confine0);
					break;
				case 1:
					confine.setImageResource(R.drawable.confine1);
//					if (new_hand != 0 || !AppVariables.isSignin) {
//						status.setBackgroundResource(R.drawable.saled);
//					}
					break;
				case 2:
					confine.setImageResource(R.drawable.confine2);
					break;
				case 3:
					confine.setImageResource(R.drawable.confine4);
					break;
				case 4:
					confine.setImageResource(R.drawable.confine4);
					break;
				case 5:
					confine.setImageResource(R.drawable.confine5);
					break;
				case 6:
					confine.setImageResource(R.drawable.confine6);
//					if (new_hand != 0 || !AppVariables.isSignin) {
//						status.setBackgroundResource(R.drawable.saled);
//					}
					break;
				case 7:
					confine.setImageResource(R.drawable.confine7);
					break;
				case 8:
					confine.setImageResource(R.drawable.confine8);
					break;
			}


			}

			@Override
			public void click(int id, List<Product> list, int position,
					ViewHolder viewHolder) {
					Intent intent = new Intent(getActivity(),
							TenderActivity.class);
//					intent.putExtra("id", list.get(position).getId());
					//专享类型
//					intent.putExtra("confine", list.get(position).getConfine());
//					intent.putExtra("status", list.get(position).getNewstatus());
//					intent.putExtra("new_hand", new_hand);
					//TODO
					intent.putExtra("product", list.get(position));
					startActivity(intent);
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		return v;
	}

	private void initView(View v) {
		llButton = (LinearLayout) v.findViewById(R.id.llButton);
		all = (RelativeLayout) v.findViewById(R.id.all);
		all.setOnClickListener(listener);
		gain = (RelativeLayout) v.findViewById(R.id.gain);
		gain.setOnClickListener(listener);
		limitTime = (RelativeLayout) v.findViewById(R.id.limitTime);
		limitTime.setOnClickListener(listener);
		singlePurchase = (RelativeLayout) v.findViewById(R.id.singlePurchase);
		singlePurchase.setOnClickListener(listener);
		btnSearch = (RelativeLayout) v.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(listener);
		imgSearch = (ImageView) v.findViewById(R.id.imgSearch);
		tips = (TextView) v.findViewById(R.id.tips);
		mEmpty = (TextView) v.findViewById(R.id.empty);
		
		//初始化 topButtons
		topButtons.put(0, all);
		topButtons.put(SortType.PROFIT.getCode(), gain);
		topButtons.put(SortType.LIMIT_DATE.getCode(), limitTime);
		topButtons.put(SortType.COST.getCode(), singlePurchase);
	}
	
	/**
	 * 刷新对应button 状态UI
	 * @param key
	 */
	private void refreshButton(int key) {
		RelativeLayout rl = topButtons.get(Math.abs(key));
		((TextView)rl.getChildAt(0)).setTextColor(getResources().getColor(R.color.blue_v2));
		if (key != 0)
		((ImageView)rl.getChildAt(1)).setImageResource(key > 0 ? R.drawable.icon_sort_down :R.drawable.icon_sort_up);
	}
	
	/**
	 * 重置所有topButton
	 */
	private void resetButtonStatus() {
		for (int i = 0; i < 4; i++) {
			RelativeLayout rl = (RelativeLayout) llButton.getChildAt(i);
			TextView tv = (TextView)rl.getChildAt(0);
			tv.setTextColor(getResources().getColor(R.color.grey_btn));
			View v = rl.getChildAt(1);
			if (null != v)
			((ImageView)v).setImageResource(R.drawable.icon_sort_none);
		}
		imgSearch.setImageResource(R.drawable.icon_search_off);
	}
	
	private int tjFlg = 0;  //正数为倒序 负数为正序  2 为收益 3为可投金额 4位投资期限
	private Map<String, String> search = null;
	
	private TabsFragment tabsFragment;

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			hideSearch();
			resetButtonStatus();
			switch (view.getId()) {
				case R.id.all:
					tjFlg = 0;
					search = null;
					page = 1;
					((TextView)all.getChildAt(0)).setTextColor(getResources().getColor(R.color.blue_v2));
					getData(1);
					break;
				case R.id.gain:
					//将 条件筛选与 排序独立
					search = null;
					
					if (Math.abs(tjFlg) == SortType.PROFIT.getCode()) {
						tjFlg = -tjFlg;
					}
					else {
						tjFlg = SortType.PROFIT.getCode();
					}
					page = 1;
					refreshButton(tjFlg);
					getData(1);
					break;
				case R.id.limitTime:
					//将 条件筛选与 排序独立
					search = null;
					
					if (Math.abs(tjFlg) == SortType.LIMIT_DATE.getCode()) {
						tjFlg = -tjFlg;
					}
					else {
						tjFlg = SortType.LIMIT_DATE.getCode();
					}
					page = 1;
					refreshButton(tjFlg);
					getData(1);
					break;
				case R.id.singlePurchase:
					//将 条件筛选与 排序独立
					search = null;
					
					if (Math.abs(tjFlg) == SortType.COST.getCode()) {
						tjFlg = -tjFlg;
					}
					else {
						tjFlg = SortType.COST.getCode();
					}
					page = 1;
					refreshButton(tjFlg);
					getData(1);
					break;
				case R.id.btnSearch:
					//展开条件选择界面
					if (null == tabsFragment) {
						tabsFragment = new TabsFragment((ViewGroup) container.getParent(), new CallBackDialogConfirm() {
							
							@Override
							public void onSubmit(HashMap<String, String> values) {
								tjFlg = 0;
								search = values;
								getData(1);
								hideSearch();
							}
							
							@Override
							public void onKeyBack() {
								hideSearch();
								if (null == search) {
									refreshButton(tjFlg);
								}
							}
						}, 0);
					}
					if (tabsFragment.isVisible()) {
						hideSearch();
						if (null == search) {
							refreshButton(tjFlg);
						}
					}
					else {
						tabsFragment.show(getFragmentManager());
						imgSearch.setImageResource(R.drawable.icon_search_on);
					}
					break;
			}
		}
	};
	
	private void hideSearch() {
		if (null != tabsFragment)
		tabsFragment.hide(getFragmentManager());
		if (null != imgSearch)
		imgSearch.setImageResource(null == search ? R.drawable.icon_search_off : R.drawable.icon_search_on);
	}

	private void getData(int page) {
		HttpParams params = new HttpParams();
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		params.put("loginVersionName", "Android"
				+ ApplicationUtil.getApkInfo(getActivity()).versionName);
		if (null == http) {
			http = new KJHttp();
		}
		if (tjFlg != 0)
			params.put("tjFlg", tjFlg);
		else if (null != search && search.size() > 0) {
			combineParams(search, params);
		}
		
		http.post(AppConstants.PRODUCTS, params, httpCallback);
	}
	
	/**
	 * condition = 1开启条件选择
	 * @param search tabsFragment 返回的所有条件
	 */
	private void combineParams(Map<String, String> search, HttpParams params) {
		params.put("condition", 1);
		for (Entry<String, String> condition : search.entrySet()) {
			params.put(condition.getKey(), condition.getValue());
		}
	}
	
	public void refreshData() {
		getData(1);
	}

	private KJRefreshListener refreshListener = new KJRefreshListener() {
		@Override
		public void onRefresh() {
			getData(1);
		}

		@Override
		public void onLoadMore() {
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

	private HttpCallBack httpCallback = new HttpCallBack(getActivity()) {
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
//				int state = ret.getInt("status");
//				new_hand = ret.getInt("new_hand");
				page = ret.getInt("current_page");
				JSONArray arr = ret.getJSONArray("product_list");
				if (null == arr || arr.length() == 0) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
					
				}
				if (page == 1) {
					data = new ProductList(arr)
							.getProducts();
				} else {
					data = new ProductList(data, arr).getProducts();
				}
				adapter.setList(data);
				mEmpty.setVisibility(data.size() > 0 ? View.INVISIBLE : View.VISIBLE);
				String content = "默认";
				content = SortType.getSortTypeByCode(Math.abs(tjFlg)).getType();
//				switch (Math.abs(tjFlg)) {
//					case 2 :
//						content = "收益";
//						break;
//					case 4 :
//						content = "期限";
//						break;
//					case 3 :
//						content = "金额";
//						break;
//					default :
//						break;
//				}
				tips.setText("————  按" + content + "排序     ————");
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		};

		public void onFinish() {
			listview.stopRefreshData();
		};
	};
	
}
