package com.zpan.sharelib.icon;

import android.content.Context;
import com.handsome.sharelib.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置 icon 样式
 *
 * @author zpan
 * @date 2019/3/21
 */
public class ShareViewIcons {

    private Context mContext;
    /**
     * 分享条目icon 是否使用本地资源
     */
    private boolean mUseResourceIcon;
    private ArrayList<ShareIcon> shareIcons = new ArrayList<>();

    public ShareViewIcons(Context context) {
        this.mContext = context;
    }

    public void setPlatform(List<Integer> platforms) {
        if (platforms == null || platforms.size() == 0) {
            return;
        }
        for (Integer platform : platforms) {
            shareIcons.add(buildIcon(platform));
        }
    }

    public void useResourceIcon(boolean resourceIcon) {
        this.mUseResourceIcon = resourceIcon;
    }

    private ShareIcon buildIcon(int platform) {
        ShareIcon.Builder builder = new ShareIcon.Builder();
        builder.setPlatform(platform);
        builder.setUseResourceIcon(mUseResourceIcon);
        // TODO icon 本地资源待补充 zpan 2019/3/22
        switch (platform) {
            case ShareIcon.SHARE_WEIXIN:
                builder.setIconfont(mContext.getString(R.string.ic_weixin))
                    .setIconResourceId(R.drawable.icon_share_wechat)
                    .setIconText(mContext.getString(R.string.share_platform_wechat))
                    .setIconColor(ShareIcon.COLOR_WECHAT);
                break;
            case ShareIcon.SHARE_WEIXIN_CIRCLE:
                builder.setIconfont(mContext.getString(R.string.ic_weixin_circle))
                    .setIconResourceId(R.drawable.icon_share_wechat_circle)
                    .setIconText(mContext.getString(R.string.share_platform_wechat_circle))
                    .setIconColor(ShareIcon.COLOR_WECHAT_TIMELINE);
                break;
            case ShareIcon.SHARE_WEIBO:
                builder.setIconfont(mContext.getString(R.string.ic_weibo))
                    .setIconResourceId(R.drawable.icon_share_wechat)
                    .setIconText(mContext.getString(R.string.share_platform_weibo))
                    .setIconColor(ShareIcon.COLOR_SINA);
                break;
            case ShareIcon.SHARE_QQ:
                builder.setIconfont(mContext.getString(R.string.ic_qq))
                    .setIconResourceId(R.drawable.icon_share_wechat)
                    .setIconText(mContext.getString(R.string.share_platform_qq))
                    .setIconColor(ShareIcon.COLOR_QQ);
                break;
            case ShareIcon.SHARE_QZONE:
                builder.setIconfont(mContext.getString(R.string.ic_qzone))
                    .setIconResourceId(R.drawable.icon_share_wechat)
                    .setIconText(mContext.getString(R.string.share_platform_qzone))
                    .setIconColor(ShareIcon.COLOR_QQ);
                break;
            case ShareIcon.SHARE_SMS:
                builder.setIconfont(mContext.getString(R.string.ic_share_messagr))
                    .setIconResourceId(R.drawable.icon_share_wechat)
                    .setIconText(mContext.getString(R.string.share_platform_msg))
                    .setIconColor(ShareIcon.COLOR_SMS);
                break;
            case ShareIcon.SHARE_COPY:
                builder.setIconfont(mContext.getString(R.string.ic_share_messagr))
                    .setIconResourceId(R.drawable.icon_share_wechat)
                    .setIconText(mContext.getString(R.string.share_platform_copy))
                    .setIconColor(ShareIcon.COLOR_COPY_URL);
                break;
            default:
                break;
        }
        return builder.build(mContext);
    }

    public ArrayList<ShareIcon> getShareIcons() {
        return shareIcons;
    }
}
