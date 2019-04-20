package com.zpan.sharelib.sns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.handsome.sharelib.BuildConfig;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.zpan.sharelib.sns.callback.SnsOauthListener;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsConstants;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.handler.SnsHandler;
import com.zpan.sharelib.sns.handler.SnsHandlerFactory;

/**
 * 社会化登录分享Util, 支持自定义配置信息
 * 注意事项:
 * 1. 在Application onCreate()方法调用SnsShareUtil.initKeyConfig()
 * 2. Activity中onActivityResult增加代码<br/>
 *
 * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 * super.onActivityResult(requestCode, resultCode, data);
 * SnsShareUtil.authorizeCallBack(requestCode,resultCode,data);
 * }
 *
 * @author zpan
 */
public class SnsShareUtil {

    private static SnsHandler mHandler;

    /**
     * 授权分享
     */
    public static void oauth(Activity activity, SnsPlatform platform, SnsOauthListener listener) {
        if (platform == SnsPlatform.WEIXIN || platform == SnsPlatform.WEIXIN_CIRCLE) {
            Toast.makeText(activity, "微信/朋友圈不需要授权", Toast.LENGTH_SHORT).show();
            return;
        }
        mHandler = SnsHandlerFactory.getSnsHandler(activity, platform);
        mHandler.oauth(listener);
    }

    /**
     * 直接分享(无编辑界面)
     */
    public static void shareDirect(Activity activity, SnsPlatform platform, SnsMedia media, SnsShareListener listener) {
        SnsConstants.SNS_WEIXIN_APP_ID = BuildConfig.WEIXIN_KEY;
        SnsHandler handler = SnsHandlerFactory.getSnsHandler(activity, platform);
        mHandler = handler;
        handler.shareDirect(media, listener);
    }

    /**
     * 微博：
     * 要接收到授权的相关数据，必须在当前页⾯Activity或者Fragment的
     * onActivityResult⽅法中添加SSOHandler的调
     */
    public static void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        if (mHandler != null) {
            mHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 处理分享回调：重写onNewIntent⽅法
     */
    public static void doResultIntent(Intent intent, final Context context) {
        mHandler.getWeiboShareHandler().doResultIntent(intent, new WbShareCallback() {
            @Override
            public void onWbShareSuccess() {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWbShareCancel() {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWbShareFail() {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 判断平台是否授权
     */
    public static boolean isOauthed(Activity activity, SnsPlatform platform) {
        return mHandler.isOauthed();
    }

    /**
     * 解绑
     */
    public static void logout(Activity activity, SnsPlatform... platforms) {
        for (SnsPlatform platform : platforms) {
            SnsHandler handler = SnsHandlerFactory.getSnsHandler(activity, platform);
            handler.logout();
        }
    }
}
