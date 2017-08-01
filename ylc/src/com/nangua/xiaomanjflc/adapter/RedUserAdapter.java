package com.nangua.xiaomanjflc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.adapter.ViewHolder.MyClickHandler;
import com.nangua.xiaomanjflc.bean.Red;
import com.nangua.xiaomanjflc.cache.CacheBean;

import java.util.ArrayList;
import java.util.List;

public abstract class RedUserAdapter extends BaseAdapter {

	protected List<Red> list;
	protected Context context;
	protected ListView listview;
	private LayoutInflater inflater;
	protected final int itemLayoutId;

	public RedUserAdapter(Context context, List<Red> mDatas, int itemLayoutId) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = mDatas;
		this.itemLayoutId = itemLayoutId;
	}

	public RedUserAdapter(Context context, int itemLayoutId) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.itemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Red getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		Red current = list.get(position);
		if (null != current) {
			if (current.getLock_flg() == -0.5f) {
				convertView = inflater.inflate(R.layout.item_red_anno, null, true);
				TextView i = (TextView) convertView.findViewById(R.id.title);
				i.setText("可使用现金券");
			}
			else if (current.getLock_flg() == 0.5f) {
				convertView = inflater.inflate(R.layout.item_red_anno, null, true);
				//若为最后一行则隐藏
				if (position == getCount() - 1) {
					convertView.setVisibility(View.GONE);
				}
				else {
					TextView i = (TextView) convertView.findViewById(R.id.title);
					i.setText("已锁定现金券");
				}
			}
			else {
				final ViewHolder holder = getViewHolder(position, convertView,
						parent);
				ImageView i = holder.getView(R.id.cash_check);
				ImageView l = holder.getView(R.id.bgimg);
				TextView a = holder.getView(R.id.active_time);
				TextView txtRed = holder.getView(R.id.txtRed);

				Bitmap redBg = CacheBean.getInstance().getRedBgs()
						.get(R.color.orange);
				Bitmap redGreyBg = CacheBean.getInstance().getRedBgs()
						.get(R.color.grey);
				if (current.getLock_flg() != 0) {
					// TODO 锁定
					l.setImageBitmap(redGreyBg);
					i.setVisibility(View.INVISIBLE);
					txtRed.setText("现金券(已锁定)");
				}
				else {
					holder.addClick(R.id.item);
					holder.setHandler(new MyClickHandler() {
			            @Override
			            public void viewClick(int id) {
			                click(id, list, position,holder);
			            }
			        });
					i.setVisibility(View.VISIBLE);
					l.setImageBitmap(redBg);
					if (current.isChecked()) {
						i.setImageResource(R.drawable.icon_red_check);
					}
					else {
						i.setImageResource(R.drawable.icon_red_check_none);
					}
				}
				holder.setText(R.id.txtRed, "现金券", false);
				holder.setText(R.id.cashId, current.getId() + "", false);
				holder.setText(R.id.cash_title, "有效时间：", false);
				holder.setText(R.id.get_time, current.getActive_time() + "至",
						false);
				a.setText(current.getExpire_time());
				a.setVisibility(View.VISIBLE);
				holder.setText(R.id.cash_price, current.getCash_price() + "元",
						false);
				holder.setText(R.id.cash_desc, current.getCash_desc(), false);
				return holder.getConvertView();
			}
		}
		return convertView;
	}

	public abstract void click(int id, List<Red> list, int position,
			ViewHolder viewHolder);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(context, convertView, parent, itemLayoutId,
				position);
	}

	public void setList(List<Red> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setList(Red[] list) {
		ArrayList<Red> arrayList = new ArrayList<Red>(list.length);
		for (Red t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}

	public List<Red> getList() {
		return list;
	}

}
