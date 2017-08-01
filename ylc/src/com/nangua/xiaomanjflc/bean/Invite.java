package com.nangua.xiaomanjflc.bean;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by louding on 15/9/6.
 */
public class Invite {
    private int inv_type;
    private int ret_status;
    private int amount;
    private String create_time;
    private String name;

    public int getInv_type() {
        return inv_type;
    }

    public void setInv_type(int inv_type) {
        this.inv_type = inv_type;
    }

    public int getRet_status() {
        return ret_status;
    }

    public void setRet_status(int ret_status) {
        this.ret_status = ret_status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String sd = sdf.format(new Date(create_time * 1000));
        this.create_time = sd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "invite{" +
                "inv_type=" + inv_type +
                ", ret_status=" + ret_status +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
