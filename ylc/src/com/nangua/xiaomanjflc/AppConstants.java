package com.nangua.xiaomanjflc;

/**
 * @description:
 * @author: Liu wei
 * @mail: i@liuwei.co
 * @date: 2014-3-12
 */
public class AppConstants {
	
	public final static int IPS_VERSION = 4; // 当前IPS插件版本 对应1.1.4
	
	public final static String IPS_PACKAGE_NAME = "com.ips.p2p3"; // IPS插件包名
	
	public final static String USER_DB_NAME = "xiaomanjf_user.db"; // 数据库名字

	public final static String PATH_UPDATE_APK = "/yilicai/Update/";// 软件更新默认保存目录

	public static final int TIME = 120* 1000;// 手势密码出现持续时间  60秒

	public static final float BANNER_SCALE = 2.0f; // 长宽比
	
	public static final float BANNER_SCALE_BOTTOM = 2.0f; // 底部轮播长宽比
	
	public static final long LAST_IMAGE_CACHE_TIME = 5 * 60 * 1000; // 最近一张图片的判定有效时间

	/**
	 * 返回成功
	 */
	public static final int SUCCESS = 1;

	/**
	 * 返回失败
	 */
	public static final int FAILED = 2;
	
	// 线上地址
//	public final static String HOST = "http://www.xiaomanjf.com/mapp"; //mapp项目端口
//	public final static String HOST_IMAGE = "http://www.xiaomanjf.com/docroot/upload/images";
//	public final static String SPECIALHOST = "http://www.xiaomanjf.com"; //app项目 端口
	
	// 真实测试地址
//	public final static String HOST = "http://nangua.webok.net:9070/mapp"; //mapp项目端口
//	public final static String HOST_IMAGE = "http://nangua.webok.net:9070/docroot/upload/images";
//	public final static String SPECIALHOST = "http://nangua.webok.net:9070"; //app项目 端口

	// 内侧地址
//	public final static String HOST = "http://nangua.webok.net:9080/mapp"; //mapp项目端口
//	public final static String HOST_IMAGE = "http://nangua.webok.net:9080/docroot/upload/images";
//	public final static String SPECIALHOST = "http://nangua.webok.net:9080"; //app项目 端口
	
	//本地外网
//	public final static String HOST = "http://nangua.webok.net:9973/p2p-mapp";
//	public final static String HOST_IMAGE = "http://nangua.webok.net:9973/p2p-mapp/docroot/upload/images";
//	public final static String SPECIALHOST = "http://nangua.webok.net:9973";
	
	//本地外网IP
//	public final static String HOST = "http://192.168.199.73:9973/p2p-mapp";
//	public final static String HOST_IMAGE = "http://192.168.199.73:9973/p2p-mapp/docroot/upload/images";
//	public final static String SPECIALHOST = "http://192.168.199.73:9973";
	
//	public final static String HOST = "http://192.168.199.10:9080/mapp";
//	public final static String HOST_IMAGE = "http://192.168.199.10:9080/p2p-mapp/docroot/upload/images";
//	public final static String SPECIALHOST = "http://192.168.199.10:9080";

	//刘庆安
//	public final static String HOST = "http://192.168.199.43:9065/mapp";
//	public final static String HOST_IMAGE = "http://192.168.199.43:9065/mapp/docroot/upload/images";
//	public final static String SPECIALHOST = "http://192.168.199.43:9065";
	
	//集成测试
//	public final static String HOST = "http://nangua.webok.net:80/mapp";
//	public final static String HOST_IMAGE = "http://nangua.webok.net:80/p2p-mapp/docroot/upload/images";
//	public final static String SPECIALHOST = "http://nangua.webok.net:80";
	
	public final static String HOST = "http://192.168.199.34:80/mapp";
	public final static String HOST_IMAGE = "http://192.168.199.34:80/p2p-mapp/docroot/upload/images";
	public final static String SPECIALHOST = "http://192.168.199.34:80";

	public final static String HTTP = "http://";

	/**
	 * 登录
	 */
	public static final String SIGNIN = HOST + "/login/noOauth";

	/**
	 * 注册账号
	 */
	public static final String SIGNUP = HOST + "/register/noOauth";
	
	/**
	 * 邀请码获取邀请人信息
	 */
	public static final String INVITED_USER = HOST + "/register/invitedUser";
	
	
	

	/**
	 * 发送手机验证码
	 */
	public static final String GETCODE = HOST + "/reg/sendcode";
	
	/**
	 * 补充图片验证码
	 * 发送手机验证码
	 */
	public static final String GETCODE_V2 = HOST + "/reg/sendcode/v2";

	/**
	 * 获取验证码
	 */
	public static final String CAPTCHA = HOST + "/etc/captcha";

	/**
	 * 验证是否登录
	 */
	public static final String ISSIGNIN = HOST + "/islogin";

	/**
	 * 找回登陆密码时发送验证码
	 */
	public static final String SENDCODE = HOST + "/findback/sendcode";
	
	
	/**
	 * 补充图片验证码
	 * 找回登陆密码时发送验证码V2
	 */
	public static final String SENDCODE_V2 = HOST + "/findback/sendcode/v2";

	/**
	 * 找回登陆密码时验证手机验证码
	 */
	public static final String VERIFY_CODE = HOST + "/findback/find";

	/**
	 * 找回登陆密码
	 */
	public static final String GET_LOSE = HOST + "/findback/reset";

	/**
	 * 产品列表
	 */
	public static final String PRODUCTS = HOST + "/product/all";
	
	/**
	 * 产品推荐列表
	 */
	public static final String PRODUCTS_RECOMMENDED = HOST + "/product/recommended";

	/**
	 * 产品详情
	 */
	public static final String DETAIL_PRODUCT = HOST
			+ "/product/personal-loan/detail/";

	/**
	 * 直投宝列表
	 */
//	public static final String DIRECT = HOST + "/product/direct";

	/**
	 * 购买产品
	 */
	public static final String BUY = HOST + "/product/";

	/**
	 * 公告列表
	 */
	public static final String ANNOUNCE = HOST + "/article/announce";

	/**
	 * 获取账户页信息
	 */
	public static final String GAIN = HOST + "/my";

	/**
	 * 获取邀请信息
	 */
	public static final String INVITE = HOST + "/my/invitation";

	/**
	 * 用户基本信息
	 */
	public static final String BASICINFO = HOST + "/my/basic";


	/**
	 * 修改密码
	 */
	public static final String CHANGEPWD = HOST + "/my/password";

	/**
	 * 投资记录回款中
	 */
	public static final String INVEST_ORDER = HOST + "/my/order";

	/**
	 * 投资记录待确认
	 */
	public static final String INVEST_PENDING = HOST + "/my/order/pending";

	/**
	 * 投资记录已结清
	 */
	public static final String INVEST_CLOSED = HOST + "/my/order/closed";

	/**
	 * 投资记录流标
	 */
	public static final String INVEST_ABORTED = HOST + "/my/order/aborted";

	/**
	 * 交易记录
	 */
	public static final String TRANSACTION = HOST + "/my/bill";

	/**
	 * 现金券
	 */
	public static final String RED = HOST + "/my/cash";

	/**
	 * 新现金券
	 */
	public static final String NEWCASH = HOST + "/product/enablecash";
	
	/**
	 * 用户是否有借款
	 */
	public static final String ACCOUNT_ISLOAN = HOST + "/account/isloan";
	
	/**
	 * ips web登录
	 */
	public static final String IPS_LOGIN = HOST + "/account/huanxun";
	/**
	 * 充值
	 */
	public static final String CHARGE = HOST + "/account/recharge";

	/**
	 * 提现
	 */
	public static final String CASH = HOST + "/account/withdraw";

	/**
	 * 提现手续费
	 */
	public static final String FEE = HOST + "/my/withdraw-bill";

	/**
	 * 实名认证
	 */
	public static final String IDCARD = HOST + "/account/register";

	/**
	 * 银行卡绑定
	 */
	public static final String BANKCARD = HOST + "/account/bindcard";

	/**
	 * 检测更新
	 */
	public static final String UPDATE = HOST + "/api/version/get";

	/**
	 * 服务协议 直接
	 */
//	public static final String SERVICE_PROTOCOL = SPECIALHOST
//			+ "/contract/show/new";
	
	public static final String SERVICE_PROTOCOL = SPECIALHOST
			+ "/tender/tendercontract.html";
	
	
	/**
	 * 服务协议 债权
	 */
	public static final String SERVICE_PROTOCOL2 = SPECIALHOST
			+ "/tender/transfercontract.html";

	/**
	 * 借款协议
	 */
	public static final String INVEST_PROTOCOL = HOST + "/protocol/contract/";

	/**
	 * 获取首页轮播图数据
	 */
	public static final String GET_SLIDE_IMAGE = HOST + "/appadv";
	
	/**
	 * 获取首页轮播图数据
	 */
	public static final String GET_BOTTOM_IMAGE = HOST + "/appadv/bottom";
	

	/**
	 * 关于我们
	 */
	public static final String ABOUT_US = SPECIALHOST + "/page/app/about/";
	
	/**
	 * 保障权益
	 */
	public static final String GUARD = SPECIALHOST + "/page/app/about/";
	

	/**
	 * 帮助中心
	 */
	public static final String FAQ = SPECIALHOST + "/page/app/faq/";


	/**
	 * 消息中心
	 */
	public static final String MESSAGE_CENTER = HOST + "/my/center";

	/**
	 * 消息中心 已读
	 */
	public static final String MESSAGE_CENTER_ALREAD = HOST
			+ "/my/center/alread";
	
	/**
	 * 消息中心 全部已读
	 */
	public static final String MESSAGE_CENTER_READ_ALL = HOST
			+ "/my/center/readall";

	/**
	 * 取得银行列表
	 */
	public static final String GET_BANKCODE = HOST + "/account/bankCode";
	
	
    /**
     * 支付货币类型  钱 0 金币1
     */
    public enum NotifyMsgType {

    	//支付宝  微信 金币 现金（线下）
    	TXT(0,"普通文本"), UPDATE(1, "更新"), PRODUCT(2, "商品跳转"), URL(3, "链接跳转");

        private String type;
        
        private int code;

        NotifyMsgType(int code, String type) {
            this.type = type;
            this.code = code;
        }

        public String getType() {
            return type;
        }
        
        public int getCode() {
        	return code;
        }

    }
    
    /**
     * 过场广告类型
     */
    public enum MainADType {

    	//支付宝  微信 金币 现金（线下）
    	GOODS(2,"商品"), LINK(1, "链接");

        private String type;
        
        private int code;

        MainADType(int code, String type) {
            this.type = type;
            this.code = code;
        }

        public String getType() {
            return type;
        }
        
        public int getCode() {
        	return code;
        }

    }
    
    /**
     * 过场广告类型
     */
    public enum SortType {

    	//支付宝  微信 金币 现金（线下）
    	DEFAULT(0,"默认"), PROFIT(2,"商品"), COST(3, "金额"), LIMIT_DATE(4, "期限");

        private String type;
        
        private int code;

        SortType(int code, String type) {
            this.type = type;
            this.code = code;
        }

        public String getType() {
            return type;
        }
        
        public int getCode() {
        	return code;
        }
        
        public static SortType getSortTypeByCode(int code) {
        	for (SortType sortType : SortType.values()) {
				if (sortType.getCode() == code) {
					return sortType;
				}
			}
        	return null;
        }

    }

}
