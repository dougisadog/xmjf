package com.nangua.xiaomanjflc.bean;

import java.io.Serializable;

public class Product implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -573884124894369635L;

	private int id; //产品ID
    
    private Long financeStartDate; //产品发布时间

    private int type; //产品类型 productType  -1原始  0壹财贷 1壹财宝

    private String url; //产品链接 productUrl

    private String name; //产品名称 name

    private int confine; //产品类型confine  0所有用户，1，新手专享，2，app专享，3，周末专享

    private String nameInfo; //产品名称 activityTab

    private int activity; //activity  0 nameInfo没有，1，nameInfo有

    private int activityType; //为3 下面有值

    private double extraRate;
    
    private String activityRate; //加息

    private String singlePurchaseLowerLimit; //最小起投数

    private String guaranteeModeName; //还款类型 本息担保

    private String remainingInvestmentAmount; //剩余投资量 remainingInvestmentAmount

    private String totalInvestment; //投资总量 totalInvestment

    private String gain; //预期年化收益 annualizedGain

    private String deadline; //投资时限 investmentPeriodDesc[0]

    private String deadlinedesc; //投资时限 investmentPeriodDesc[1]

    private int percentage; //百分比 investmentProgress

    private String repayMethod; //到期还本付息(还款方式) repaymentMethodName

    private int status; //产品状态

    private int newstatus;
    
    private String recommendTitle; //推荐标提示标语
    
    private String recommendBody; //推荐标提示标语

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getConfine() {
        return confine;
    }

    public void setConfine(int confine) {
        this.confine = confine;
    }

    public String getNameInfo() {
        return nameInfo;
    }

    public void setNameInfo(String nameInfo) {
        this.nameInfo = nameInfo;
    }

    public String getSinglePurchaseLowerLimit() {
        return singlePurchaseLowerLimit;
    }

    public void setSinglePurchaseLowerLimit(int singlePurchaseLowerLimit) {
        this.singlePurchaseLowerLimit = singlePurchaseLowerLimit/100+"";
    }

    public String getGuaranteeModeName() {
        return guaranteeModeName;
    }

    public void setGuaranteeModeName(String guaranteeModeName) {
        this.guaranteeModeName = guaranteeModeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNewstatus() {
        return newstatus;
    }

    public void setNewstatus(int newstatus) {
        this.newstatus = newstatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(String repayMethod) {
        this.repayMethod = repayMethod;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadlinedesc() {
        return deadlinedesc;
    }

    public void setDeadlinedesc(String deadlinedesc) {
        this.deadlinedesc = deadlinedesc;
    }

    public String getRemainingInvestmentAmount() {
        return remainingInvestmentAmount;
    }

    public void setRemainingInvestmentAmount(String remainingInvestmentAmount) {
        this.remainingInvestmentAmount = remainingInvestmentAmount;
    }

    public String getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(String totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public double getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(double extraRate) {
        this.extraRate = extraRate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", confine='" + confine + '\'' +
                ", nameInfo='" + nameInfo + '\'' +
                ", singlePurchaseLowerLimit='" + singlePurchaseLowerLimit + '\'' +
                ", guaranteeModeName='" + guaranteeModeName + '\'' +
                ", gain='" + gain + '\'' +
                ", deadline='" + deadline + '\'' +
                ", percentage=" + percentage +
                ", repayMethod='" + repayMethod + '\'' +
                ", status=" + status +
                ", newstatus=" + newstatus +
                '}';
    }

	public Long getFinanceStartDate() {
		return financeStartDate;
	}

	public void setFinanceStartDate(Long financeStartDate) {
		this.financeStartDate = financeStartDate;
	}

	public String getRecommendTitle() {
		return recommendTitle;
	}

	public void setRecommendTitle(String recommendTitle) {
		this.recommendTitle = recommendTitle;
	}

	public String getActivityRate() {
		return activityRate;
	}

	public void setActivityRate(String activityRate) {
		this.activityRate = activityRate;
	}

	public String getRecommendBody() {
		return recommendBody;
	}

	public void setRecommendBody(String recommendBody) {
		this.recommendBody = recommendBody;
	}


}
