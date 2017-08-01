package com.nangua.xiaomanjflc.bean.jsonbean;

/**
 * 账户信息
 * @author Doug
 *
 */
public class GainData {
	
	private int totalInterest;//累计收益
	
	private int unrepaidInterest; //预计收益
	
	private int available; //可用余额
	
	private int investAmount;//投资金额
	
	private int frozeAmount; //冻结金额
	
	private int noreadmessage; //未读消息

	public int getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(int totalInterest) {
		this.totalInterest = totalInterest;
	}

	public int getUnrepaidInterest() {
		return unrepaidInterest;
	}

	public void setUnrepaidInterest(int unrepaidInterest) {
		this.unrepaidInterest = unrepaidInterest;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(int investAmount) {
		this.investAmount = investAmount;
	}

	public int getFrozeAmount() {
		return frozeAmount;
	}

	public void setFrozeAmount(int frozeAmount) {
		this.frozeAmount = frozeAmount;
	}

	public int getNoreadmessage() {
		return noreadmessage;
	}

	public void setNoreadmessage(int noreadmessage) {
		this.noreadmessage = noreadmessage;
	}

}
