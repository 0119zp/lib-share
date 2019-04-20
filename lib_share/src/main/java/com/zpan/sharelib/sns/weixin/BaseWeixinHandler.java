package com.zpan.sharelib.sns.weixin;

import android.app.Activity;
import android.content.Intent;
import com.handsome.sharelib.R;
import com.zpan.sharelib.sns.callback.SnsOauthListener;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsConstants;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsMediaType;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.handler.BaseSnsHandler;

/**
 * @author zpan
 * @date 2019/3/25
 */
public abstract class BaseWeixinHandler extends BaseSnsHandler {

    public WeChatUtils mWechatUtils;

    public BaseWeixinHandler(Activity activity, SnsPlatform platform) {
        super(activity, platform);
        mWechatUtils = new WeChatUtils(activity);
    }

    @Override
    public void oauth(SnsOauthListener listener) {
        // no need to fulfill
    }

    @Override
    public boolean isOauthed() {
        return true;
    }

    @Override
    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        // no need to fulfill
    }

    public void shareDirect(SnsMedia media, SnsShareListener listener, boolean isFriendCircle) {
        if (!mWechatUtils.isWXAppInstalled()) {
            listener.onComplete(SnsConstants.WECHAT_NOT_INSTALL_CODE, mActivity.getString(R.string.sns_weixin_not_install));
            return;
        }

        if (isFriendCircle && !mWechatUtils.isWXAppSupportMoment()) {
            listener.onComplete(SnsConstants.WECHAT_CIRCLE_NOT_SUPPORT_CODE, mActivity.getString(R.string.sns_weixin_circle_not_support));
            return;
        }

        listener.onStart();
        SnsWxEntryActivity.mSnsListener = listener;

        //图文形式
        if (media.type == SnsMediaType.TYPE_IMAGE) {

            //优先字节数组方式
            if (media.getBitmapData() != null) {
                mWechatUtils.shareImageFromByteArray(media.getBitmapData(), media.title, media.content, isFriendCircle);
            }

            //第2级缩略图
            else if (media.getThumbBitmap() != null) {
                mWechatUtils.shareImageFromBitmap(media.getThumbBitmap(), media.title, media.content, isFriendCircle);
            }

            //第3级 本地url
            else if (!media.getLocalImageUrl().isEmpty()) {
                //微信sdk暂时不支持
            }

            //优先级最低-->网络url
            else if (!media.getNetImageUrl().isEmpty()) {
                //微信sdk暂时不支持
            }
        }

        // webpage形式
        if (media.type == SnsMediaType.TYPE_WEBPAGE) {
            if (media.getBitmapData() != null) {
                mWechatUtils.shareWebPage(media.getBitmapData(), media.webpageUrl, media.title, media.content, isFriendCircle);
            } else {
                mWechatUtils.shareWebPage(media.getThumbBitmap(), media.webpageUrl, media.title, media.content, isFriendCircle);
            }
        }
        /*
         * 视频形式
         * */
        if (media.type == SnsMediaType.TYPE_VEDIO) {
            //1.1.1 暂时不做
        }
    }
}
