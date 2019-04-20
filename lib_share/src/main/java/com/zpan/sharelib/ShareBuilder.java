package com.zpan.sharelib;

import android.content.Context;
import com.zpan.sharelib.bean.ShareLibBean;
import com.zpan.sharelib.business.ShareLibBusiness;
import com.zpan.sharelib.business.SharePopupWindow;
import com.zpan.sharelib.business.ShareSourceTypeEnum;
import com.zpan.sharelib.callback.ShareCancleListener;
import com.zpan.sharelib.callback.ShareCopyListener;
import com.zpan.sharelib.callback.ShareItemClickListener;
import com.zpan.sharelib.callback.ShareResultListener;
import com.zpan.sharelib.icon.ShareViewIcons;
import com.zpan.sharelib.sns.config.SnsMediaType;

/**
 * @author zpan
 */
public class ShareBuilder {

    public ShareViewIcons shareViewIcons;
    public ShareLibBean shareLibBean;
    public ShareItemClickListener shareItemClickListener;
    public ShareResultListener shareResultListener;
    public ShareSourceTypeEnum shareSourceType;
    public SnsMediaType snsMediaType;
    public int snsPlatform;
    public ShareCopyListener shareCopyListener;
    public ShareCancleListener shareCancleListener;
    public ShareLibBusiness shareLibBusiness;

    public ShareBuilder() {
    }

    public ShareLibManager buildShareLibManager(Context context) {
        return new ShareLibManager(context, this);
    }

    public ShareLibBusiness buildShareLibBusiness(Context context) {
        return new ShareLibBusiness(context, this);
    }

    public SharePopupWindow buildSharePopupwindow(Context context) {
        return new SharePopupWindow(context, this);
    }

    public ShareBuilder setShareViewIcons(ShareViewIcons icons) {
        this.shareViewIcons = icons;
        return this;
    }

    public ShareBuilder setShareLibBean(ShareLibBean shareLibBean) {
        this.shareLibBean = shareLibBean;
        return this;
    }

    public ShareBuilder setShareItemClickListener(ShareItemClickListener shareItemClickListener) {
        this.shareItemClickListener = shareItemClickListener;
        return this;
    }

    public ShareBuilder setShareResultListener(ShareResultListener shareResultListener) {
        this.shareResultListener = shareResultListener;
        return this;
    }

    public ShareBuilder setShareSourceType(ShareSourceTypeEnum shareSourceType) {
        this.shareSourceType = shareSourceType;
        return this;
    }

    public ShareBuilder setSnsMediaType(SnsMediaType snsMediaType) {
        this.snsMediaType = snsMediaType;
        return this;
    }

    public ShareBuilder setSnsPlatform(int snsPlatform) {
        this.snsPlatform = snsPlatform;
        return this;
    }

    public ShareBuilder setShareCopyListener(ShareCopyListener shareCopyListener) {
        this.shareCopyListener = shareCopyListener;
        return this;
    }

    public ShareBuilder setShareCancleListener(ShareCancleListener shareCancleListener) {
        this.shareCancleListener = shareCancleListener;
        return this;
    }

    public ShareBuilder setShareLibBusiness(ShareLibBusiness shareLibBusiness) {
        this.shareLibBusiness = shareLibBusiness;
        return this;
    }
}
