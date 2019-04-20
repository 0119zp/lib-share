package com.zpan.sharelib.sns.weixin;

import android.app.Activity;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsPlatform;

/**
 * @author zpan
 * @date 2019/3/20
 */
public class WeixinCircleHandler extends BaseWeixinHandler {

    public WeixinCircleHandler(Activity activity, SnsPlatform platform) {
        super(activity, platform);
    }

    @Override
    public void shareDirect(SnsMedia media, SnsShareListener listener) {
        super.shareDirect(media, listener, true);
    }
}
