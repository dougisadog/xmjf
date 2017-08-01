package com.nangua.xiaomanjflc.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.nangua.xiaomanjflc.AppConstants;
import com.nangua.xiaomanjflc.AppVariables;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;
import com.nangua.xiaomanjflc.utils.ClipboardUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.analytics.social.UMPlatformData.UMedia;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.bean.jsonbean.User;
import com.nangua.xiaomanjflc.cache.CacheBean;
import com.nangua.xiaomanjflc.error.DebugPrinter;
import com.nangua.xiaomanjflc.support.UIHelper;

public class NormalInviteActivity extends KJActivity {

	@BindView(id = R.id.count, click = true)
	private TextView mCount;
	@BindView(id = R.id.txtInvite)
	private TextView mTxtInvite;
	@BindView(id = R.id.scan, click = true)
	private RelativeLayout mScan;

	private KJHttp http;
	private HttpParams params;

	private String invitationCount;
	private String refCode;
	private String shareUrl;
	
	private String inviteContent;
	private String inviteTitleUrl;
	
	private String shareTitle;
	private String shareImg;
	
	private WebView webView;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_normal_invite);

		Intent intent = getIntent();
		if ("account".equals(intent.getStringExtra("activity"))) {
			UIHelper.setTitleView(this, "我的账户", "邀请好友");
		} else {
			UIHelper.setTitleView(this, "返回", "邀请好友");
		}
		UIHelper.setBtnRight(this, R.drawable.header_more, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showShare();
			}
		});
		
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setUseWideViewPort(true);

		webView.setWebViewClient(new MyClient());

	}
	
	final class MyClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

//			int width = ApplicationUtil.getApkInfo(NormalInviteActivity.this).width;
//			String baseUrl = shareUrl;
//			webView.loadUrl("javascript:doPicCreate('" + baseUrl + "','" + refCode + "','" + width + "')");
		}
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
//		case R.id.invite:
//			showShare();
//			break;
		case R.id.scan:
			showActivity(NormalInviteActivity.this, InviteListActivity.class);
			break;
		}
	}

	@Override
	public void initData() {
		super.initData();
		http = new KJHttp();
		params = new HttpParams();
		getInfo();
	}
	
	

	@Override
	public void initWidget() {
		//添加下划线  
		mTxtInvite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  
		super.initWidget();
	}

	private void getInfo() {
		params.put("sid", AppVariables.sid);
		http.post(AppConstants.INVITE, params, new HttpCallBack(
				NormalInviteActivity.this) {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject ret = new JSONObject(t);
					//分享地址
					shareUrl = ret.getString("recommendationUrl");
					//邀请人数
					invitationCount = ret.getString("invitationCount");
					
					inviteContent = ret.getString("invite_content");
					if (StringUtils.isEmpty(inviteContent)) {
						inviteContent = "亲，我在小满金服投资啦！年化收益7%-15%。加入立得百元现金券，猛戳链接领钱吧！";
					}
					
					shareTitle = ret.getString("share_title");
					if (StringUtils.isEmpty(shareTitle)) {
						shareTitle = "点击邀请注册";
					}
					
					shareImg = ret.getString("share_img");
					if (StringUtils.isEmpty(shareImg)) {
						shareImg = "http://www.xiaomanjf.com/static/images/img/wechat_share.png";
					}
					
					inviteTitleUrl = ret.getString("invite_title_url");
					if (StringUtils.isEmpty(inviteTitleUrl)) {
						inviteTitleUrl = AppConstants.SPECIALHOST;
					}
					//邀请码
					refCode = ret.getString("refCode");
					mCount.setText(invitationCount);
//					mCashReturned.setText(FormatUtils.priceFormat(cashReturned));
					
					int width = ApplicationUtil.getApkInfo(NormalInviteActivity.this).width;
					String url = AppConstants.SPECIALHOST + "/useryaoqingweixin.html?refcode= " + refCode 
							+ "&baseUrl=" + shareUrl + "&width=" + width;
//					webView.loadUrl("file:///android_asset/UserYaoqingWeixin.html");
					webView.loadUrl(url);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showShare() {
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// text是分享文本，所有平台都需要这个字段
		oks.setTitle(shareTitle);
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitleUrl(shareUrl);
		
		oks.setImageUrl(shareImg);
//		oks.setImagePath(imagePath);
		oks.setText(inviteContent);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(shareUrl);

		oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform platform, int action, Throwable arg2) {
			}
			
			@Override
			public void onComplete(Platform platform, int action,
					HashMap<String, Object> arg2) {
				UMedia uMedia = null;
				if (platform.getName().equals("SinaWeibo")) {
					uMedia = UMedia.SINA_WEIBO;
				}
				else if (platform.getName().equals("Wechat")) {
					uMedia = UMedia.WEIXIN_FRIENDS;
				}
				else if (platform.getName().equals("WechatMoments")) {
					uMedia = UMedia.WEIXIN_CIRCLE;
				}
				else if (platform.getName().equals("QQ")) {
					uMedia = UMedia.TENCENT_QQ;
				}
				User user = CacheBean.getInstance().getUser();
				if (null != uMedia && null != user) {
					UMPlatformData platformData = new UMPlatformData(uMedia, user.getPhone());
					platformData.setName(user.getName());
					MobclickAgent.onSocialEvent(NormalInviteActivity.this, platformData);
					DebugPrinter.d("platform:" + platform.getName() + " action:" + action + "\n");
				}
			}
			
			@Override
			public void onCancel(Platform platform, int action) {
			}
		});
		Bitmap linkLogo = BitmapFactory.decodeResource(getResources(),
				R.drawable.ssdk_oks_customer_link);
		String labelLink = "复制链接";
		OnClickListener listenerCopyLink = new OnClickListener() {
			public void onClick(View v) {
				ClipboardUtils.copy(shareUrl);
				Toast.makeText(NormalInviteActivity.this, "已复制链接",
						Toast.LENGTH_LONG).show();
			}
		};
		oks.setCustomerLogo(linkLogo, labelLink, listenerCopyLink);

		Bitmap refcodeLogo = BitmapFactory.decodeResource(getResources(),
				R.drawable.ssdk_oks_customer_refcode);
		String labelRefcode = "复制邀请码";
		OnClickListener listenerCopyRefcode = new OnClickListener() {
			public void onClick(View v) {
				ClipboardUtils.copy(refCode);
				Toast.makeText(NormalInviteActivity.this, "已复制邀请码",
						Toast.LENGTH_LONG).show();
			}
		};
		oks.setCustomerLogo(refcodeLogo, labelRefcode, listenerCopyRefcode);
		oks.show(this);
	}

}
