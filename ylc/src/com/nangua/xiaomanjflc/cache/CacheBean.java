package com.nangua.xiaomanjflc.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.kymjs.kjframe.http.Cache;

import com.louding.frame.KJDB;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.bean.DetailProduct;
import com.nangua.xiaomanjflc.bean.MainAD;
import com.nangua.xiaomanjflc.bean.ShopADData;
import com.nangua.xiaomanjflc.bean.database.UPushMessage;
import com.nangua.xiaomanjflc.bean.jsonbean.Account;
import com.nangua.xiaomanjflc.bean.jsonbean.GainData;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.support.ApkInfo;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class CacheBean {
	
	private static CacheBean instance = null;
	
	public static CacheBean getInstance() {
		if (null == instance) {
			instance = new CacheBean();
		}
		return instance;
	}
	
	public void init(Context context) {
		ApplicationUtil.getApkInfo(context);
		KJDB kjdb = KJDB.create(context);
		List<UPushMessage> msgs = kjdb.findAll(UPushMessage.class, "showed DESC, receiveTime DESC");
		if (msgs.size() > 0) {
			setMsg(msgs.get(0));
		}
	}
	
	public void clear() {
		user = null;
		account = null;
	}
	
	//清空webcookie数据
	public static void syncCookie(Context context) {
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//移除  
	    CookieSyncManager.getInstance().sync();  
	}
	
	/**
	 * 个人信息是否需要更新
	 * @return
	 */
	public static boolean checkNeedUpdate() {
		User user = getInstance().getUser();
		if (null == user || AppVariables.uid != Integer.parseInt(user.getUid())) return true;
		return System.currentTimeMillis()
				- user.getLastModTime() > AppVariables.cacheLiveTime;
	}
	
	private User user;
	
	private Account account;
	
	private GainData gainData;
	
	// /personal-loan/detail 返回json
	private JSONObject product;
	
	private ApkInfo apkInfo;
	
	private List<MainAD> adDatas;
	
	private List<ShopADData> shopADDatas;//首页顶部图片
	
	private List<ShopADData> shopBottomADDatas; //首页底部图片
	
	private HashMap<Integer, Bitmap> redBgs = new HashMap<Integer, Bitmap>();
	
	private UPushMessage msg;
	
	private HashMap<String, String> redConditions = new HashMap<String, String>();
	
	private MainAD ad; //主页广告绝对地址（图片src直接调用）
	
	private Bitmap captcha;
	
	private String captchaKey;
	
	//现金券背景bitmap缓存
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		user.setLastModTime(new Date().getTime());
		this.user = user;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ApkInfo getApkInfo() {
		return apkInfo;
	}

	public void setApkInfo(ApkInfo apkInfo) {
		this.apkInfo = apkInfo;
	}


	public JSONObject getProduct() {
		return product;
	}

	public void setProduct(JSONObject product) {
		this.product = product;
	}

	public HashMap<Integer, Bitmap> getRedBgs() {
		return redBgs;
	}

	public void setRedBgs(HashMap<Integer, Bitmap> redBgs) {
		this.redBgs = redBgs;
	}

	public UPushMessage getMsg() {
		return msg;
	}

	public void setMsg(UPushMessage msg) {
		this.msg = msg;
	}

	public HashMap<String, String> getRedConditions() {
		return redConditions;
	}

	public void setRedConditions(HashMap<String, String> redConditions) {
		this.redConditions = redConditions;
	}


	public List<MainAD> getAdDatas() {
		return adDatas;
	}

	public void setAdDatas(List<MainAD> adDatas) {
		this.adDatas = adDatas;
	}

	public MainAD getAd() {
		return ad;
	}

	public void setAd(MainAD ad) {
		this.ad = ad;
	}

	public List<ShopADData> getShopADDatas() {
		return shopADDatas;
	}

	public void setShopADDatas(List<ShopADData> shopADDatas) {
		this.shopADDatas = shopADDatas;
	}

	public List<ShopADData> getShopBottomADDatas() {
		return shopBottomADDatas;
	}

	public void setShopBottomADDatas(List<ShopADData> shopBottomADDatas) {
		this.shopBottomADDatas = shopBottomADDatas;
	}

	public GainData getGainData() {
		return gainData;
	}

	public void setGainData(GainData gainData) {
		this.gainData = gainData;
	}

	public Bitmap getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Bitmap captcha) {
		this.captcha = captcha;
	}

	public String getCaptchaKey() {
		return captchaKey;
	}

	public void setCaptchaKey(String captchaKey) {
		this.captchaKey = captchaKey;
	}


}
