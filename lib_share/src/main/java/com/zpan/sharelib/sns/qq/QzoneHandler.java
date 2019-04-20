package com.zpan.sharelib.sns.qq;

import android.app.Activity;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsPlatform;
import com.zpan.sharelib.sns.util.FileUtil;
import java.io.File;
import java.util.ArrayList;

/**
 * 1. title, webpageUrl, thumbBitmap必选(如果不传默认使用R.drawbable.ic_launcher), 其他可选. 2. Qzone传中文路径不显示图片,
 * bug! 3. 分享的标题，最多200个字符。 4. 分享的摘要，最多600字符。
 * QQ空间分享工具类
 * 1.1.1版本新增 网络,本地，byte[]形式的分享方式(不支持byte[]模式,网络图片)
 * 优先级-> byte[]>bitmap>本地>网络
 * http://wiki.open.qq.com/index.php?title=Android_API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E&=45038#1.14_.E5.88.86.E4.BA.AB.E5.88.B0QQ.E7
 * .A9.BA.E9.97.B4.EF.BC.88.E6.97.A0.E9.9C.80QQ.E7.99.BB.E5.BD.95.EF.BC.89
 *
 * @author zpan
 */
public class QzoneHandler extends BaseQqHandler {

    public QzoneHandler(Activity activity, SnsPlatform platform) {
        super(activity, platform);
    }

    @Override
    public void shareDirect(SnsMedia media, final SnsShareListener listener) {
        super.shareDirect(media, listener);

        mTencent.shareToQzone(mActivity, buildShareParams(media), new IUiListener() {

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

        //限制title字数不超过190个字符
        if (TextUtils.isEmpty(media.title)) {
            media.title = "";
        } else if (media.title.length() > 190) {
            media.title = media.title.substring(0, 190);
        }
        //限制分享内容字数不超过590个字符
        if (TextUtils.isEmpty(media.content)) {
            media.content = "";
        } else if (media.content.length() > 590) {
            media.content = media.content.substring(0, 590) + "...";
        }

        final Bundle params = new Bundle();
        int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, media.title);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, media.webpageUrl);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, media.content);

        if (media.thumbBitmap == null) {
            int resId = mActivity.getApplicationInfo().icon;
            if (resId > 0) {
                media.thumbBitmap = BitmapFactory.decodeResource(mActivity.getResources(), resId);
            } else {
                Log.e("jameson", "no ic_launcher resource in drawable folder, while shareToQzone must have a thumbBitmap");
            }
        }
        ArrayList arrayList = new ArrayList();
        if (media.thumbBitmap != null) {
            String dir = FileUtil.getTempPath(mActivity);
            File thumbFile = FileUtil.saveBitmap(mActivity.getApplicationContext(), media.thumbBitmap, dir, "tmp", CompressFormat.PNG);

            arrayList.add(thumbFile.getAbsolutePath());
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arrayList);
        } else if (!media.getLocalImageUrl().isEmpty()) {
            arrayList.add(media.getLocalImageUrl());
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arrayList);
        }
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);

        return params;
    }
}
