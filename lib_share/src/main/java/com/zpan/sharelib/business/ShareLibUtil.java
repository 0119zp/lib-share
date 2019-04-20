package com.zpan.sharelib.business;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;
import com.handsome.sharelib.R;
import com.zpan.sharelib.icon.ShareIcon;
import com.zpan.sharelib.sns.config.SnsPlatform;

/**
 * 短信、复制
 *
 * @author zpan
 */
public class ShareLibUtil {

    /**
     * 分享至短信
     */
    protected void sendSms(Context context, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    /**
     * 复制链接
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    protected void cutAndPasteBoard(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        ClipboardManager copy = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setPrimaryClip(ClipData.newPlainText("url", content));

        Resources resources = context.getResources();
        String shareSuccess = resources.getString(R.string.share_copy_copyed);
        Toast.makeText(context, shareSuccess, Toast.LENGTH_SHORT).show();
    }

    public static SnsPlatform getSnsPlatform(int platform) {
        switch (platform) {
            case ShareIcon.SHARE_WEIXIN:
                return SnsPlatform.WEIXIN;
            case ShareIcon.SHARE_WEIXIN_CIRCLE:
                return SnsPlatform.WEIXIN_CIRCLE;
            case ShareIcon.SHARE_QZONE:
                return SnsPlatform.QZONE;
            case ShareIcon.SHARE_QQ:
                return SnsPlatform.QQ;
            case ShareIcon.SHARE_WEIBO:
                return SnsPlatform.WEIBO;
            default:
                return SnsPlatform.WEIXIN;
        }
    }
}
