package com.nangua.xiaomanjflc.bean;

public class DetailRecord {

	private String realName;
	private String price;
	private String createDate;
	
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	@Override
	public String toString() {
		return "DetailRecord [realName=" + realName + ", price=" + price
				+ ", createDate=" + createDate + "]";
	}
	
}
