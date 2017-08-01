package com.nangua.xiaomanjflc.ui.fragment;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.adapter.CommonAdapter;
import com.nangua.xiaomanjflc.adapter.ViewHolder;
import com.nangua.xiaomanjflc.bean.DetailRecord;
import com.nangua.xiaomanjflc.bean.DetailRecordList;
import com.nangua.xiaomanjflc.bean.Product;
import com.nangua.xiaomanjflc.ui.myabstract.VerticalScrollFragment;
import com.nangua.xiaomanjflc.widget.TenderVerticalViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProductRecordFragment extends VerticalScrollFragment{

	private View v;

	private KJListView listview2;
	private CommonAdapter<DetailRecord> adapter2;
	private List<DetailRecord> records;	
	private TextView mEmpty;
	
	private KJHttp http;
	private HttpParams params;
	private int id;
	private int page = 1;
	private boolean noMoreData;
	
	public ProductRecordFragment (ScollCallBack scollCallBack) {
		this.scollCallBack = scollCallBack;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initialed = true;
		
		v = inflater.inflate(R.layout.fragment_product_record, null);
		
		mEmpty = (TextView) v.findViewById(R.id.empty);
		listview2 = (KJListView) v.findViewById(R.id.listview2);
		adapter2 = new CommonAdapter<DetailRecord>(getActivity(),
				R.layout.item_detail_record) {
			@Override
			public void canvas(ViewHolder holder, DetailRecord item) {
				holder.setText(R.id.realName, item.getRealName(), false);
				holder.setText(R.id.price, "￥" + item.getPrice(), false);
				holder.setText(R.id.createDate, item.getCreateDate(), false);
			}

			@Override
			public void click(int id, List<DetailRecord> list, int position,
					ViewHolder viewHolder) {
			}
		};
		listview2.setOverScrollMode(View.OVER_SCROLL_NEVER );
		listview2.setAdapter(adapter2);
		listview2.setOnRefreshListener(refreshListener);
		listview2.getHeadView().setVisibility(View.INVISIBLE);
//		listview2.setEmptyView(v.findViewById(R.id.empty));
		listview2.setPullRefreshEnable(false);
		Intent intent = getActivity().getIntent();
		Product product = (Product) intent.getSerializableExtra("product");
		id = product.getId();
		http = new KJHttp();
		params = new HttpParams();
		getData(1);
		return v;
	}
	
	
	
	@Override
	public void onResume() {
		TenderVerticalViewPager.setActionDown(null);
		super.onResume();
	}

	private void getData(int page) {
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("id", id);
		params.put("page", page);
		http.post(AppConstants.DETAIL_PRODUCT + id, params, httpCallback);
	}
	
	
	private HttpCallBack httpCallback = new HttpCallBack(getActivity()) {
		public void success(org.json.JSONObject ret) {
			try {

				JSONObject articles = ret.getJSONObject("productOrders");
				page = articles.getInt("currentPage");
				JSONArray arr = articles.getJSONArray("items");
				if (null == arr || arr.length() == 0) {
					listview2.hideFooter();
					noMoreData = true;
				} else {
					listview2.showFooter();
					noMoreData = false;
				}

				records = new DetailRecordList(articles.getJSONArray("items"))
						.getList();
				adapter2.setList(records);
				mEmpty.setVisibility(records.size() > 0 ? View.INVISIBLE : View.VISIBLE);
				listview2.setVisibility(records.size() > 0 ? View.VISIBLE : View.INVISIBLE);
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(), R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}
		
		

		@Override
		public void failure(JSONObject ret) {
			getActivity().finish();
			super.failure(ret);
		}



		public void onFinish() {
			super.onFinish();
			listview2.stopRefreshData();
		}
	};
	
	private KJRefreshListener refreshListener = new KJRefreshListener() {
		@Override
		public void onRefresh() {
//			getData(1);
			
			//进过优化后舍弃
//			if (null != scollCallBack)
//				scollCallBack.onScrollTop();
		}

		@Override
		public void onLoadMore() {
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub
		
	}

}
