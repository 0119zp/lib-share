package com.zpan.sharelib.bean;

import android.graphics.Bitmap;

/**
 * 分享主体结构
 *
 * @author Zpan
 */
public class ShareLibBean {

    /**
     * 分享icon图片资源id
     */
    private int shareIconResId;
    /**
     * 分享icon网络图片
     */
    private String shareIconUrl;
    /**
     * 分享标题
     */
    private String shareTitle;
    /**
     * 分享内容，当有详细业务信息时，分享内容根据ShareDetailBean在ShareLibBussiness中组装
     */
    private String shareContent;
    /**
     * 分享web链接
     */
    private String shareWebUrl;
    /**
     * 分享图片
     */
    private Bitmap shareIcon;

    /**
     * 分享内容关键字段信息，业务信息分享时使用
     */
    private ShareLibDetailBean shareLibDetailBean;
    /**
     * 分享图片本地路径
     */
    private String shareIconPath;
    /**
     * 分享图片源信息
     */
    private String shareIconSrc;

    public ShareLibBean() {
    }

    public String getShareIconUrl() {
        return shareIconUrl;
    }

    public void setShareIconUrl(String shareIconUrl) {
        this.shareIconUrl = shareIconUrl;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public Bitmap getShareIcon() {
        return shareIcon;
    }

    public void setShareIcon(Bitmap shareIcon) {
        this.shareIcon = shareIcon;
    }

    public String getShareWebUrl() {
        return shareWebUrl;
    }

    public void setShareWebUrl(String shareWebUrl) {
        this.shareWebUrl = shareWebUrl;
    }

    public int getShareIconResId() {
        return shareIconResId;
    }

    public void setShareIconResId(int shareIconResId) {
        this.shareIconResId = shareIconResId;
    }

    public ShareLibDetailBean getShareLibDetailBean() {
        return shareLibDetailBean;
    }

    public void setShareLibDetailBean(ShareLibDetailBean shareLibDetailBean) {
        this.shareLibDetailBean = shareLibDetailBean;
    }

    public String getShareIconPath() {
        return shareIconPath;
    }

    public void setShareIconPath(String shareIconPath) {
        this.shareIconPath = shareIconPath;
    }

    public String getShareIconSrc() {
        return shareIconSrc;
    }

    public void setShareIconSrc(String shareIconSrc) {
        this.shareIconSrc = shareIconSrc;
    }
}
