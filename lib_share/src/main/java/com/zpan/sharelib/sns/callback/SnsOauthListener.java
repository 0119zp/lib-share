package com.zpan.sharelib.sns.callback;

/**
 * @author zpan
 * @date 2019/3/25
 */
public interface SnsOauthListener {

    /**
     * 授权开始
     */
    void onStart();

    /**
     * 授权完成
     *
     * @param statusCode 为Constants.SUCCESS_CODE表示成功, 否者为错误,
     * @param msg 为错误信息
     */
    void onComplete(int statusCode, String msg);

    /**
     * 授权取消
     */
    void onCancel();

    /**
     * 授权错误
     *
     * @param msg 错误信息
     */
    void onError(String msg);
}
