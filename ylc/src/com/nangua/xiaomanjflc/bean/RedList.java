package com.nangua.xiaomanjflc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.louding.frame.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RedList {

	private List<Red> list;

	public RedList(JSONArray array) throws JSONException {
		super();
		list = new ArrayList<Red>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Red a = new Red();
			JSONObject o = (JSONObject) array.get(i);
//			if (0 != o.getInt("lock_flg")) continue;
//			if (o.has("lock_flg"))
//				try {
//					a.setLock_flg(o.getInt("lock_flg"));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			if (o.has("lock_flg")) {
				a.setLock_flg(o.getInt("lock_flg"));
			}
			
			a.setActive_time(o.getLong("active_time"));
			a.setCach_Desc(o.getString("cash_desc"));
			a.setCash_price(o.getInt("cash_price"));
			
			if( "2".equals(o.getString("term_type")) ){
				a.setValid_days(o.getString("valid_days"));
				a.setExpire_time(o.getLong("active_time"), Integer.parseInt(o.getString("valid_days")));
			}else {
				a.setValid_days("0");
				a.setExpire_time(o.getString("end_date"));
			}
				
			
			
			a.setUsed_time(o.getLong("used_time"));
			a.setId(o.getInt("id"));
			a.setChecked(false);
			list.add(a);
		}
	}

	public RedList(List<Red> li, JSONArray array) throws JSONException {
		super();
		list = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Red a = new Red();
			JSONObject o = (JSONObject) array.get(i);
			if (o.has("lock_flg")) {
				a.setLock_flg(o.getInt("lock_flg"));
			}
//			if (o.has("lock_flg"))
//				try {
//					a.setLock_flg(o.getInt("lock_flg"));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			a.setActive_time(o.getLong("active_time"));
			a.setCach_Desc(o.getString("cash_desc"));
			a.setCash_price(o.getInt("cash_price"));
			if( "2".equals(o.getString("term_type")) ){
				a.setValid_days(o.getString("valid_days"));
				a.setExpire_time(o.getLong("active_time"), Integer.parseInt(o.getString("valid_days")));
			}else {
				a.setValid_days("0");
				a.setExpire_time(o.getString("end_date"));
			}
			a.setUsed_time(o.getLong("used_time"));
			a.setId(o.getInt("id"));
			a.setChecked(false);
			list.add(a);
		}
	}

	public List<Red> getList() {
		return list;
	}

	public void setList(List<Red> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		int len = list.size();
		String st = "";
		for (int i = 0; i < len; i++) {
			Red a = list.get(i);
			st += " /**" + i + a.toString();
		}
		return st;
	}

}
