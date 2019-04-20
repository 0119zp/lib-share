package com.zpan.sharelib.sns.handler;

import android.content.Intent;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.zpan.sharelib.sns.callback.SnsOauthListener;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsMedia;

/**
 * @author zpan
 * @date 2019/3/20
 */
public interface SnsHandler {

    /**
     * 授权
     *
     * @param listener 授权监听
     */
    void oauth(SnsOauthListener listener);

    /**
     * 直接分享(无编辑界面)
     *
     * @param media 多媒体
     * @param listener 分享监听
     */
    void shareDirect(SnsMedia media, SnsShareListener listener);

    /**
     * 是否授权
     *
     * @return 授权结果
     */
    boolean isOauthed();

    /**
     * 回调
     *
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data 数据
     */
    void authorizeCallBack(int requestCode, int resultCode, Intent data);

    /**
     * 获取微博 shareHandler 对象
     *
     * @return 微博 shareHandler 对象
     */
    WbShareHandler getWeiboShareHandler();

    /**
     * 退出
     */
    void logout();
}
