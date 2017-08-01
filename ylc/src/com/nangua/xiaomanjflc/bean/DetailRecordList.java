package com.nangua.xiaomanjflc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailRecordList {

    private List<DetailRecord> list;

    public DetailRecordList(JSONArray array) throws JSONException {
        super();
        list = new ArrayList<DetailRecord>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            DetailRecord a = new DetailRecord();
            JSONObject o = (JSONObject) array.get(i);
            a.setRealName(o.getString("realName"));
            a.setPrice(o.getString("price"));
            a.setCreateDate(o.getString("createDate"));
            list.add(a);
        }
    }

    public DetailRecordList(List<DetailRecord> li, JSONArray array) throws JSONException {
        super();
        list = li;
        int len = array.length();
        for (int i = 0; i < len; i++) {
            DetailRecord a = new DetailRecord();
            JSONObject o = (JSONObject) array.get(i);
            a.setRealName(o.getString("realName"));
            a.setPrice(o.getString("price"));
            a.setCreateDate(o.getString("createDate"));
            list.add(a);
        }
    }

    public List<DetailRecord> getList() {
        return list;
    }

    public void setList(List<DetailRecord> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        int len = list.size();
        String st = "";
        for (int i = 0; i < len; i++) {
            DetailRecord a = list.get(i);
            st += " /**" + i + a.toString();
        }
        return st;
    }

}
