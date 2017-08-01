package com.nangua.xiaomanjflc.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.adapter.CommonAdapter;
import com.nangua.xiaomanjflc.adapter.ViewHolder;
import com.nangua.xiaomanjflc.bean.Invest;
import com.nangua.xiaomanjflc.bean.InvestList;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.widget.TitleTab;
import com.nangua.xiaomanjflc.widget.TitleTab.ItemCallBack;
import com.nangua.xiaomanjflc.R;

public class InvestActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;
	@BindView(id = R.id.mytab)
	private TitleTab titleTab;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Invest> adapter;
	private List<Invest> data;

	private int page = 1;
	private String url;
	private int state;
	private boolean noMoreData;
	private boolean clickable = true;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_invest_v2);
		UIHelper.setTitleView(this, "我的账户", "投资记录");
	}

	@Override
	public void initData() {
		super.initData();
		url = AppConstants.INVEST_ORDER;
		state = 1;
		data = new ArrayList<Invest>();
		http = new KJHttp();
		params = new HttpParams();
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		http.post(url, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		final List<String> names = new ArrayList<String>();
		names.add("回款中");
		names.add("待确认");
		names.add("已结清");
		names.add("流标");
		titleTab.setDatas(names, new ItemCallBack() {

			@Override
			public void onItemClicked(int position) {
				for (int i = 0; i < titleTab.getChildCount(); i++) {
					
					TextView tv = titleTab.getTextView(i);
					if (null != tv)
						tv.setTextColor(getResources().getColor(position == i ? R.color.orange : R.color.grey));
				}
				if (titleTab.getCurrentPosition() != position) {
					if (position == 0) {
						url = AppConstants.INVEST_ORDER;
						state = 1;
					}
					else if (position == 1) {
						url = AppConstants.INVEST_PENDING;
						state = 2;
					}
					else if (position == 2) {
						url = AppConstants.INVEST_CLOSED;
						state = 3;
					}
					else if (position == 3) {
						url = AppConstants.INVEST_ABORTED;
						state = 4;
					}
					getData(1);
				}
			}

		});
		
		titleTab.clickItem(0);
		adapter = new CommonAdapter<Invest>(InvestActivity.this,
				R.layout.item_invest_v2) {
			@Override
			public void canvas(ViewHolder holder, Invest item) {
				holder.addClick(R.id.invest_protocol);
				TextView t = holder.getView(R.id.statusText);
				holder.setText(R.id.name, item.getName(), false);
				holder.setText(R.id.price, item.getPrice() + "元", false);
				holder.setText(R.id.rate, item.getRate(), false);
				LinearLayout l = holder.getView(R.id.add_v);
				TextView gainCent = holder.getView(R.id.gainCent);
				gainCent.setVisibility(View.VISIBLE);
				if (!StringUtils.isEmpty(item.getActivityRate())) {
					gainCent.setVisibility(View.GONE);
					l.setVisibility(View.VISIBLE);
					holder.setText(R.id.add,item.getActivityRate(), false);
				} else {
					l.setVisibility(View.GONE);
				}
				switch (state) {
				case 1:
					holder.setText(R.id.txthint, "回款本息", false);
					holder.setText(R.id.lastReturn,
							item.getPrincipalAndInterest() + "元", false);
					holder.setText(R.id.repayTime,
							"下期回款日：" + item.getRepayTime(), false);
					t.setVisibility(View.VISIBLE);
					t.setText("期数：" + item.getStatusText());
					break;
				case 2:
					holder.setText(R.id.txthint, "状态", false);
					holder.setText(R.id.lastReturn, item.getStatusText(), false);
					holder.setText(R.id.repayTime,
							"投资日期：" + item.getCreateDate(), false);
					t.setVisibility(View.VISIBLE);
					String beginDate = item.getInterestBeginDate();
					if (StringUtils.isEmpty(beginDate))
						t.setVisibility(View.GONE);
					else
					t.setText("起息：" + item.getInterestBeginDate());
					break;
				case 3:
					holder.setText(R.id.txthint, "回款总额", false);
					holder.setText(R.id.lastReturn, item.getLastReturn() + "元", false);
					holder.setText(R.id.repayTime,
							"起息：" + item.getInterestBeginDate(), false);
					t.setVisibility(View.VISIBLE);
					t.setText("结清：" + item.getEndDate());
					break;
				case 4:
					holder.setText(R.id.txthint, "状态", false);
					holder.setText(R.id.lastReturn, item.getStatusText(), false);
					holder.setText(R.id.repayTime,
							"投资日期：" + item.getCreateDate(), false);
					t.setVisibility(View.GONE);
					break;

				}
			}

			@Override
			public void click(int id, List<Invest> list, int position,
					ViewHolder viewHolder) {
				switch (id) {
				case R.id.invest_protocol:
					Intent intent = new Intent(InvestActivity.this,
							InvestProtocolActivity.class);
					intent.putExtra("id", list.get(position).getId());
					startActivity(intent);
					break;
				}
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		listview.setEmptyView(findViewById(R.id.empty));
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

	private HttpCallBack httpCallback = new HttpCallBack(InvestActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("orders");
				page = articles.getInt("currentPage");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page < 2) {
					data = new InvestList(articles.getJSONArray("items"))
							.getInvests();
				} else {
					data = new InvestList(data, articles.getJSONArray("items"))
							.getInvests();
				}
				adapter.setList(data);
			} catch (Exception e) {
				if (page > 0) {
					page = page - 1;
				}
				e.printStackTrace();
				Toast.makeText(InvestActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
			clickable = true;
		}
	};
}
