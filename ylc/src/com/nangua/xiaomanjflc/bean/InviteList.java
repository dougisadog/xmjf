package com.nangua.xiaomanjflc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louding on 15/9/6.
 */
public class InviteList {

    private List<Invite> list;

    public InviteList(JSONArray array) throws JSONException {
        super();
        list = new ArrayList<Invite>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            Invite a = new Invite();
            JSONObject o = (JSONObject) array.get(i);
            a.setCreate_time(o.getLong("create_time"));
            a.setInv_type(o.getInt("inv_type"));
            a.setRet_status(o.getInt("ret_status"));
            a.setAmount(o.getInt("amount"));
            a.setName(o.getString("name"));
            list.add(a);
        }
    }

    public InviteList(List<Invite> li, JSONArray array) throws JSONException {
        super();
        list = li;
        int len = array.length();
        for (int i = 0; i < len; i++) {
            Invite a = new Invite();
            JSONObject o = (JSONObject) array.get(i);
            a.setCreate_time(o.getLong("create_time"));
            a.setInv_type(o.getInt("inv_type"));
            a.setRet_status(o.getInt("ret_status"));
            a.setAmount(o.getInt("amount"));
            a.setName(o.getString("name"));
            list.add(a);
        }
    }

    public List<Invite> getList() {
        return list;
    }

    public void setList(List<Invite> list) {
        this.list = list;
    }

}
