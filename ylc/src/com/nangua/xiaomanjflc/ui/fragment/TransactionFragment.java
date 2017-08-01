package com.nangua.xiaomanjflc.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.adapter.TransactionRecordAdapter;
import com.nangua.xiaomanjflc.bean.Transaction;
import com.nangua.xiaomanjflc.bean.TransactionList;
import com.nangua.xiaomanjflc.R;

@SuppressLint("SimpleDateFormat")
public class TransactionFragment extends ListFragment {

	private LinearLayout layout;

	private KJListView listview;

	private KJHttp http;
	private HttpParams params;

	private List<Transaction> data;

	private int page = 1;
	private boolean noMoreData;

	private TransactionRecordAdapter recordAdapter;
	private boolean refresh = true;

	private String typeFilter;
	
	public TransactionFragment() {
	}

	public TransactionFragment(String typeFilter) {
		this.typeFilter = typeFilter;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = (LinearLayout) inflater.inflate(
				R.layout.activity_transaction_listview, null);
		return layout;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	private void init() {
		listview = (KJListView) layout.findViewById(android.R.id.list);
		data = new ArrayList<Transaction>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		params.put("typeFilter", typeFilter);
		http.post(AppConstants.TRANSACTION, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(getActivity()) {
		@Override
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("balanceLogList");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				page = articles.getJSONObject("pager").getInt("page");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page == 1) {
					data = new TransactionList(articles.getJSONArray("items"))
							.getList();
				} else {
					data = new TransactionList(data,
							articles.getJSONArray("items")).getList();
				}

				List<Transaction> data2 = new ArrayList<Transaction>();
				String datetime = "";
				String createTime = "";
				SimpleDateFormat sdfold;
				SimpleDateFormat sdfnew;
				for (int i = 0; i < data.size(); i++) {
					sdfold = new SimpleDateFormat("yyyy-MM-dd");
					createTime = data.get(i).getCreateTime();
					Date date = sdfold.parse(createTime);
					sdfnew = new SimpleDateFormat("yyyy年MM月");
					if (!datetime.equals(sdfnew.format(date))) {
						datetime = sdfnew.format(date);
						Transaction transaction = new Transaction();
						transaction.setDateflag(sdfnew.format(date));
						data2.add(transaction);
						data2.add(data.get(i));
					} else {
						data2.add(data.get(i));
					}
				}

				if (refresh) {
					recordAdapter = new TransactionRecordAdapter(getActivity(),
							data2);
					listview.setAdapter(recordAdapter);
					listview.setOnRefreshListener(refreshListener);
					listview.setEmptyView(getActivity().findViewById(R.id.empty));
					refresh = false;
				} else {
					recordAdapter.setTransactions(data2);
					recordAdapter.notifyDataSetChanged();
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

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

}