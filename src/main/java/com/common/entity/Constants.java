package com.common.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Constants {

    public final static Map<Integer, String> MC_MSG_KEY_PREFIX_MAP  = new HashMap() {
        {
            put(0, "remindKey_");//系统通知（系统消息）
            put(1, "essayKey_");//日报
            put(2, "essayKey_");//会所
            put(3, "essayKey_");//新人学堂
            put(4, "newsKey_");//新鲜事
            put(5, "remindKey_");//评论（系统消息）
        }
    };
//essay type
    public static final int JOURNAL_ESSAY_ID = 1;
    public static final int CLUB_ESSAY_ID = 2;
    public static final int NEWCOMER_ESSAY_ID = 3;
    public static final int NEWS_ESSAY_ID= 4;
//auth id
    public static final String USER_AUTH_ID = "ROLE_USER";
    public static final String MANAGER_AUTH_ID = "ROLE_MANAGER";
    public static final String IDENTIFICATION_AUTH_ID = "ROLE_IDENTIFICATION";
    public static final String ADMIN_AUTH_ID = "ROLE_ADMIN";

    public static final Integer INDENTIFICATION_FANS_NUM = 30;
    public static final Integer INDENTIFICATION_RECOMMEND_ESSAY_NUM = 3;
    public static final Integer INDENTIFICATION_RECOMMEND_NUM = 50;

    public static final String NOTICE_EXIST = "noticeExist";
    public static final String NOTICE_STATUS = "noticeStatus";

    public static final String MC_NEWS_MSG_KEY_PREFIX = "newsKey_";
    public static final String MC_ESSAY_MSG_KEY_PREFIX = "essayKey_";
    public static final String MC_REMIND_MSG_KEY_PREFIX = "remindKey_";

    //public static final int RECOMMEND_NUM_FROM = 2;
    public static final int HOT_COMMENT_NUM_FROM = 2;

    public static final  List<Integer> ARTICLE_ESSAY_TYPES = Arrays.asList(1, 2, 3);
    public static final int RECOMMEND_NUM_FROM = 2;
    public static final int IS_PUBLISHED = 1;
    public static final int MSG_IS_NOT_READ = 0;
    public static final int SPECIFIED_DAY_NUM= -2;
    public static final String ORDER_BY_RECOMMEND_NUM = "recommendNum";
    public static final String ORDER_BY_COMMENT_NUM = "commentNum";

    public static final String QQ = "1920656780";

	public static final String WX = "kf-yeyoutuan";
	
	public static final String SERVICETELEPHONE = "0755-86570409"; //客服电话
	
	public static final String defaultCity = "深圳";
	
    public static int MAX_DAO_QUERY_PAGE_SIZE = 1000; // 最大分页查询数量
    
    public static final String FORUM_U_TOKEN_KEY = "FORUM_U_TOKEN_KEY#";

    public static final String REG_MOBILE = "^(1[4|3|6|7|8]\\d{9}|15[012356789]\\d{8})$"; // 手机号码正则

    public static final String USERNAME_MOBILE = "^[0-9A-Za-z]{5,16}$";  //用户名正则：必须由字母或数字构成，长度在5~16位

    public static final String PASSWORD_MOBILE = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";  //密码正则：必须同时由字母跟数字组合，长度在8~16位

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


    public static final Integer REMIND_MSG_EXPIRE_SECOND = 60*60; // 消息提醒过期时间

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

    public static final String DEFAULT_SIGNATURE = "这个人是兄贵，什么也没有留下：（";

    public static final String DEFAULT_USER_PIC = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527660236355&di=f641fcde8c51587601ff57345338dcb0&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f343596de3e1a8012193a36fefb4.jpg%402o.jpg";

    public static final int TEAM_POINTS = -20; //用户开团最低经验
    
    public static final int TEAM_TIME = 6;	//团队订单有效时长

    public static final String COMPANY_TEAM_TIME  = "HOUR";	//单位 HOUR  MINUTE
    
    public static final int OFF_LINE_BEFOR_TIME = 6;	//商品下架前几个小时不能购买
    public static final String COMPANY_OFF_LINE_BEFOR_TIME  = "HOUR";	//单位

    public static final int INVITE_COIN_REWARD= 10;

    public static final long REDUCE_PRICE = 2000;	//代金券抵用面额最低不能低于20块
}
