package com.nangua.xiaomanjflc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransactionList {

	private List<Transaction> list;

	public TransactionList(JSONArray array) throws JSONException {
		super();
		list = new ArrayList<Transaction>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Transaction a = new Transaction();
			JSONObject o = (JSONObject) array.get(i);
			a.setTransactionId(o.getLong("transactionId"));
			a.setBeginningBalance(o.getString("beginningBalance"));
			a.setOperationAmount(o.getString("operationAmount"));
			a.setAvailable(o.getString("available"));
			a.setCreateTime(o.getString("createTime"));
			a.setTransactionType(o.getString("transactionType"));
			list.add(a);
		}
	}

	public TransactionList(List<Transaction> li, JSONArray array)
			throws JSONException {
		super();
		list = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Transaction a = new Transaction();
			JSONObject o = (JSONObject) array.get(i);
			a.setTransactionId(o.getLong("transactionId"));
			a.setBeginningBalance(o.getString("beginningBalance"));
			a.setOperationAmount(o.getString("operationAmount"));
			a.setAvailable(o.getString("available"));
			a.setCreateTime(o.getString("createTime"));
			a.setTransactionType(o.getString("transactionType"));
			list.add(a);
		}
	}

	public List<Transaction> getList() {
		return list;
	}

	public void setList(List<Transaction> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		int len = list.size();
		String st = "";
		for (int i = 0; i < len; i++) {
			Transaction a = list.get(i);
			st += " /**" + i + a.toString();
		}
		return st;
	}

}
