package com.nangua.xiaomanjflc.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.louding.frame.KJHttp;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.adapter.fragment.HomeFragmentAdapter;
import com.nangua.xiaomanjflc.bean.DetailProduct;
import com.nangua.xiaomanjflc.bean.Product;
import com.nangua.xiaomanjflc.support.UpdateManager;
import com.nangua.xiaomanjflc.ui.myabstract.HomeFragment;
import com.nangua.xiaomanjflc.ui.myabstract.VerticalScrollFragment.ScollCallBack;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.widget.FontTextView;
import com.nangua.xiaomanjflc.widget.TitleTab;
import com.nangua.xiaomanjflc.widget.TitleTab.ItemCallBack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragmentIntroduction extends HomeFragment{

	private View v;

	private ViewPager viewpager;
	private List<HomeFragment> homeFragments;
	private HomeFragmentAdapter adapter;
	private TitleTab titleTab;
	private DetailProduct product;
	
	private ScollCallBack scollCallBack;
	
	public DetailFragmentIntroduction() {
	}
	
	public DetailFragmentIntroduction(DetailProduct product) {
		this.product = product;
	}
	
	public DetailFragmentIntroduction(DetailProduct product, ScollCallBack scollCallBack) {
		this.product = product;
		this.scollCallBack = scollCallBack;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initialed = true;
		
		v = inflater.inflate(R.layout.fragment_detail_introduction, null);
		initView();
		initData();
		return v;
	}
	
	private void initView() {
		viewpager = (ViewPager) v.findViewById(R.id.viewpager);
		homeFragments = new ArrayList<HomeFragment>();
		
		// 担保方介绍
		homeFragments.add(new BaseTextFragment(product.getDetailDescription(), scollCallBack));
		// 项目简介
		homeFragments.add(new BaseTextFragment(product.getDescription(), scollCallBack));
		// 安全保障
		homeFragments.add(new BaseTextFragment(product.getDescriptionRiskDescri(), scollCallBack));
		// 投标记录
		homeFragments.add(new ProductRecordFragment(scollCallBack));
		adapter = new HomeFragmentAdapter(getFragmentManager(), homeFragments);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < titleTab.getChildCount(); i++) {
					TextView tv = titleTab.getTextView(i);
					if (null != tv)
						tv.setTextColor(getResources().getColor(position == i ? R.color.orange : R.color.grey));
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
 		titleTab = (TitleTab) v.findViewById(R.id.mytab);
		final List<String> names = new ArrayList<String>();
		names.add("项目简介");
		names.add("担保介绍");
		names.add("安全保证");
		names.add("投标记录");
		titleTab.setDatas(names, new ItemCallBack() {

			@Override
			public void onItemClicked(int positon) {

				for (int i = 0; i < titleTab.getChildCount(); i++) {
					viewpager.setCurrentItem(positon);
					TextView tv = titleTab.getTextView(i);
					if (null != tv)
						tv.setTextColor(getResources().getColor(positon == i ? R.color.orange : R.color.grey));
				}
			}

		});
		
		titleTab.clickItem(0);
	}

	private void initData() {
	}


	public void refreshData() {
		if (!initialed) return;
	}

}
