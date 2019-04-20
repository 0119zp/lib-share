package com.zpan.sharelib.sns.qq;

import android.app.Activity;
import android.content.Intent;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zpan.sharelib.sns.callback.SnsOauthListener;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsAccount;
import com.zpan.sharelib.sns.config.SnsConstants;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.handler.BaseSnsHandler;

/**
 * @author zpan
 * @date 2019/3/20
 */
public abstract class BaseQqHandler extends BaseSnsHandler {

    protected Tencent mTencent;
    private static final String SCOPE = "all";

    public BaseQqHandler(Activity activity, SnsPlatform platform) {
        super(activity, platform);
    }

    protected void init() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(SnsConstants.SNS_QQ_APP_ID, mActivity);
            SnsAccount account = getAccessToken();
            if (account != null) {
                mTencent.setAccessToken(account.getAccessToken(), account.getExpriedIn() + "");
                mTencent.setOpenId(account.getOpenId());
            }
        }
    }

    @Override
    public void oauth(SnsOauthListener listener) {
        init();
        mOauthListener = listener;
        mOauthListener.onStart();

        mTencent.logout(mActivity);
        mTencent.login(mActivity, SCOPE, new IUiListener() {
            @Override
            public void onComplete(final Object values) {
                SnsAccount snsAccount = new SnsAccount(mTencent.getAccessToken(), mTencent.getExpiresIn(), mTencent.getOpenId());
                saveAccessToken(snsAccount);
                mOauthListener.onComplete(SUCCESS_CODE, String.valueOf(values));
            }

            @Override
            public void onCancel() {
                mOauthListener.onCancel();
            }

            @Override
            public void onError(UiError e) {
                mOauthListener.onComplete(e.errorCode, e.errorMessage);
            }
        });
    }

    @Override
    public void shareDirect(SnsMedia media, SnsShareListener listener) {
        init();
        listener.onStart();
    }

    @Override
    public boolean isOauthed() {
        init();
        return mTencent.isSessionValid() && mTencent.getOpenId() != null;
    }

    @Override
    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }
}
