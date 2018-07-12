package com.common.entity;

/**
 *
 */
public class SentenceConstants {

    public static final String DEFAULT_SIGNATURE = null;

	public static final String GOLD_ADD_REASON_ESSAY = "文章奖励";
	
	public static final String GOLD_REDUCE_REASON_CASH = "提现";

    public static final String COIN_ADD_REASON_INVITE= "邀请奖励";

    public static final String COIN_ADD_REASON_SYSTEM= "系统奖励";

    public static final String COIN_REDUCE_REASON_CONSUME = "个性化消费"; //

    public static final String DEFAULT_USER_PIC = null;

    public static final String DEFAULT_USER_BACKDROP= null;

    public static final String defaultCity = "深圳";
	
    public static int MAX_DAO_QUERY_PAGE_SIZE = 1000; // 最大分页查询数量
    
    public static final String FORUM_U_TOKEN_KEY = "FORUM_U_TOKEN_KEY#";

    public static final String REG_MOBILE = "^(1[4|3|6|7|8]\\d{9}|15[012356789]\\d{8})$"; // 手机号码正则

    public static final String USERNAME_MOBILE = "^[0-9A-Za-z]{5,16}$";  //用户名正则：必须由字母或数字构成，长度在5~16位

    public static final String PASSWORD_MOBILE = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";  //密码正则：必须由字母跟数字组合，长度在8~16位

    public static final String INVITECODE_MOBILE = "^[0-9]{6}$";  //正则：必须由数字构成，长度在6位

    public static final String DEFAULT_PWD = "123456"; // 默认密码

    public static final String POSTFIX_IMG = "jpg|jpeg|png"; // 图片后缀

    public static final String POSTFIX_VIDEO = "mp4|mov|rm|rmvb|avi"; // 视频后缀

    public static final String POSTFIX_AUDIO = "mp3|3ga|m4a|wav";// 音频后缀

    public static final String EXCEPTION_INFO = "服务访问出现问题，请稍后重试";
    
    public static final String ID_GENERATE_NAME = "orderNo";	//商店商品订单号生成模块名,与db对应
    
    public static final String ID_PLATFORM_NAME = "productOrderNo";	//平台产品订单号生成模块名,与db对应
    
    public static final String ID_VOUCHER_NAME = "voucherOrderNo";	//平台抵用券订单号生成模块名,与db对应
    
    public static final String ID_SHOP_WITHDRAW_NAME = "shopWithdrawOrderNo";	//商户提现订单id
    
    public static final Integer IMAGE_CODE_COUNT_EXPIRE_SECOND = 86400; // ip访问条数过期时间
    public static final Integer DEFAULT_DAY_MAX_IMAGE_CODE = 50; // 默认ip访问每天最大访问量
    public static final Integer IMAGE_CODE_EXPIRE_SECOND = 300; // 图片验证码过期时间
    public static final String IMAGE_CODE_DETAIL_KEY_STR = "IMAGE_CODE_DETAIL_KEY#";// memcache登录发送内容记录key
    public static final String IMAGE_CODE_Count_KEY_STR = "IMAGE_CODE_Count_Key#"; // memcache登录发送数量记录key
    
    
    public static final Integer SMS_SEND_EXPIRE_SECOND = 300; // 短信过期时间

    public static final Integer SMS_COUNT_EXPIRE_SECOND = 86400; // 短信条数过期时间
    
    public static final Integer DEFAULT_DAY_MAX_SMS_COUNT = 50; // 默认每天最大发送短信条数
    
    public static final String SMS_VALIDATE_CODE_LIMIT_TIME = "5";	//验证码有效时间(分钟)
    
    public static final String SMS_LOGIN_PASSWD_KEY_STR = "didi_loginPwdSmsKey#";// memcache登录发送内容记录key
    
    public static final String SMS_LOGIN_PASSWD_COUNT_KEY_STR = "didi_loginPwdSmsCountKey#"; // memcache登录发送数量记录key
    
    public static final int TEAM_ORDER_COUNT = 3;
    
    public static final int REWARD_SIGN_VIP = 10; //单人奖励vip 10天
    
    public static final int REWARD_TEAM_VIP = 5; //团购奖励vip 5天
     
    public static final int PUNISH_VIP = -10;//处罚vip
    
    public static final int PUNISH_EXPERIENCE  = -100;//处罚经验
    
    public static final int INVITE_REWARD_VIP = 10; //邀请好友奖励vip
    
    public static final String CONSUM_ORDER_TITAL = "订单核销通知";
    
    public static final String REWARDTITAL = "奖励通知";
    
    public static final String PUNISHTITAL = "处罚通知";
    
    public static final String ACTIVITYTITAL = "活动通知";
    
    public static final String MEMBERTITAL = "会员通知";


    public static final int TEAM_POINTS = -20; //用户开团最低经验
    
    public static final int TEAM_TIME = 6;	//团队订单有效时长

    public static final String COMPANY_TEAM_TIME  = "HOUR";	//单位 HOUR  MINUTE
    
    public static final int OFF_LINE_BEFOR_TIME = 6;	//商品下架前几个小时不能购买
    public static final String COMPANY_OFF_LINE_BEFOR_TIME  = "HOUR";	//单位

    public static final int INVITE_COIN_REWARD= 10;

    public static final long REDUCE_PRICE = 2000;	//代金券抵用面额最低不能低于20块
}
