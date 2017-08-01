package com.nangua.xiaomanjflc.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.adapter.CommonAdapter;
import com.nangua.xiaomanjflc.adapter.ViewHolder;
import com.nangua.xiaomanjflc.bean.Announce;
import com.nangua.xiaomanjflc.bean.AnnounceList;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.R;

public class AnnoActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Announce> adapter;
	private static List<Announce> data = new ArrayList<Announce>();

	private int page = 1;
	private boolean noMoreData;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_listview);
		UIHelper.setTitleView(this, "返回", "平台公告");
	}

	@Override
	public void initData() {
		super.initData();
		http = new KJHttp();
		params = new HttpParams();
//		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", "");
		http.post(AppConstants.ANNOUNCE, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Announce>(AnnoActivity.this,
				R.layout.item_anno) {
			@Override
			public void click(int id, List<Announce> list, int position,
					ViewHolder viewHolder) {
				switch (id) {
				case R.id.item_anno:
					Intent intent = new Intent(AnnoActivity.this,
							AnnoContentActivity.class);
					intent.putExtra("title", list.get(position).getTitle());
					intent.putExtra("data", list.get(position).getDateline());
					intent.putExtra("content", list.get(position).getContent());
					startActivity(intent);
					break;
				}
			}

			@Override
			public void canvas(ViewHolder holder, Announce item) {
				holder.addClick(R.id.item_anno);
				holder.setText(R.id.data, item.getDateline(), false);
				holder.setText(R.id.title, item.getTitle(), false);
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
			System.out.println("加载更多============");
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

	private HttpCallBack httpCallback = new HttpCallBack(AnnoActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("articles");
				page = articles.getInt("currentPage");
//				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				JSONArray arr = articles.getJSONArray("items");
				if (null == arr || arr.length() == 0) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				System.out.println("当前页面=====>" + page);
				if (page == 1) {
					data = new AnnounceList(arr)
							.getAnnounces();
				} else {
					data = new AnnounceList(data, arr).getAnnounces();
				}
				System.out.println("data=========>" + data);
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("数据解析错误。");
				Toast.makeText(AnnoActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};
	
	public static void startActivity(Activity activity, List<Announce> announces) {
		data = announces;
		Intent i = new Intent(activity,AnnoActivity.class);
		activity.startActivity(i);
	}

}
