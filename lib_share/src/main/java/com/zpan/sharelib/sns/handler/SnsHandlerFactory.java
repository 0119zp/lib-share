package com.zpan.sharelib.sns.handler;

import android.app.Activity;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.qq.QqHandler;
import com.zpan.sharelib.sns.qq.QzoneHandler;
import com.zpan.sharelib.sns.weibo.WeiboHandler;
import com.zpan.sharelib.sns.weixin.WeixinCircleHandler;
import com.zpan.sharelib.sns.weixin.WeixinHandler;

/**
 * @author zpan
 * @date 2019/3/20
 */
public class SnsHandlerFactory {

    public static SnsHandler getSnsHandler(Activity activity, SnsPlatform platform) {
        SnsHandler handler = null;
        switch (platform) {
            case WEIBO:
                handler = new WeiboHandler(activity, platform);
                break;
            case QQ:
                handler = new QqHandler(activity, platform);
                break;
            case QZONE:
                handler = new QzoneHandler(activity, platform);
                break;
            case WEIXIN:
                handler = new WeixinHandler(activity, platform);
                break;
            case WEIXIN_CIRCLE:
                handler = new WeixinCircleHandler(activity, platform);
                break;
            default:
                break;
        }
        return handler;
    }
}
