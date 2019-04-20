package com.zpan.sharelib.business;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.handsome.sharelib.R;
import com.zpan.sharelib.ShareBuilder;
import com.zpan.sharelib.bean.ShareLibBean;
import com.zpan.sharelib.callback.ShareCancleListener;
import com.zpan.sharelib.callback.ShareCopyListener;
import com.zpan.sharelib.callback.ShareItemClickListener;
import com.zpan.sharelib.callback.ShareResultListener;
import com.zpan.sharelib.icon.ShareIcon;
import com.zpan.sharelib.icon.ShareViewIcons;
import com.zpan.sharelib.sns.SnsShareUtil;
import com.zpan.sharelib.sns.callback.SnsShareListener;
import com.zpan.sharelib.sns.config.SnsConstants;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsMediaType;
import com.zpan.sharelib.sns.config.SnsPlatform;

/**
 * @author Zpan
 * @date 2019/3/21
 */
public class ShareLibBusiness {

    protected Context mContext;
    private ShareViewIcons mShareViewIcons;
    private ShareLibBean mShareLibBean;
    private ShareItemClickListener mShareItemClickListener;
    private ShareResultListener mShareResultListener;
    private ShareSourceTypeEnum mShareSourceType;
    private SnsMediaType mSnsMediaType;
    private int mSnsPlatform;
    private ShareCopyListener mShareCopyListener;
    private ShareCancleListener mShareCancleListener;

    private Bitmap mLoadBitMap;
    private static final int SNS_MEDIA_TYPE_WEBPAGE = 0;
    private static final int SNS_MEDIA_TYPE_IMAGE = 1;

    Handler mShareHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            int snsPlatform = (int) msg.obj;
            SnsMediaType mediaType = msg.what == SNS_MEDIA_TYPE_IMAGE ? SnsMediaType.TYPE_IMAGE : SnsMediaType.TYPE_WEBPAGE;
            startShare(snsPlatform, mediaType);
        }
    };

    public static ShareBuilder getShareBuilder() {
        return new ShareBuilder();
    }

    public ShareLibBusiness(Context context, ShareBuilder builder) {
        this.mContext = context;
        this.mShareViewIcons = builder.shareViewIcons;
        this.mShareLibBean = builder.shareLibBean;
        this.mShareItemClickListener = builder.shareItemClickListener;
        this.mShareResultListener = builder.shareResultListener;
        this.mShareSourceType = builder.shareSourceType;
        this.mSnsMediaType = builder.snsMediaType;
        this.mSnsPlatform = builder.snsPlatform;
        this.mShareCopyListener = builder.shareCopyListener;
        this.mShareCancleListener = builder.shareCancleListener;

        initShare();
    }

    private void initShare() {
        if (null == mContext) {
            throw new IllegalArgumentException(this.getClass().getName() + "`s Context is null");
        }

        if (mShareViewIcons == null) {
            shareStart(mSnsPlatform, mSnsMediaType);
            return;
        }

        SharePopupWindow.getShareBuilder()
            .setShareViewIcons(mShareViewIcons)
            .setShareItemClickListener(mShareItemClickListener)
            .setShareLibBusiness(this)
            .setShareCopyListener(mShareCopyListener)
            .setSnsMediaType(mSnsMediaType)
            .setShareCancleListener(mShareCancleListener)
            .buildSharePopupwindow(mContext);
    }

    public void shareStart(int platform, SnsMediaType mediaType) {
        mShareLibBean = ShareLibBusinessUtil.buildShareBean(mContext, platform, mShareLibBean, mShareSourceType);
        if (platform == ShareIcon.SHARE_SMS || platform == ShareIcon.SHARE_COPY) {
            localOwnShare(platform);
            return;
        }
        //  第三方分享平台，先下载缩略图
        loadShareImage(platform, mediaType);
    }

    /**
     * 本地功能短信、复制链接
     */
    private void localOwnShare(int platform) {
        if (null == mShareLibBean) {
            return;
        }
        String shareContent = mShareLibBean.getShareContent();
        ShareLibUtil shareLibUtil = new ShareLibUtil();
        if (platform == ShareIcon.SHARE_SMS) {
            shareLibUtil.sendSms(mContext, shareContent);
            shareFinish();
            return;
        }
        shareLibUtil.cutAndPasteBoard(mContext, shareContent);
        shareFinish();
    }

    private void loadShareImage(final int snsPlatform, final SnsMediaType mediaType) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String shareIconUrl = mShareLibBean.getShareIconUrl();
                    int shareIconResId = mShareLibBean.getShareIconResId();
                    Bitmap shareIcon = mShareLibBean.getShareIcon();
                    if (!TextUtils.isEmpty(shareIconUrl)) {
                        Glide.with(mContext).asBitmap().load(shareIconUrl).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                mLoadBitMap = bitmap;
                            }
                        });
                    } else if (shareIconResId > 0) {
                        mLoadBitMap = BitmapFactory.decodeResource(mContext.getResources(), shareIconResId);
                    } else if (null != shareIcon) {
                        mLoadBitMap = shareIcon;
                    }
                    Message msg = new Message();
                    msg.obj = snsPlatform;
                    msg.what = mediaType == SnsMediaType.TYPE_IMAGE ? SNS_MEDIA_TYPE_IMAGE : SNS_MEDIA_TYPE_WEBPAGE;
                    mShareHandler.sendMessage(msg);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startShare(final int platform, SnsMediaType mediaType) {
        SnsPlatform snsPlatform = ShareLibUtil.getSnsPlatform(platform);
        // 组装Medis数据进行分享
        final SnsMedia snsMedia = buildSnsMedia(mediaType);
        SnsShareUtil.shareDirect((Activity) mContext, snsPlatform, snsMedia, new SnsShareListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int statusCode, String msg) {
                handleShareResult(statusCode, msg);
            }
        });
    }

    private SnsMedia buildSnsMedia(SnsMediaType mediaType) {
        SnsMedia media;
        if (mediaType == SnsMediaType.TYPE_IMAGE) {
            media = new SnsMedia(SnsMediaType.TYPE_IMAGE);
        } else {
            media = new SnsMedia(SnsMediaType.TYPE_WEBPAGE);
        }
        media.title = mShareLibBean.getShareTitle();
        media.content = mShareLibBean.getShareContent();
        media.thumbBitmap = mLoadBitMap;
        media.webpageUrl = mShareLibBean.getShareWebUrl();
        return media;
    }

    private void handleShareResult(int statusCode, String msg) {
        Resources resources = mContext.getResources();
        switch (statusCode) {
            case SnsConstants.SUCCESS_CODE:
                String shareSuccess = resources.getString(R.string.share_success);
                Toast.makeText(mContext, shareSuccess, Toast.LENGTH_SHORT).show();
                break;
            case SnsConstants.FAILURE_CODE:
                String shareFail = resources.getString(R.string.share_failure);
                Toast.makeText(mContext, shareFail, Toast.LENGTH_SHORT).show();
                break;
            case SnsConstants.CANCEL_CODE:
                String shareCancel = resources.getString(R.string.share_canceled);
                Toast.makeText(mContext, shareCancel, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        shareFinish();
        if (null != mShareResultListener) {
            mShareResultListener.onComplete(statusCode, msg);
        }
    }

    public void shareFinish() {
        if (null != mLoadBitMap) {
            mLoadBitMap.recycle();
        }
    }
}
