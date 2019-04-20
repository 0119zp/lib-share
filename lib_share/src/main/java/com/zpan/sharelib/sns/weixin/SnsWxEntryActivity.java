package com.zpan.sharelib.sns.weixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.handsome.sharelib.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsConstants;

/**
 * @author zpan
 * @date 2019/3/20
 */
public class SnsWxEntryActivity extends Activity implements IWXAPIEventHandler {

    protected IWXAPI api;
    public static SnsShareListener mSnsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(SnsConstants.SNS_WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * request from wechat
     */
    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * response from wechat
     */
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (mSnsListener != null) {
                    mSnsListener.onComplete(SnsConstants.SUCCESS_CODE, "");
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (mSnsListener != null) {
                    mSnsListener.onComplete(SnsConstants.WECHAT_SHARE_CANCEL_CODE, getString(R.string.share_canceled));
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (mSnsListener != null) {
                    mSnsListener.onComplete(SnsConstants.WECHAT_SHARE_FAILED_CODE, getString(R.string.share_failure));
                }
                break;
            default:
                break;
        }
        finish();
    }
}