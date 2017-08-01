package com.nangua.xiaomanjflc.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.ui.TransactionNewActivity;
import com.nangua.xiaomanjflc.widget.FontTextView;

public class TransactionMenuFragment extends ListFragment {

	private KJHttp http;
	private HttpParams params;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_menu, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		init();
	}

	private void init() {
		http = new KJHttp();
		params = new HttpParams();
		getData();
	}

	private void getData() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.ACCOUNT_ISLOAN, params, httpCallback);
	}

	private HttpCallBack httpCallback = new HttpCallBack(getActivity()) {
		@Override
		public void success(org.json.JSONObject ret) {
			try {
				int articles = ret.getInt("listCnt");
				String[] transactionType;
				if (articles > 0) {
					transactionType = getResources().getStringArray(
							R.array.transactionType2);
				} else {
					transactionType = getResources().getStringArray(
							R.array.transactionType);
				}

				SampleAdapter adapter = new SampleAdapter(getActivity());
				for (int i = 0; i < transactionType.length; i++) {
					adapter.add(new SampleItem(transactionType[i]));
				}

				setListAdapter(adapter);

			} catch (Exception e) {
				e.printStackTrace();
				Log.i("TAG", "数据解析错误。");
				Toast.makeText(getActivity(), R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

	};

	public class SampleAdapter extends ArrayAdapter<SampleItem> {
		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_transaction_meun, null);
			}
			FontTextView title = (FontTextView) convertView
					.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}
	}

	private class SampleItem {
		public String tag;

		public SampleItem(String tag) {
			this.tag = tag;
		}
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		TransactionFragment newContent = new TransactionFragment(position + "");
		switchFragment(newContent);
	}

	private void switchFragment(Fragment fragment) {

		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof TransactionNewActivity) {
			TransactionNewActivity fca = (TransactionNewActivity) getActivity();
			fca.switchContent(fragment);
		}
	}
}
