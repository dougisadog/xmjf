package com.nangua.xiaomanjflc.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.adapter.CommonAdapter;
import com.nangua.xiaomanjflc.adapter.ViewHolder;
import com.nangua.xiaomanjflc.bean.Invite;
import com.nangua.xiaomanjflc.bean.InviteList;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.utils.FormatUtils;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.nangua.xiaomanjflc.R;

public class InviteListActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Invite> adapter;
	private List<Invite> data;

	private int page = 1;
	private boolean noMoreData;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_invite_listview);
		UIHelper.setTitleView(this, "返回", "邀请记录");
	}

	@Override
	public void initData() {
		super.initData();
		data = new ArrayList<Invite>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Invite>(InviteListActivity.this,
				R.layout.item_invite_record) {
			@Override
			public void canvas(ViewHolder holder, Invite item) {
				holder.setText(R.id.name, item.getName(), false);
//				FontTextView type = holder.getView(R.id.type);
//				FontTextView desc = holder.getView(R.id.desc);
				FontTextView date = holder.getView(R.id.date);
				if (item.getRet_status() == 1) {
//					holder.setText(R.id.status, "已返", false);
//					type.setTextColor(getResources().getColor(R.color.red));
//					desc.setTextColor(getResources().getColor(R.color.red));
//					date.setTextColor(getResources().getColor(R.color.red));
				} else {
//					holder.setText(R.id.status, "待返", false);
//					type.setTextColor(getResources().getColor(R.color.font_green));
//					desc.setTextColor(getResources().getColor(R.color.font_green));
//					date.setTextColor(getResources().getColor(R.color.font_green));
				}
				date.setText(item.getCreate_time());
				switch (item.getInv_type()) {
				case 1:
//					type.setText("邀请注册");
//					desc.setText(FormatUtils.priceFormat(item.getAmount()) +  "元现金券");
					break;
				case 2:
//					type.setText("邀请投资");
//					desc.setText(FormatUtils.priceFormat(item.getAmount()) +  "元现金券");
					break;
				case 3:
//					type.setText("邀请投资");
//					desc.setText(FormatUtils.priceFormat(item.getAmount()) + "元");
					break;
				}
			}

			@Override
			public void click(int id, List<Invite> list, int position,
					ViewHolder viewHolder) {
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

	private void getData(int page) {
		params.put("sid", AppVariables.sid);
		params.put("page", page);
		http.post(AppConstants.INVITE, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(
			InviteListActivity.this) {
		@Override
		public void onSuccess(String t) {
			try {
				JSONObject ret = new JSONObject(t);
				JSONObject invitations = ret.getJSONObject("invitations");
				page = invitations.getInt("currentPage");
				int maxPage = invitations.getJSONObject("pager").getInt("maxPage");
				JSONArray arr = invitations.getJSONArray("items");
				if (null == arr || arr.length() == 0) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page < 2) {
					data = new InviteList(invitations.getJSONArray("items")).getList();
				} else {
					data = new InviteList(data, invitations.getJSONArray("items")).getList();
				}
				adapter.setList(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

}
