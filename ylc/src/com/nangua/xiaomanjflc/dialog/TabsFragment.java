package com.nangua.xiaomanjflc.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.louding.frame.utils.DensityUtils;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.BaseViewBean;
import com.nangua.xiaomanjflc.ui.adapter.MyGridAdapter;
import com.nangua.xiaomanjflc.ui.adapter.MyGridAdapter.ItemCallBack;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TabsFragment extends Fragment implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onKeyBack();
		public void onSubmit(HashMap<String, String> values);
		
	}
	
	private CallBackDialogConfirm callback;
	private GridView limitTime; //期限
	private GridView gain;  //收益率
	private GridView repayMethod; //还款方式
	private GridView singlePurchase; //起投金额
	
	private MyGridAdapter limitTimeAdapter;
	private MyGridAdapter gainAdapter;
	private MyGridAdapter repayMethodAdapter;
	private MyGridAdapter singlePurchaseAdapter;
	private RelativeLayout searchContainer;
	
	private ArrayList<BaseViewBean> repayMethodList;
	private ArrayList<BaseViewBean> limitTimeList;
	private ArrayList<BaseViewBean> gainList;
	private ArrayList<BaseViewBean> singlePurchaseList;
	
	private Button confirm;
	
	private static ViewGroup vg;
	
	public TabsFragment() {
		super();
	}
	
	public TabsFragment(ViewGroup vg, CallBackDialogConfirm callback, int position) {
		super();
		this.callback = callback;
		TabsFragment.vg = vg;
		values = new HashMap<String, String>();
		values.put("minTerm", "0");
		values.put("maxTerm", "0");
		values.put("minRate", "0");
		values.put("maxRate", "0");
		values.put("recoverTag", "");
		values.put("minStart", "0");
		values.put("maxStart", "0");
	}
	
	public void show(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(vg.getId(), this);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
	}
	
	
	public void hide(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.remove(this);
		ft.commit();
	}
	
	private HashMap<String, String> values = new HashMap<String, String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_tabs, null);// 得到加载view
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT); 
		lp.setMargins(0, DensityUtils.dip2px(getActivity(), 40), 0, 0);
        v.setLayoutParams(lp);  
		searchContainer = (RelativeLayout) v.findViewById(R.id.searchContainer);
		//此处不知为何onclick 未能预先拦截到searchContainer的点击 而产生其他动作 暂时使用OnTouchListener优先拦截并消耗该事件
		searchContainer.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				callback.onKeyBack();
				return true;
			}
		});
		limitTime = (GridView) v.findViewById(R.id.limitTime);
		if (null == limitTimeList) {
			String[] arrLimitTime= getResources().getStringArray(
					R.array.search_limit_time);
			limitTimeList = new ArrayList<BaseViewBean>();
			for (int i = 0; i < arrLimitTime.length; i++) {
				BaseViewBean b = new BaseViewBean();
				b.setName(arrLimitTime[i]);
				limitTimeList.add(b);
			}
		}
		limitTimeAdapter = new MyGridAdapter(getActivity(), limitTimeList);
		limitTimeAdapter.setItemCallBack(new ItemCallBack() {
			
			@Override
			public void onItemClick(int position, BaseViewBean bean) {
				limitTimeList.get(position).setState(bean.getState());
				if (bean.getState() != 1) {
					values.put("minTerm", "0");
					values.put("maxTerm", "0");
					return;
				}
				if(position == 0){
					values.put("minTerm", "0");
					values.put("maxTerm", "1");
				} else if (position == 1){
					values.put("minTerm", "1");
					values.put("maxTerm", "3");
				} else if (position == 2){
					values.put("minTerm", "3");
					values.put("maxTerm", "6");
				} else if (position == 3){
					values.put("minTerm", "6");
					values.put("maxTerm", "0");
				} else {
					values.put("minTerm", "0");
					values.put("maxTerm", "0");
				}
				
			}
		});
		limitTime.setAdapter(limitTimeAdapter);
		
		
		gain = (GridView) v.findViewById(R.id.gain);
		if (null == gainList) {
			String[] arrGain= getResources().getStringArray(
					R.array.search_gain);
			gainList = new ArrayList<BaseViewBean>();
			for (int i = 0; i < arrGain.length; i++) {
				BaseViewBean b = new BaseViewBean();
				b.setName(arrGain[i]);
				gainList.add(b);
			}
		}
		gainAdapter = new MyGridAdapter(getActivity(), gainList);
		gainAdapter.setItemCallBack(new ItemCallBack() {
			
			@Override
			public void onItemClick(int position, BaseViewBean bean) {
				gainList.get(position).setState(bean.getState());
				if (bean.getState() != 1) {
					values.put("minRate", "0");
					values.put("maxRate", "0");
					return;
				}
				if (position == 0) {
					values.put("minRate", "3");
					values.put("maxRate", "7");
				}else if(position == 1){
					values.put("minRate", "7");
					values.put("maxRate", "10");
				} else if (position == 2){
					values.put("minRate", "10");
					values.put("maxRate", "12");
				} else if (position == 3){
					values.put("minRate", "12");
					values.put("maxRate", "15");
				}else if (position == 4){
					values.put("minRate", "15");
					values.put("maxRate", "24");
				}else {
					values.put("minRate", "0");
					values.put("maxRate", "0");
				}
				
			}
		});
		gain.setAdapter(gainAdapter);
		
		
		repayMethod = (GridView) v.findViewById(R.id.repayMethod);
		if (null == repayMethodList) {
			String[] arrRepayMethod= getResources().getStringArray(
					R.array.search_repay_method);
			repayMethodList = new ArrayList<BaseViewBean>();
			for (int i = 0; i < arrRepayMethod.length; i++) {
				BaseViewBean b = new BaseViewBean();
				b.setName(arrRepayMethod[i]);
				repayMethodList.add(b);
			}
		}
		repayMethodAdapter = new MyGridAdapter(getActivity(), repayMethodList);
		repayMethodAdapter.setItemCallBack(new ItemCallBack() {
			
			@Override
			public void onItemClick(int position, BaseViewBean bean) {
				repayMethodList.get(position).setState(bean.getState());
				if (bean.getState() != 1) {
					values.put("recoverTag", "");
					return;
				}
				if(position == 0){
					values.put("recoverTag", "3");
				} else if (position == 1){
					values.put("recoverTag", "4");
				} else if (position == 2){
					values.put("recoverTag", "2");
				} else {
					values.put("recoverTag", "");
				}
				
			}
		});
		repayMethod.setAdapter(repayMethodAdapter);
		
		singlePurchase = (GridView) v.findViewById(R.id.singlePurchase);
		if (null == singlePurchaseList) {
			String[] arrSinglePurchase= getResources().getStringArray(
					R.array.search_single_purchase);
			singlePurchaseList = new ArrayList<BaseViewBean>();
			for (int i = 0; i < arrSinglePurchase.length; i++) {
				BaseViewBean b = new BaseViewBean();
				b.setName(arrSinglePurchase[i]);
				singlePurchaseList.add(b);
			}
		}
		singlePurchaseAdapter = new MyGridAdapter(getActivity(), singlePurchaseList);
		singlePurchaseAdapter.setItemCallBack(new ItemCallBack() {
			
			@Override
			public void onItemClick(int position, BaseViewBean bean) {
				singlePurchaseList.get(position).setState(bean.getState());
				if (bean.getState() != 1) {
					values.put("minStart", "0");
					values.put("maxStart", "0");
					return;
				}
				if(position == 0){
					values.put("minStart", "99.99");
					values.put("maxStart", "100");
				} else if (position == 1){
					values.put("minStart", "100");
					values.put("maxStart", "500");
				} else if (position == 2){
					values.put("minStart", "500");
					values.put("maxStart", "0");
				} else {
					values.put("minStart", "0");
					values.put("maxStart", "0");
				}
				
			}
		});
		singlePurchase.setAdapter(singlePurchaseAdapter);
		
		confirm = (Button) v.findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
		return v;
	}
	
	
	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.confirm:
			callback.onSubmit(values);
			break;
		}
	}
	
	
}
