package com.nangua.xiaomanjflc.adapter;

import java.util.List;

import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.Announce;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import wheel.adapters.AbstractWheelTextAdapter;

public class AnnoWheelAdapter extends AbstractWheelTextAdapter {

	private List<Announce> announces;
	
	
	/**
	 * Constructor
	 */
	public AnnoWheelAdapter(Context context, List<Announce> announces) {
		super(context, R.layout.item_wheel, NO_RESOURCE);
		this.announces = announces;
		setItemTextResource(R.id.txtWheelTitle);
	}
	
	public AnnoWheelAdapter(int resId, Context context, List<Announce> announces) {
		super(context, resId, NO_RESOURCE);
		this.announces = announces;
		setItemTextResource(R.id.txtWheelTitle);
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}

	@Override
	public int getItemsCount() {
		return announces.size();
	}

	@Override
	protected CharSequence getItemText(int index) {
		return announces.get(index).getTitle();
	}
	
}
