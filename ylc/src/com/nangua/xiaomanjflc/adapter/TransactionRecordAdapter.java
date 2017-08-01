package com.nangua.xiaomanjflc.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nangua.xiaomanjflc.bean.Transaction;
import com.nangua.xiaomanjflc.R;

public class TransactionRecordAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private List<Transaction> transactions;
	
	private Context context;

	public TransactionRecordAdapter(Context context,
			List<Transaction> transactions) {
		this.layoutInflater = LayoutInflater.from(context);
		this.transactions = transactions;
		this.context = context;
	}

	@Override
	public int getCount() {
		return transactions.size();
	}

	@Override
	public Object getItem(int position) {
		return transactions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (transactions.get(position).getDateflag() != null) {
			convertView = layoutInflater.inflate(
					R.layout.item_transaction_record_date, null);
			holder = new ViewHolder();
			holder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);

			holder.tv_date.setText(transactions.get(position).getDateflag());
		} else {
			convertView = layoutInflater.inflate(
					R.layout.item_transaction_record, null);
			holder = (ViewHolder) convertView.getTag();
			if (null == holder) {
				holder = new ViewHolder();
				holder.transactionId = (TextView) convertView
						.findViewById(R.id.transactionId);
				holder.amount = (TextView) convertView
						.findViewById(R.id.amount);
				holder.createTime = (TextView) convertView
						.findViewById(R.id.createTime);
				holder.transactionType = (TextView) convertView
						.findViewById(R.id.transactionType);
				convertView.setTag(holder);
			}
			

			holder.transactionId.setText("交易号："
					+ transactions.get(position).getTransactionId());
			if (transactions.get(position).getOperationAmount().startsWith("+")) {
//				holder.amount.setTextColor(Color.rgb(61, 145, 64));
				holder.amount.setTextColor(context.getResources().getColor(R.color.orange));
			} else if (transactions.get(position).getOperationAmount()
					.startsWith("-")) {
//				holder.amount.setTextColor(Color.rgb(253, 153, 18));
				holder.amount.setTextColor(context.getResources().getColor(R.color.green));
			} else {
//				holder.amount.setTextColor(Color.rgb(61, 145, 64));
				holder.amount.setTextColor(context.getResources().getColor(R.color.orange));
			}
			holder.amount.setText(transactions.get(position)
					.getOperationAmount());
			try {
				SimpleDateFormat sdfold = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = sdfold.parse(transactions.get(position)
						.getCreateTime());
				SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				holder.createTime.setText(sdfnew.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			holder.transactionType.setText(transactions.get(position)
					.getTransactionType());

		}

		return convertView;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public static class ViewHolder {
		public TextView transactionId;
		public TextView amount;
		public TextView createTime;
		public TextView transactionType;
		public TextView tv_date;
	}

}