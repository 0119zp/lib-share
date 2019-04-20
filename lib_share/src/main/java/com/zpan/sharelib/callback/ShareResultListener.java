package com.zpan.sharelib.callback;

/**
 * 分享完成
 *
 * @author zpan
 * @date 2019/3/21
 */
public interface ShareResultListener {
    /**
     * 分享完成
     *
     * @param statusCode 状态码
     * @param msg 分享描述
     */
    void onComplete(int statusCode, String msg);
}
