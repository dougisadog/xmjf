package com.nangua.xiaomanjflc.ui.fragment;

import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.ui.myabstract.VerticalScrollFragment;
import com.nangua.xiaomanjflc.widget.SmartScrollView;
import com.nangua.xiaomanjflc.widget.SmartScrollView.ISmartScrollChangedListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class BaseTextFragment extends VerticalScrollFragment{

	private View v;

	private String mContent;
	
	private TextView content;
	
	private SmartScrollView sv;
	
	public BaseTextFragment() {
	}
	
	public BaseTextFragment (String mContent, ScollCallBack scollCallBack){
		this.mContent = mContent;
		this.scollCallBack = scollCallBack;
	};
	
	public BaseTextFragment (String mContent){
		this.mContent = mContent;
	};
	
	private Handler handler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initialed = true;
		
		v = inflater.inflate(R.layout.fragment_base_text, null);
		sv = (SmartScrollView) v.findViewById(R.id.sv);
		sv.setScanScrollChangedListener(new ISmartScrollChangedListener() {
			
			@Override
			public void onScrolledToTop() {
				if (null != scollCallBack) {
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							//经过优化后舍弃
//							if (sv.isScrolledToTop())
//								scollCallBack.onScrollTop();
						}
					}, 300);
				}
			}
			
			@Override
			public void onScrolledToBottom() {
				if (null != scollCallBack) {
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							if (sv.isScrolledToBottom())
								scollCallBack.onScrollBottom();
						}
					}, 300);
				}
			}
		});
		content = (TextView) v.findViewById(R.id.content);
		content.setText(mContent);
		
		return v;
	}

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub
		
	}
	
}
