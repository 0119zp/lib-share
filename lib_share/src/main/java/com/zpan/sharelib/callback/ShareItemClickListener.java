package com.zpan.sharelib.callback;

import android.view.View;
import android.widget.PopupWindow;

/**
 * 条目点击事件
 *
 * @author zpan
 */
public interface ShareItemClickListener {

    /**
     * 条目点击事件
     *
     * @param popupWindow 视图
     * @param view 点击view
     * @param platform 平台
     */
    void onItemClick(PopupWindow popupWindow, View view, int platform);
}
