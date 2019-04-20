package com.zpan.sharelib.sns.weibo;

import android.app.Activity;
import android.os.Bundle;
import com.handsome.sharelib.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.zpan.sharelib.sns.callback.SnsOauthListener;
import com.zpan.sharelib.sns.config.SnsAccount;
import com.zpan.sharelib.sns.config.SnsConstants;

/**
 * 授权结果回调，实现WbAuthListener接⼜
 *
 * @author zpan
 * @date 2019/3/25
 */
public class SelfWbAuthListener implements WbAuthListener {

    private SnsOauthListener mOauthListener;
    private Activity mActivity;
    private BaseWeiboHandler mWeiboHandler;

    public SelfWbAuthListener(BaseWeiboHandler handler, Activity activity, SnsOauthListener listener) {
        this.mWeiboHandler = handler;
        this.mActivity = activity;
        this.mOauthListener = listener;
    }

    /**
     * 授权成功回调,返回Oauth2AccessToken对象，封装了登录相关信 息
     *
     * @param oauth2AccessToken 返回Oauth2AccessToken对象
     */
    @Override
    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {

        if (oauth2AccessToken.isSessionValid()) {
            // 保存 Token 到 SharedPreferences
            SnsAccount snsAccount =
                new SnsAccount(oauth2AccessToken.getToken(), oauth2AccessToken.getExpiresTime(), oauth2AccessToken.getUid());

            mWeiboHandler.saveAccessToken(snsAccount);
            mOauthListener.onComplete(SnsConstants.SUCCESS_CODE, String.valueOf(oauth2AccessToken.getBundle()));
        } else {
            String message = mActivity.getString(R.string.share_auth_fail);
            int statusCode = SnsConstants.FAILURE_CODE;
            try {
                Bundle bundle = oauth2AccessToken.getBundle();
                if (bundle != null) {
                    String code = bundle.getString("code");
                    if (code != null) {
                        statusCode = Integer.parseInt(code);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mOauthListener.onComplete(statusCode, message);
        }
    }

    /**
     * 授权取消回调
     */
    @Override
    public void cancel() {
        mOauthListener.onCancel();
    }

    /**
     * 授权失败回调(更多相关失败信息，请参考⽂档结尾错误码)
     *
     * @param wbConnectErrorMessage 失败信息
     */
    @Override
    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
        mOauthListener.onError(wbConnectErrorMessage.getErrorCode());
    }
}
