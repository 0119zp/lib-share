package com.zpan.sharelib;

import android.content.Context;
import android.content.Intent;
import com.zpan.sharelib.bean.ShareLibBean;
import com.zpan.sharelib.business.ShareLibBusiness;
import com.zpan.sharelib.business.ShareSourceTypeEnum;
import com.zpan.sharelib.callback.ShareCancleListener;
import com.zpan.sharelib.callback.ShareCopyListener;
import com.zpan.sharelib.callback.ShareItemClickListener;
import com.zpan.sharelib.callback.ShareResultListener;
import com.zpan.sharelib.icon.ShareViewIcons;
import com.zpan.sharelib.sns.SnsShareUtil;
import com.zpan.sharelib.sns.config.SnsMediaType;

/**
 * @author zpan
 */
public class ShareLibManager {

    /**
     * 上下文对象
     */
    private Context mContext;
    /**
     * 分享渠道
     */
    private ShareViewIcons mShareViewIcons;
    /**
     * 分享内容实体
     */
    private ShareLibBean mShareLibBean;
    /**
     * 点击具体分享渠道Button回调
     */
    private ShareItemClickListener mShareItemClickListener;
    /**
     * 分享完成结果回调（成功或失败）
     */
    private ShareResultListener mShareResultListener;
    /**
     * 分享业务类型，针对不同业务类型和渠道重新组装分享内容
     */
    private ShareSourceTypeEnum mShareSourceType;
    /**
     * 内容分享方式
     */
    private SnsMediaType mSnsMediaType;
    /**
     * 指定的分享平台，非空时直接分享，不会弹出分享面板
     */
    private int mSnsPlatform;
    /**
     * 点击转发文案回调
     */
    private ShareCopyListener mShareCopyListener;
    /**
     * 点击取消按钮回调
     */
    private ShareCancleListener mShareCancleListener;

    public ShareLibManager(Context context, ShareBuilder builder) {
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
        if (mShareSourceType == null) {
            mShareSourceType = ShareSourceTypeEnum.TYPE_OTHER;
        }
        ShareLibBusiness.getShareBuilder()
            .setShareItemClickListener(mShareItemClickListener)
            .setShareCancleListener(mShareCancleListener)
            .setShareCopyListener(mShareCopyListener)
            .setShareLibBean(mShareLibBean)
            .setShareResultListener(mShareResultListener)
            .setShareSourceType(mShareSourceType)
            .setShareViewIcons(mShareViewIcons)
            .setSnsMediaType(mSnsMediaType)
            .setSnsPlatform(mSnsPlatform)
            .buildShareLibBusiness(mContext);
    }

    public static ShareBuilder getShareBuilder() {
        return new ShareBuilder();
    }

    public static void shareAuthorizeCallBack(int requestCode, int resultCode, Intent data) {
        SnsShareUtil.authorizeCallBack(requestCode, resultCode, data);
    }

    public static void shareDoResultIntent(Intent intent, Context context) {
        SnsShareUtil.doResultIntent(intent, context);
    }
}
