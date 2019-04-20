package com.zpan.sharelib.sns.handler;

import android.app.Activity;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.zpan.sharelib.sns.callback.SnsOauthListener;
import com.zpan.sharelib.sns.config.SnsAccount;
import com.zpan.sharelib.sns.config.SnsConstants;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.util.SharedPreferencesHelper;

/**
 * @author zpan
 * @date 2019/3/20
 */
public abstract class BaseSnsHandler implements SnsHandler {

    public final int SUCCESS_CODE = SnsConstants.SUCCESS_CODE;
    public final int FAILURE_CODE = SnsConstants.FAILURE_CODE;
    public final int CANCEL_CODE = SnsConstants.CANCEL_CODE;
    public SnsOauthListener mOauthListener;

    public Activity mActivity;
    public SnsPlatform mPlatform;

    public BaseSnsHandler(Activity activity, SnsPlatform platform) {
        this.mActivity = activity;
        this.mPlatform = platform;
    }

    public void saveAccessToken(SnsAccount snsAccount) {
        SharedPreferencesHelper.getInstance(mActivity.getApplicationContext()).putObject(getSaveKey(), snsAccount);
    }

    public SnsAccount getAccessToken() {
        return SharedPreferencesHelper.getInstance(mActivity.getApplicationContext()).getObject(getSaveKey(), SnsAccount.class);
    }

    public void removeAccessToken() {
        SharedPreferencesHelper.getInstance(mActivity.getApplicationContext()).removeByKey(getSaveKey());
    }

    private String getSaveKey() {
        String key = mPlatform.name();
        if (mPlatform == SnsPlatform.QQ || mPlatform == SnsPlatform.QZONE) {
            key = SnsPlatform.QQ.name();
        }
        return key;
    }

    @Override
    public WbShareHandler getWeiboShareHandler() {
        return null;
    }

    @Override
    public void logout() {
        removeAccessToken();
    }
}
