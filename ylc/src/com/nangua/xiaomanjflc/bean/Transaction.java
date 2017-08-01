package com.nangua.xiaomanjflc.bean;

public class Transaction {

	private String transactionId; // 流水账号
	private String operationAmount; // 操作金额
	private String beginningBalance; // 操作前金额
	private String available; // 可用余额
	private String createTime; // 时间
	private String transactionType; // 交易类型
	private String dateflag;

	public String getDateflag() {
		return dateflag;
	}

	public void setDateflag(String dateflag) {
		this.dateflag = dateflag;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId + "";
	}

	public String getOperationAmount() {
		return operationAmount;
	}

	public void setOperationAmount(String operationAmount) {
		this.operationAmount = operationAmount;
	}

	public String getBeginningBalance() {
		return beginningBalance;
	}

	public void setBeginningBalance(String beginningBalance) {
		this.beginningBalance = beginningBalance;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId
				+ ", operationAmount=" + operationAmount
				+ ", beginningBalance=" + beginningBalance + ", available="
				+ available + ", createTime=" + createTime
				+ ", transactionType=" + transactionType + "]";
	}

}
