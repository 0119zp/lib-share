package com.zpan.sharelib.sns.weibo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.Toast;
import com.handsome.sharelib.R;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.zpan.sharelib.sns.callback.SnsOauthListener;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsAccount;
import com.zpan.sharelib.sns.config.SnsConstants;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.handler.BaseSnsHandler;

/**
 * @author zpan
 */
public abstract class BaseWeiboHandler extends BaseSnsHandler {

    private static final String WB_NOT_INSTALL = "8000";
    private static final String WB_NOT_OFFICIAL = "8001";
    private static final String WB_SIGN_NOT_SAME = "C8998";

    private SsoHandler mSsoHandler;
    public WbShareHandler wbShareHandler;

    public BaseWeiboHandler(Activity activity, SnsPlatform platform) {
        super(activity, platform);
        // ⾸先初始化WbSdk对象(在你的应⽤的Application或者调⽤Sdk功能代码前)
        AuthInfo authInfo = new AuthInfo(mActivity, SnsConstants.SNS_SINA_WEIBO_APP_KEY, SnsConstants.SNS_SINA_REDIRECT_URL,
            SnsConstants.SNS_SINA_WEIBO_SCOPE);
        WbSdk.install(activity, authInfo);
        // 初始化SsoHandler对象 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
        mSsoHandler = new SsoHandler(mActivity);

        wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();
    }

    @Override
    public void oauth(SnsOauthListener listener) {
        mOauthListener = listener;
        mOauthListener.onStart();

        mSsoHandler.authorizeClientSso(new SelfWbAuthListener(this, mActivity, listener));
    }

    @Override
    public void shareDirect(final SnsMedia media, final SnsShareListener listener) {
        listener.onStart();
        if (isOauthed()) {
            sendMessage(media);
        } else {
            oauth(new SnsOauthListener() {

                @Override
                public void onStart() {

                }

                @Override
                public void onError(String msg) {
                    if (TextUtils.isEmpty(msg)) {
                        listener.onComplete(FAILURE_CODE, msg);
                        return;
                    }
                    Resources resources = mActivity.getResources();
                    if (WB_NOT_INSTALL.equals(msg)) {
                        String wbNotInstall = resources.getString(R.string.sns_wb_not_install);
                        Toast.makeText(mActivity, wbNotInstall, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WB_NOT_OFFICIAL.equals(msg)) {
                        String wbNotOfficial = resources.getString(R.string.sns_wb_not_official);
                        Toast.makeText(mActivity, wbNotOfficial, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (WB_SIGN_NOT_SAME.equals(msg)) {
                        String wbSignNotSame = resources.getString(R.string.sns_wb_sign_notsame);
                        Toast.makeText(mActivity, wbSignNotSame, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.onComplete(FAILURE_CODE, msg);
                }

                @Override
                public void onComplete(int statusCode, String msg) {
                    sendMessage(media);
                }

                @Override
                public void onCancel() {
                    listener.onComplete(CANCEL_CODE, "");
                }
            });
        }
    }

    /**
     * 分享消息
     *
     * @param media 分享信息
     */
    public abstract void sendMessage(SnsMedia media);

    /**
     * 是否授权
     *
     * @return 授权状态
     */
    @Override
    public boolean isOauthed() {
        return getOauth2AccessToken().isSessionValid();
    }

    /**
     * 获取当前已保存过的 Token
     */
    private Oauth2AccessToken getOauth2AccessToken() {
        SnsAccount snsAccount = getAccessToken();
        Oauth2AccessToken accessToken = new Oauth2AccessToken();
        if (snsAccount != null) {
            accessToken.setToken(snsAccount.getAccessToken());
            accessToken.setExpiresTime(snsAccount.getExpriedIn());
            accessToken.setUid(snsAccount.getOpenId());
        }
        return accessToken;
    }

    /**
     * SSO 授权回调: 发起 SSO 登陆的 Activity 必须重写 onActivityResult
     *
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data 数据
     */
    @Override
    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public WbShareHandler getWeiboShareHandler() {
        return wbShareHandler;
    }
}
