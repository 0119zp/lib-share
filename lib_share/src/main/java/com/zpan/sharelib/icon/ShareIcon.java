package com.zpan.sharelib.icon;

import android.content.Context;
import android.graphics.Color;

/**
 * icon 属性配置
 *
 * @author zpan
 */
public class ShareIcon {

    public static final float DEFAULT_ICON_SIZE = 50;

    public static final int COLOR_WECHAT = 0xff73C48F;
    public static final int COLOR_WECHAT_TIMELINE = 0xffF96854;
    public static final int COLOR_SINA = 0xff6B98E9;
    public static final int COLOR_QQ = 0xffF5A623;
    public static final int COLOR_SMS = 0xffF96854;
    public static final int COLOR_COPY_URL = 0xff73C48F;

    public static final int SHARE_WEIXIN = 0x101;
    public static final int SHARE_WEIXIN_CIRCLE = 0x102;
    public static final int SHARE_QQ = 0x103;
    public static final int SHARE_QZONE = 0x104;
    public static final int SHARE_WEIBO = 0x105;
    public static final int SHARE_SMS = 0x106;
    public static final int SHARE_COPY = 0x107;

    /**
     * iconfont 图标
     */
    public String iconfont;
    /**
     * icon 本地资源id
     */
    public int iconResourceId;
    /**
     * iconfont 代表的平台
     */
    public int platform;
    /**
     * iconfont 下面的平台名
     */
    public String iconText;
    /**
     * iconfont 的颜色
     */
    public int iconColor;
    /**
     * iconfont 的大小
     */
    public float iconSize;
    /**
     * iconfont 下面文字的颜色
     */
    public int iconTextColor;
    /**
     * iconfont 下面文字的大小
     */
    public float iconTextSize;
    /**
     * icon + text 整体背景资源Id
     */
    public int backgroundResId;
    /**
     * iconfont 背景资源Id
     */
    public int iconBackgroudResId;
    /**
     * 是否使用本地 icon
     */
    public boolean useResourceIcon = false;

    public ShareIcon(Context context, Builder builder) {
        this.iconfont = builder.iconfont;
        this.iconResourceId = builder.iconResourceId;
        this.platform = builder.platform;
        this.iconText = builder.iconText;
        this.iconColor = builder.iconColor;
        this.iconSize = builder.iconSize;
        if (this.iconSize == 0) {
            // icon 默认大小
            this.iconSize = DEFAULT_ICON_SIZE;
        }
        this.iconTextColor = builder.iconTextColor;
        if (this.iconTextColor == 0) {
            // icon 下面text默认颜色
            this.iconTextColor = Color.parseColor("#666666");
        }
        this.iconTextSize = builder.iconTextSize;
        if (iconTextSize == 0) {
            // icon 下面text默认大小
            this.iconTextSize = sp2px(context, 12);
        }

        this.backgroundResId = builder.backgroundResId;
        this.iconBackgroudResId = builder.iconBackgroudResId;
        this.useResourceIcon = builder.useResourceIcon;
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static class Builder {

        private String iconfont;
        private int iconResourceId;
        private int platform;
        private String iconText;
        private int iconColor;
        private float iconSize;
        private int iconTextColor;
        private float iconTextSize;
        private int backgroundResId;
        private int iconBackgroudResId;
        private boolean useResourceIcon;

        public Builder() {
        }

        public Builder setIconfont(String iconfont) {
            this.iconfont = iconfont;
            return this;
        }

        public Builder setIconResourceId(int iconResourceId) {
            this.iconResourceId = iconResourceId;
            return this;
        }

        public Builder setPlatform(int platform) {
            this.platform = platform;
            return this;
        }

        public Builder setIconText(String iconText) {
            this.iconText = iconText;
            return this;
        }

        public Builder setIconColor(int iconColor) {
            this.iconColor = iconColor;
            return this;
        }

        public Builder setIconTextColor(int iconTextColor) {
            this.iconTextColor = iconTextColor;
            return this;
        }

        public Builder setBackgroundResId(int backgroundResId) {
            this.backgroundResId = backgroundResId;
            return this;
        }

        public Builder setIconTextSize(float iconTextSize) {
            this.iconTextSize = iconTextSize;
            return this;
        }

        public Builder setIconSize(float iconSize) {
            this.iconSize = iconSize;
            return this;
        }

        public Builder setIconBackgroudResId(int iconBackgroudResId) {
            this.iconBackgroudResId = iconBackgroudResId;
            return this;
        }

        public Builder setUseResourceIcon(boolean useResourceIcon) {
            this.useResourceIcon = useResourceIcon;
            return this;
        }

        public ShareIcon build(Context context) {
            return new ShareIcon(context, this);
        }
    }
}
