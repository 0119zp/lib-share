package com.zpan.sharelib.sns.qq;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsMediaType;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.util.FileUtil;
import com.zpan.sharelib.sns.util.ImageUtil;
import java.io.File;

/**
 * QQ分享工具类
 * 1.1.1版本新增 网络,本地，byte[]形式的分享方式(qq分享不支持byte[]方式)
 * 优先级-> byte[]>bitmap>本地>网络
 * http://wiki.open.qq.com/index.php?title=Android_API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E&=45038#1.13_.E5.88.86.E4.BA.AB.E6.B6.88.E6.81
 * .AF.E5.88.B0QQ.EF.BC.88.E6.97.A0.E9.9C.80QQ.E7.99.BB.E5.BD.95.EF.BC.89
 *
 * @author zpan
 */
public class QqHandler extends BaseQqHandler {
    public QqHandler(Activity activity, SnsPlatform platform) {
        super(activity, platform);
    }

    @Override
    public void shareDirect(SnsMedia media, final SnsShareListener listener) {
        super.shareDirect(media, listener);

        mTencent.shareToQQ(mActivity, buildShareParams(media), new IUiListener() {

            @Override
            public void onCancel() {
                listener.onComplete(CANCEL_CODE, "");
            }

            @Override
            public void onComplete(Object value) {
                listener.onComplete(SUCCESS_CODE, "");
            }

            @Override
            public void onError(UiError e) {
                listener.onComplete(e.errorCode, e.errorMessage);
            }
        });
    }

    private Bundle buildShareParams(SnsMedia snsMedia) {
        SnsMedia media;
        try {
            media = (SnsMedia) snsMedia.clone();
        } catch (CloneNotSupportedException e) {
            media = snsMedia;
            e.printStackTrace();
        }

        final Bundle params = new Bundle();
        int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;

        switch (media.type) {
            case TYPE_WEBPAGE:
                //图文模式必须传入3个参数,SHARE_TO_QQ_KEY_TYPE,SHARE_TO_QQ_TITLE,SHARE_TO_QQ_TARGET_URL
                //SHARE_TO_QQ_SUMMARY为可选项
                shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
                params.putString(QQShare.SHARE_TO_QQ_TITLE, media.title);
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, media.webpageUrl);
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, media.content);
                break;
            case TYPE_IMAGE:
                //纯图片SHARE_TO_QQ_KEY_TYPE,SHARE_TO_QQ_IMAGE_LOCAL_URL必传
                shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE;
                break;
            case TYPE_VEDIO:
                break;
            default:
                break;
        }

        if (media.thumbBitmap == null) {
            int resId = mActivity.getApplicationInfo().icon;
            if (resId > 0) {
                media.thumbBitmap = BitmapFactory.decodeResource(mActivity.getResources(), resId);
            } else {
                Log.e("jameson", "no ic_launcher resource in drawable folder, while shareToQzone must have a thumbBitmap");
            }
        }

        if (media.thumbBitmap != null) {
            Bitmap thumbBitmap = media.thumbBitmap;
            if (media.type != SnsMediaType.TYPE_IMAGE) {
                //优先本地图片地址
                if (!media.getLocalImageUrl().isEmpty()) {
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, media.getLocalImageUrl());
                } else if (!media.getNetImageUrl().isEmpty()) {
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, media.getNetImageUrl());
                } else {
                    thumbBitmap = ImageUtil.getInSampleBitmap(media.thumbBitmap, 200, 200, true, false);
                    String dir = FileUtil.getTempPath(mActivity);
                    File thumbFile = FileUtil.saveBitmap(mActivity.getApplicationContext(), thumbBitmap, dir, "tmp", CompressFormat.PNG);
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumbFile.getAbsolutePath());
                }
            } else if (media.type == SnsMediaType.TYPE_IMAGE) {
                //纯图片形式只能传本地图片url
                if (!media.getLocalImageUrl().isEmpty()) {
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, media.getLocalImageUrl());
                } else {
                    String dir = FileUtil.getTempPath(mActivity);
                    File thumbFile = FileUtil.saveBitmap(mActivity.getApplicationContext(), thumbBitmap, dir, "tmp", CompressFormat.PNG);
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, thumbFile.getAbsolutePath());
                }
            }
        }
        //分享的类型
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
        return params;
    }
}
