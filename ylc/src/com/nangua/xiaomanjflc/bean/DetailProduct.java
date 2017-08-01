package com.nangua.xiaomanjflc.bean;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nangua.xiaomanjflc.utils.FormatUtils;

public class DetailProduct {

	// 借款人信息类型标题
	private String personTypeTitle;
	// 借款方区分 0:无 1:借款人信息 2:原始借款人信息 3:原始借款企业借款信息
	private String personTypeKbn;
	// 审核信息区分 0:无 1:个人或者原始借款人审核信息 2:原始企业审核信息
	private String checkInfoFlg;

	// 产品信息部分
	private int id;// 商品id
	private String name;
	private int status;
	private int newstatus;
	private String totalInvestment; // 总投资额 单位是分
	private String totalInvestmentunit; // 总投资额 单位是分
	private String investmentPeriodDesc; // 投资时限
	private String investmentPeriodDescunit; // 投资时限
	private String annualizedGain; // 预计年化收益率
	private String tenderAward; // 加息
	private String guaranteeModeName; // 保障方式
	private String repaymentMethodName; // 还款方式
	private int investmentProgress; // 百分比
	private String expirationDate; // 投标截止剩余时间
	
	private String endDay; //截止日期 yyyy-mm-dd
	private String interestBeginDate; // 计息开始
	private String remainingInvestmentAmount; // 可投 单位分
	private String singlePurchaseLowerLimit; // 起投 单位分
	private String baseLimitAmount; // （输入此字段的整数倍，单位是分）

	// 项目简介
	private String detailDescription; // 项目简介
	// 担保方介绍
	private String description; // 担保方介绍
	// 安全保障描述
	private String descriptionRiskDescri; // 安全保障描述
	// 个人信息
	private String username; // 用户
	private String gender; // 性别
	private String age; // 年龄
	private String purpose; // 资金用途
	// 企业信息
	private String gCompanyName; // 企业名称
	private String gLegalPerson; // 法人代表
	private String gRegisteredCapital; // 注册资本
	private String gIndustry; // 所属行业
	private String gPurpose1; // 资金用途
	private String gCompanyIntroduce; // 企业介绍
	// 个人审核
	private boolean idCardCheckFlg;// 身份证
	private boolean estateCheckFlg;// 房产证
	private boolean marriageCheckFlg;// 婚姻证明
	private boolean householdCheckFlg;// 户口本
	private boolean credibilityCheckFlg;// 信用报告
	private boolean guaranteeCheckFlg;// 机构担保审核
	// 企业审核
	private boolean businessCheckFlg;// 营业执照
	private boolean legalCardCheckFlg;// 法人身份证
	private boolean bondingCheckFlg;// 机构担保
	private boolean platformCheckFlg;// 平台审核
	private boolean addressCheckFlg;// 营业场所审核

	public DetailProduct(JSONObject o) throws JSONException {
		super();

		personTypeTitle = o.getString("personTypeTitle");
		personTypeKbn = o.getString("personTypeKbn");
		checkInfoFlg = o.getString("checkInfoFlg");

		// 个人信息
		JSONObject pApplication = o.getJSONObject("pApplication");
		username = pApplication.getString("pFinancePersonName");
		gender = pApplication.getString("pFinancePersonSex");
		age = pApplication.getString("pFinancePersonAge");
		purpose = pApplication.getString("pPurpose");
		// 企业信息
		JSONObject gApplication = o.getJSONObject("gApplication");
		gCompanyName = gApplication.getString("gCompanyName");
		gLegalPerson = gApplication.getString("gLegalPerson");
		gRegisteredCapital = gApplication.getString("gRegisteredCapital");
		gIndustry = gApplication.getString("gIndustry");
		gPurpose1 = gApplication.getString("gPurpose1");
		gCompanyIntroduce = gApplication.getString("gCompanyIntroduce");

		// 个人审核
		JSONObject pApplicationCheck = o.getJSONObject("pApplicationCheck");
		idCardCheckFlg = pApplicationCheck.getBoolean("idCardCheckFlg");
		estateCheckFlg = pApplicationCheck.getBoolean("estateCheckFlg");
		marriageCheckFlg = pApplicationCheck.getBoolean("marriageCheckFlg");
		householdCheckFlg = pApplicationCheck.getBoolean("householdCheckFlg");
		credibilityCheckFlg = pApplicationCheck
				.getBoolean("credibilityCheckFlg");
		guaranteeCheckFlg = pApplicationCheck.getBoolean("guaranteeCheckFlg");

		// 企业审核
		JSONObject gApplicationCheck = o.getJSONObject("gApplicationCheck");
		businessCheckFlg = gApplicationCheck.getBoolean("businessCheckFlg");
		legalCardCheckFlg = gApplicationCheck.getBoolean("legalCardCheckFlg");
		bondingCheckFlg = gApplicationCheck.getBoolean("bondingCheckFlg");
		platformCheckFlg = gApplicationCheck.getBoolean("platformCheckFlg");
		addressCheckFlg = gApplicationCheck.getBoolean("addressCheckFlg");

		// 产品信息部分
		JSONObject product = o.getJSONObject("product");
		id = product.getInt("id");
		name = product.getString("name");
		status = product.getInt("status");
		newstatus = product.getInt("newstatus");
		totalInvestment = FormatUtils.getNewAmount(
				product.getString("totalInvestment")).get(0);
		totalInvestmentunit = FormatUtils.getNewAmount(
				product.getString("totalInvestment")).get(1);
		JSONArray a = product.getJSONArray("investmentPeriodDesc");
		investmentPeriodDesc = a.get(0) + "";
		investmentPeriodDescunit = a.get(1) + "";
		
		String gain = product.getString("annualizedGain");
		annualizedGain = FormatUtils.getSimpleNum(gain);
		
		//.00结尾屏蔽
		String myRate = product.getString("tenderAward");
		tenderAward = FormatUtils.getSimpleNum(myRate);
		guaranteeModeName = product.getString("guaranteeModeName");
		repaymentMethodName = product.getString("repaymentMethodName");
		investmentProgress = product.getInt("investmentProgress");
		expirationDate = product.getString("expirationDate");
		
		try {
			if (product.has("endDay")) {
				java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
				setEndDay(format1.format(new Date(product.getLong("endDay"))));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		expirationDate = expirationDate.split(" ")[0];
		interestBeginDate = product.getString("interestBeginDate");
		interestBeginDate = interestBeginDate.split(" ")[0];
		remainingInvestmentAmount = FormatUtils.getAmount(product
				.getString("remainingInvestmentAmount"));
		singlePurchaseLowerLimit = FormatUtils.getAmount(product
				.getString("singlePurchaseLowerLimit"));
		baseLimitAmount = product.getString("baseLimitAmount");
		detailDescription = product.getString("detailDescription");// 贷款描述
		description = product.getString("description");// 担保方介绍
		descriptionRiskDescri = product.getString("descriptionRiskDescri");// 安全保障描述

	}

	public String getPersonTypeTitle() {
		return personTypeTitle;
	}

	public void setPersonTypeTitle(String personTypeTitle) {
		this.personTypeTitle = personTypeTitle;
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

	public boolean isIdCardCheckFlg() {
		return idCardCheckFlg;
	}

	public void setIdCardCheckFlg(boolean idCardCheckFlg) {
		this.idCardCheckFlg = idCardCheckFlg;
	}

	public boolean isEstateCheckFlg() {
		return estateCheckFlg;
	}

	public void setEstateCheckFlg(boolean estateCheckFlg) {
		this.estateCheckFlg = estateCheckFlg;
	}

	public boolean isMarriageCheckFlg() {
		return marriageCheckFlg;
	}

	public void setMarriageCheckFlg(boolean marriageCheckFlg) {
		this.marriageCheckFlg = marriageCheckFlg;
	}

	public boolean isHouseholdCheckFlg() {
		return householdCheckFlg;
	}

	public void setHouseholdCheckFlg(boolean householdCheckFlg) {
		this.householdCheckFlg = householdCheckFlg;
	}

	public boolean isCredibilityCheckFlg() {
		return credibilityCheckFlg;
	}

	public void setCredibilityCheckFlg(boolean credibilityCheckFlg) {
		this.credibilityCheckFlg = credibilityCheckFlg;
	}

	public boolean isGuaranteeCheckFlg() {
		return guaranteeCheckFlg;
	}

	public void setGuaranteeCheckFlg(boolean guaranteeCheckFlg) {
		this.guaranteeCheckFlg = guaranteeCheckFlg;
	}

	public boolean isBusinessCheckFlg() {
		return businessCheckFlg;
	}

	public void setBusinessCheckFlg(boolean businessCheckFlg) {
		this.businessCheckFlg = businessCheckFlg;
	}

	public boolean isLegalCardCheckFlg() {
		return legalCardCheckFlg;
	}

	public void setLegalCardCheckFlg(boolean legalCardCheckFlg) {
		this.legalCardCheckFlg = legalCardCheckFlg;
	}

	public boolean isBondingCheckFlg() {
		return bondingCheckFlg;
	}

	public void setBondingCheckFlg(boolean bondingCheckFlg) {
		this.bondingCheckFlg = bondingCheckFlg;
	}

	public boolean isPlatformCheckFlg() {
		return platformCheckFlg;
	}

	public void setPlatformCheckFlg(boolean platformCheckFlg) {
		this.platformCheckFlg = platformCheckFlg;
	}

	public boolean isAddressCheckFlg() {
		return addressCheckFlg;
	}

	public void setAddressCheckFlg(boolean addressCheckFlg) {
		this.addressCheckFlg = addressCheckFlg;
	}

	public int getStatus() {
		return status;
	}

	public String getCheckInfoFlg() {
		return checkInfoFlg;
	}

	public void setCheckInfoFlg(String checkInfoFlg) {
		this.checkInfoFlg = checkInfoFlg;
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

	public String getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(String totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public String getTotalInvestmentunit() {
		return totalInvestmentunit;
	}

	public void setTotalInvestmentunit(String totalInvestmentunit) {
		this.totalInvestmentunit = totalInvestmentunit;
	}

	public String getInvestmentPeriodDesc() {
		return investmentPeriodDesc;
	}

	public void setInvestmentPeriodDesc(String investmentPeriodDesc) {
		this.investmentPeriodDesc = investmentPeriodDesc;
	}

	public String getInvestmentPeriodDescunit() {
		return investmentPeriodDescunit;
	}

	public void setInvestmentPeriodDescunit(String investmentPeriodDescunit) {
		this.investmentPeriodDescunit = investmentPeriodDescunit;
	}

	public String getAnnualizedGain() {
		return annualizedGain;
	}

	public void setAnnualizedGain(String annualizedGain) {
		this.annualizedGain = annualizedGain;
	}

	public String getTenderAward() {
		return tenderAward;
	}

	public void setTenderAward(String tenderAward) {
		this.tenderAward = tenderAward;
	}

	public String getGuaranteeModeName() {
		return guaranteeModeName;
	}

	public void setGuaranteeModeName(String guaranteeModeName) {
		this.guaranteeModeName = guaranteeModeName;
	}

	public String getRepaymentMethodName() {
		return repaymentMethodName;
	}

	public void setRepaymentMethodName(String repaymentMethodName) {
		this.repaymentMethodName = repaymentMethodName;
	}

	public int getInvestmentProgress() {
		return investmentProgress;
	}

	public void setInvestmentProgress(int investmentProgress) {
		this.investmentProgress = investmentProgress;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getInterestBeginDate() {
		return interestBeginDate;
	}

	public void setInterestBeginDate(String interestBeginDate) {
		this.interestBeginDate = interestBeginDate;
	}

	public String getRemainingInvestmentAmount() {
		return remainingInvestmentAmount;
	}

	public void setRemainingInvestmentAmount(String remainingInvestmentAmount) {
		this.remainingInvestmentAmount = remainingInvestmentAmount;
	}

	public String getSinglePurchaseLowerLimit() {
		return singlePurchaseLowerLimit;
	}

	public void setSinglePurchaseLowerLimit(String singlePurchaseLowerLimit) {
		this.singlePurchaseLowerLimit = singlePurchaseLowerLimit;
	}

	public String getBaseLimitAmount() {
		return baseLimitAmount;
	}

	public void setBaseLimitAmount(String baseLimitAmount) {
		this.baseLimitAmount = baseLimitAmount;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionRiskDescri() {
		return descriptionRiskDescri;
	}

	public void setDescriptionRiskDescri(String descriptionRiskDescri) {
		this.descriptionRiskDescri = descriptionRiskDescri;
	}

	public String getPersonTypeKbn() {
		return personTypeKbn;
	}

	public void setPersonTypeKbn(String personTypeKbn) {
		this.personTypeKbn = personTypeKbn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public String getgCompanyName() {
		return gCompanyName;
	}

	public void setgCompanyName(String gCompanyName) {
		this.gCompanyName = gCompanyName;
	}

	public String getgLegalPerson() {
		return gLegalPerson;
	}

	public void setgLegalPerson(String gLegalPerson) {
		this.gLegalPerson = gLegalPerson;
	}

	public String getgRegisteredCapital() {
		return gRegisteredCapital;
	}

	public void setgRegisteredCapital(String gRegisteredCapital) {
		this.gRegisteredCapital = gRegisteredCapital;
	}

	public String getgIndustry() {
		return gIndustry;
	}

	public void setgIndustry(String gIndustry) {
		this.gIndustry = gIndustry;
	}

	public String getgPurpose1() {
		return gPurpose1;
	}

	public void setgPurpose1(String gPurpose1) {
		this.gPurpose1 = gPurpose1;
	}

	public String getgCompanyIntroduce() {
		return gCompanyIntroduce;
	}

	public void setgCompanyIntroduce(String gCompanyIntroduce) {
		this.gCompanyIntroduce = gCompanyIntroduce;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}


}
