package com.zpan.sharelib.sns.config;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zpan
 * @date 2019/3/20
 */
public class SnsAccount implements Parcelable {

    private String accessToken = "";
    private long expriedIn;
    private String openId = "";
    private String refreshToken = "";

    public SnsAccount() {
    }

    public SnsAccount(String accessToken, long expriedIn) {
        this(accessToken, expriedIn, "");
    }

    public SnsAccount(String accessToken, long expriedIn, String openId) {
        this(accessToken, expriedIn, "", "");
    }

    public SnsAccount(String accessToken, long expriedIn, String openId, String refreshToken) {
        this.accessToken = accessToken;
        this.expriedIn = expriedIn;
        this.openId = openId;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String mAccessToken) {
        this.accessToken = mAccessToken;
    }

    public long getExpriedIn() {
        return expriedIn;
    }

    public void setExpriedIn(long mExpriedIn) {
        this.expriedIn = mExpriedIn;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String mOpenId) {
        this.openId = mOpenId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String mRefreshToken) {
        this.refreshToken = mRefreshToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeLong(expriedIn);
        dest.writeString(openId);
        dest.writeString(refreshToken);
    }

    protected SnsAccount(Parcel in) {
        accessToken = in.readString();
        expriedIn = in.readLong();
        openId = in.readString();
        refreshToken = in.readString();
    }

    public static final Creator<SnsAccount> CREATOR = new Creator<SnsAccount>() {
        @Override
        public SnsAccount createFromParcel(Parcel in) {
            return new SnsAccount(in);
        }

        @Override
        public SnsAccount[] newArray(int size) {
            return new SnsAccount[size];
        }
    };
}
