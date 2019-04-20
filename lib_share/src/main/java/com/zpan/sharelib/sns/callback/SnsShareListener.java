package com.zpan.sharelib.sns.callback;

/**
 * @author zpan
 * @date 2019/3/25
 */
public interface SnsShareListener {

    /**
     * 开始分享
     */
    void onStart();

    /**
     * 分享完成
     *
     * @param statusCode 为
     * SnsConstants.SUCCESS_CODE表示成功,
     * SnsConstants.FAILURE_CODE表示失败,
     * SnsConstants.CANCEL_CODE表示取消
     * @param msg 分享信息
     */
    void onComplete(int statusCode, String msg);
}
