package com.zpan.sharelib.bean;

/**
 * 业务详情数据，设置模板用
 *
 * @author Zpan
 */
public class ShareLibDetailBean {

    /**
     * 客户端名称
     */
    private String appName = "";
    /**
     * 国家名称
     */
    private String countryName = "";
    /**
     * 城市名称
     */
    private String cityName = "";
    /**
     * H5 链接
     */
    private String shareUrl = "";
    /**
     * 分享内容
     */
    private String content = "";

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
