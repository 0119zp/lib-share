package com.zpan.sharelib.sns.config;

import android.graphics.Bitmap;

/**
 * @author zpan
 * @date 2019/3/20
 */
public class SnsMedia implements Cloneable {
    /**
     * 缩略图
     */
    public Bitmap thumbBitmap;
    /**
     * 纬度
     */
    public String lat;
    /**
     * 经度
     */
    public String lon;
    public String title;
    public String content;
    public String webpageUrl;
    /**
     * 本地图片地址
     */
    public String localImageUrl;
    /**
     * 网络图片地址
     */
    public String netImageUrl;
    public byte[] bitmapData;
    /**
     * 分享类型, 包括纯文本, 图片, 网页, Music, Video等
     */
    public SnsMediaType type = SnsMediaType.TYPE_IMAGE;

    public SnsMedia(SnsMediaType type) {
        this("", "", "", "", "", type);
    }

    public SnsMedia(String title, String content, String webpageUrl, String localImageUrl, String netImageUrl, SnsMediaType type) {
        this.title = title;
        this.content = content;
        this.webpageUrl = webpageUrl;
        this.type = type;
        this.localImageUrl = localImageUrl;
        this.netImageUrl = netImageUrl;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Bitmap getThumbBitmap() {
        return thumbBitmap;
    }

    public byte[] getBitmapData() {
        return bitmapData;
    }

    public String getLocalImageUrl() {
        return localImageUrl;
    }

    public String getNetImageUrl() {
        return netImageUrl;
    }

    @Override
    public String toString() {
        return "SnsMedia [title="
            + title
            + ", content="
            + content
            + ", webpageUrl="
            + webpageUrl
            + ", netImageUrl="
            + netImageUrl
            + ", localImageUrl="
            + localImageUrl
            + ", type="
            + type
            + ", thumbBitmap="
            + thumbBitmap
            + ", lat="
            + lat
            + ", lon="
            + lon
            + "]";
    }
}
