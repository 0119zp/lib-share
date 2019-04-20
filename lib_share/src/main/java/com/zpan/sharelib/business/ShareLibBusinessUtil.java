package com.zpan.sharelib.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;
import com.zpan.sharelib.bean.ShareLibBean;
import com.zpan.sharelib.bean.ShareLibDetailBean;
import com.zpan.sharelib.icon.ShareIcon;

/**
 * 自定义分享模板信息，保证shareBean数据模型有效性
 *
 * @author zpan
 */
public class ShareLibBusinessUtil {

    private static final String DEFAULT_SHARE_WEBURL = "http://www.pinganfang.com";
    private static final String WEB_URL_CONTAINS = "?";

    public static ShareLibBean buildShareBean(Context context, int platform, ShareLibBean shareLibBean, ShareSourceTypeEnum sourceType) {
        if (null == shareLibBean) {
            return shareLibBean;
        }

        String shareTitle = buildShareTitle(platform, shareLibBean);
        String shareIconUrl = buildShareIconUrl(shareLibBean);
        int shareIconResId = shareLibBean.getShareIconResId();
        Bitmap shareIcon = shareLibBean.getShareIcon();
        String shareWebUrl = buildShareWebUrl(platform, shareLibBean);
        // TODO 分享内容模板待确定 zpan 2019/3/21
        String shareContent = buildShareContent(context, platform, shareLibBean, sourceType);

        // 重构分享实体类，避免原分享数据被污染
        ShareLibBean finalShareLibBean = new ShareLibBean();
        finalShareLibBean.setShareTitle(shareTitle);
        finalShareLibBean.setShareIconUrl(shareIconUrl);
        finalShareLibBean.setShareIconResId(shareIconResId);
        finalShareLibBean.setShareIcon(shareIcon);
        finalShareLibBean.setShareWebUrl(shareWebUrl);
        finalShareLibBean.setShareContent(shareContent);

        return finalShareLibBean;
    }

    /**
     * 构造分享标题
     *
     * @param platform 分享平台
     * @param shareLibBean 分享数据
     * @return 分享出去的标题
     */
    private static String buildShareTitle(int platform, ShareLibBean shareLibBean) {
        String shareTitle = "";
        switch (platform) {
            case ShareIcon.SHARE_WEIXIN:
            case ShareIcon.SHARE_WEIXIN_CIRCLE:
            case ShareIcon.SHARE_QZONE:
            case ShareIcon.SHARE_QQ:
            case ShareIcon.SHARE_SMS:
            case ShareIcon.SHARE_WEIBO:
            case ShareIcon.SHARE_COPY:
                shareTitle = shareLibBean.getShareTitle();
                if (TextUtils.isEmpty(shareTitle)) {
                    // TODO 默认消息头部待确认
                    shareTitle = "【微信分享头部】";
                }
                break;
            default:
                break;
        }
        return shareTitle;
    }

    /**
     * 构造分享图标 url
     *
     * @param shareLibBean 分享数据
     * @return 分享图标url
     */
    private static String buildShareIconUrl(ShareLibBean shareLibBean) {
        String shareIconUrl = shareLibBean.getShareIconUrl();
        if (TextUtils.isEmpty(shareIconUrl)) {
            return "";
        }
        return shareIconUrl;
    }

    /**
     * 构造分享链接
     *
     * @param platform 分享平台
     * @param shareLibBean 分享数据
     * @return 分享出去链接；优先获取ShareLibDetailBean中的web url
     */
    private static String buildShareWebUrl(int platform, ShareLibBean shareLibBean) {
        String shareWebUrl = shareLibBean.getShareWebUrl();
        if (TextUtils.isEmpty(shareWebUrl)) {
            shareWebUrl = DEFAULT_SHARE_WEBURL;
        }

        ShareLibDetailBean libDetailBean = shareLibBean.getShareLibDetailBean();
        if (null != libDetailBean) {
            String shareDetailUrl = libDetailBean.getShareUrl();
            if (!TextUtils.isEmpty(shareDetailUrl)) {
                shareWebUrl = shareDetailUrl;
            }
        }

        String platForm = getPlatForm(platform);
        if (shareWebUrl.contains(WEB_URL_CONTAINS)) {
            shareWebUrl = shareWebUrl + "&_share=" + platForm;
        } else {
            shareWebUrl = shareWebUrl + "?_share=" + platForm;
        }
        return shareWebUrl;
    }

    /**
     * 构造分享内容模板
     *
     * @param platform 平台
     * @param shareLibBean 分享数据
     * @param sourceType 分享来源类型, 区分分享模板
     * @return 分享内容
     */
    private static String buildShareContent(Context context, int platform, ShareLibBean shareLibBean, ShareSourceTypeEnum sourceType) {
        if (null == shareLibBean) {
            Toast.makeText(context, "未获取到分享内容", Toast.LENGTH_SHORT).show();
            return "";
        }

        ShareLibDetailBean shareDetailBean = shareLibBean.getShareLibDetailBean();
        String shareContent = shareLibBean.getShareContent();

        // 如果无详情业务数据直接返回通用分享内容
        if (null == shareDetailBean) {
            if (!TextUtils.isEmpty(shareLibBean.getShareWebUrl())) {
                String tag;
                if (shareLibBean.getShareWebUrl().contains(WEB_URL_CONTAINS)) {
                    tag = "&";
                } else {
                    tag = "?";
                }

                if (platform == ShareIcon.SHARE_COPY) {
                    shareContent = shareLibBean.getShareWebUrl() + tag + "_share=" + getPlatForm(platform);
                } else if (platform == ShareIcon.SHARE_WEIBO) {
                    shareContent += "\n" + "点击查看：" + shareLibBean.getShareWebUrl() + tag + "_share=" + getPlatForm(platform);
                } else if (platform == ShareIcon.SHARE_SMS) {
                    shareContent += shareLibBean.getShareWebUrl() + tag + "_share=" + getPlatForm(platform);
                }
            }
            return shareContent;
        }

        switch (sourceType) {
            case TYPE_OTHER:
                shareContent = shareLibBean.getShareContent();
                break;
            case TYPE_HANWEI:
                shareContent = shareLibBean.getShareContent();
                break;
            default:
                break;
        }

        return shareContent;
    }

    /**
     * 获得平台标识
     *
     * @param snsPlatform 平台
     * @return 平台标识
     */
    private static String getPlatForm(int snsPlatform) {
        String platForm = "";
        switch (snsPlatform) {
            case ShareIcon.SHARE_WEIXIN:
                platForm = "wxhy";
                break;
            case ShareIcon.SHARE_WEIXIN_CIRCLE:
                platForm = "wxpyq";
                break;
            case ShareIcon.SHARE_QZONE:
                platForm = "qqkj";
                break;
            case ShareIcon.SHARE_QQ:
                platForm = "qq";
                break;
            case ShareIcon.SHARE_SMS:
                platForm = "sms";
                break;
            case ShareIcon.SHARE_WEIBO:
                platForm = "sinawb";
                break;
            case ShareIcon.SHARE_COPY:
                platForm = "copy";
                break;
            default:
                break;
        }
        return platForm;
    }
}
