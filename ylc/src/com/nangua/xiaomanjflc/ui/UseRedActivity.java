package com.nangua.xiaomanjflc.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.DensityUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.adapter.RedUserAdapter;
import com.nangua.xiaomanjflc.adapter.ViewHolder;
import com.nangua.xiaomanjflc.bean.Red;
import com.nangua.xiaomanjflc.bean.RedList;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.nangua.xiaomanjflc.R;

public class UseRedActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private ListView listview;

	@BindView(id = R.id.confirm, click = true)
	private TextView confirm;

	@BindView(id = R.id.footer_hint)
	private TextView hint;

	private KJHttp http;
	private HttpParams params;

	private RedUserAdapter adapter;
	private List<Red> data;

	private int page = 1;
	private String status = "1";
	private int price = 0;
	private int checkedid = 0;
	private int amount = 0;
	private int productid = 0;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_usecash);
		UIHelper.setTitleView(this, "投标", "使用现金券");
	}

	@Override
	public void initData() {
		super.initData();
		Intent intent = getIntent();
		amount = intent.getIntExtra("amount", 0);
		productid = intent.getIntExtra("productid", 0);
		data = new ArrayList<Red>();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("status", status);
		params.put("amount", amount);
		params.put("productid", productid);
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.NEWCASH, params, httpCallback);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.confirm:
			Intent intent = new Intent();
			intent.putExtra("cash", checkedid);
			intent.putExtra("price", price);
			setResult(AppConstants.SUCCESS, intent);
			finish();
			break;
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		redBg = getRedBg(R.color.orange);
		redGreyBg = getRedBg(R.color.grey);
		adapter = new RedUserAdapter(UseRedActivity.this,
				R.layout.item_red_v2) {

			@Override
			public void click(int id, List<Red> list, int position,
					ViewHolder viewHolder) {
				Red r = list.get(position);
				if (null == r || r.getLock_flg() != 0) return;
				if (checkedid == r.getId()) {
					r.setChecked(false);
					list.set(position, r);
					checkedid = 0;
					price = 0;
					hint.setText("已选0张，可抵扣0元");
				} else {
					for (int i = 0; i < list.size(); i++) {
						Red red = list.get(i);
						red.setChecked(false);
						list.set(i, red);
					}
					Red red = list.get(position);
					red.setChecked(true);
					list.set(position, red);
					checkedid = list.get(position).getId();
					price = Integer
							.parseInt(list.get(position).getCash_price());
					hint.setText("已选1张，可抵扣" + price + "元");
				}
				adapter.setList(data);
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setEmptyView(findViewById(R.id.empty));
	}
	
	private Bitmap redBg;
	private Bitmap redGreyBg;
	
	private Bitmap getRedBg(int colorId) {
		Bitmap bitmap = null;
		HashMap<Integer, Bitmap> redBgs = CacheBean.getInstance().getRedBgs();
		if (null == redBgs.get(colorId)) {
			int w = (ApplicationUtil.getApkInfo(this).width - DensityUtils.dip2px(this, 10 + 10)) *3/7;
			int h = DensityUtils.dip2px(this, 80);
			bitmap = UIHelper.makeRedBg2(this, w, h, getResources().getColor(colorId));
			redBgs.put(colorId, bitmap);
			CacheBean.getInstance().setRedBgs(redBgs);
		}
		else {
			bitmap = redBgs.get(colorId);
		}
		return bitmap;
	}


	private HttpCallBack httpCallback = new HttpCallBack(UseRedActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				List<Red> reds = new RedList(ret.getJSONArray("val")).getList();
				data = reds;
				int cashId = getIntent().getIntExtra("cashid", 0);
				if (cashId != 0) {
					for (int i = 0; i < reds.size(); i++) {
						if (reds.get(i).getId() == cashId) {
							data.get(i).setChecked(true);
							checkedid = cashId;
							String p = data.get(i).getCash_price();
							price = Integer.parseInt(p);
							hint.setText("已选1张，可抵扣" + p + "元");
							break;
						}
					}
				}
				Red lock = new Red();
				lock.setLock_flg(0.5f);
				data.add(lock);
				Red unlock = new Red();
				unlock.setLock_flg(-0.5f);
				data.add(unlock);
				Collections.sort(data);
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(UseRedActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
		}
	};

	public void onBackPressed() {
		finish();
	}
}
