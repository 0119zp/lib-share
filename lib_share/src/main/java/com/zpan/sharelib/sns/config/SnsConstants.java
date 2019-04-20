package com.zpan.sharelib.sns.config;

/**
 * @author zpan
 * @date 2019/3/20
 */
public class SnsConstants {

    /**
     * 分享授权等返回状态码
     */
    public static final int SUCCESS_CODE = 200;
    public static final int FAILURE_CODE = 101;
    public static final int CANCEL_CODE = 102;

    public static final int WECHAT_SHARE_CANCEL_CODE = 0x1001;
    public static final int WECHAT_SHARE_FAILED_CODE = 0x1002;
    public static final int WECHAT_NOT_INSTALL_CODE = 0x1003;
    public static final int WECHAT_CIRCLE_NOT_SUPPORT_CODE = 0x1004;

    /**
     * QQ互联--key 包括QQ, Qzone
     */
    public static String SNS_QQ_APP_ID = "101045825";

    /**
     * 微信--key
     */
    public static String SNS_WEIXIN_APP_ID = "wxad2aff8b2919a3c2";

    /**
     * 微博--key
     */
    public static String SNS_SINA_WEIBO_APP_KEY = "1575990036";
    public static String SNS_SINA_REDIRECT_URL = "http://sns.whalecloud.com";
    public static String SNS_SINA_WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
        + "follow_app_official_microblog,"
        + "invitation_write";
}
