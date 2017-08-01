package com.nangua.xiaomanjflc.ui.adapter;

import java.util.ArrayList;

import com.louding.frame.utils.DensityUtils;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.BaseViewBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter {  
    private ArrayList<BaseViewBean> mNameList = new ArrayList<BaseViewBean>();  
    private LayoutInflater mInflater;  
    private Context mContext;  
    AbsListView.LayoutParams params;  
    
    private ItemCallBack itemCallBack;
    
    public void setItemCallBack(ItemCallBack itemCallBack) {
    	this.itemCallBack = itemCallBack;
    }
    
    public interface ItemCallBack {
    	public void onItemClick(int positon ,BaseViewBean bean);
    }
    
    public MyGridAdapter(Context context, ArrayList<BaseViewBean> nameList) {  
        mNameList = nameList;  
        mContext = context;  
        mInflater = LayoutInflater.from(context);  
          
        int h = DensityUtils.dip2px(context, 45);
        params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, h);  
    }
    
    public void setDatas(ArrayList<BaseViewBean> nameList) {
    	 this.mNameList = nameList;
    	 notifyDataSetChanged();
    }
    
    
    public int getCount() {  
        return mNameList.size();  
    }  
  
    public BaseViewBean getItem(int position) {  
        return mNameList.get(position);  
    }  
  
    public long getItemId(int position) {  
        return position;  
    }  
  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        ItemViewTag viewTag;  
          
        if (convertView == null)  
        {  
            convertView = mInflater.inflate(R.layout.item_search_tab, null);  
              
            // construct an item tag   
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.tabImg), (TextView) convertView.findViewById(R.id.tabName));  
            convertView.setTag(viewTag);  
        } else  
        {  
            viewTag = (ItemViewTag) convertView.getTag();  
        }  
         
        //OnClickListener 以及gridview的onitem响应效果较差 使用ontouch 并在gridview中
        //增肌android:listSelector="@null" 来消除ontouch的view影响
        convertView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN :
						refresh(position);
						break;

					default :
						break;
				}
				return false;
			}
		});
//        convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				refresh(position);
//			}
//		});
        if (mNameList.get(position).getState() == 1) {
        	 viewTag.mName.setBackgroundResource(R.drawable.btn_blue);
             viewTag.mName.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        else {
        	 viewTag.mName.setBackgroundResource(R.drawable.btn_empty);
             viewTag.mName.setTextColor(mContext.getResources().getColor(R.color.grey_btn));
        }
        // set name   
        convertView.setLayoutParams(params);
        viewTag.mName.setText(mNameList.get(position).getName());  
        return convertView;  
    } 
    
    private void refresh(int position) {
    	for (int i = 0; i < mNameList.size(); i++) {
			if (mNameList.get(position).getState() != 1 && i == position) {
				mNameList.get(i).setState(1);
			}
			else {
				mNameList.get(i).setState(0);
			}
		}
		if (null != itemCallBack) {
			notifyDataSetChanged();
			itemCallBack.onItemClick(position ,mNameList.get(position));
		}
    }
      
    class ItemViewTag  
    {  
        protected ImageView mIcon;  
        protected TextView mName;  
          
        /** 
         * The constructor to construct a navigation view tag 
         *  
         * @param name 
         *            the name view of the item 
         * @param size 
         *            the size view of the item 
         * @param icon 
         *            the icon view of the item 
         */  
        public ItemViewTag(ImageView icon, TextView name)  
        {  
            this.mName = name;  
            this.mIcon = icon;  
        }  
    }  
  
}  
