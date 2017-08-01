package com.nangua.xiaomanjflc.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息中心
 * */
public class MessageCenterList {

	private List<MessageCenter> list;

	public MessageCenterList(JSONArray array) throws JSONException {
		super();
		list = new ArrayList<MessageCenter>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			MessageCenter messageCenter = new MessageCenter();
			JSONObject jsonObject = (JSONObject) array.get(i);
			messageCenter.setSubject(jsonObject.getString("subject"));// 标题
			messageCenter.setSender(jsonObject.getString("sender"));// 发送者
			messageCenter.setIns_date(jsonObject.getString("ins_date"));// 消息时间
			messageCenter.setContents(jsonObject.getString("contents"));// 消息内容
			messageCenter.setMsg_type(jsonObject.getString("msg_type"));// 消息类型
			messageCenter.setOpen_flg(jsonObject.getString("open_flg"));// 打开标志
			messageCenter.setDel_flg(jsonObject.getString("del_flg"));// 删除标志
			messageCenter.setId(jsonObject.getString("id"));// 消息ID
			list.add(messageCenter);
		}
	}

	public MessageCenterList(List<MessageCenter> list, JSONArray array)
			throws JSONException {
		super();
		this.list = list;
		list = new ArrayList<MessageCenter>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			MessageCenter messageCenter = new MessageCenter();
			JSONObject jsonObject = (JSONObject) array.get(i);
			messageCenter.setSubject(jsonObject.getString("subject"));// 标题
			messageCenter.setSender(jsonObject.getString("sender"));// 发送者
			messageCenter.setIns_date(jsonObject.getString("ins_date"));// 消息时间
			messageCenter.setContents(jsonObject.getString("contents"));// 消息内容
			messageCenter.setMsg_type(jsonObject.getString("msg_type"));// 消息类型
			messageCenter.setOpen_flg(jsonObject.getString("open_flg"));// 打开标志
			messageCenter.setDel_flg(jsonObject.getString("del_flg"));// 删除标志
			messageCenter.setId(jsonObject.getString("id"));// 消息ID
			this.list.add(messageCenter);
		}
	}

	public List<MessageCenter> getList() {
		return list;
	}

	public void setList(List<MessageCenter> list) {
		this.list = list;
	}

}
