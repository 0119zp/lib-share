package com.zpan.sharelib.sns.weibo;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import com.handsome.sharelib.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.Utility;
import com.zpan.sharelib.sns.config.SnsMedia;
import com.zpan.sharelib.sns.config.SnsMediaType;
import com.zpan.sharelib.sns.config.SnsPlatform;
import java.io.File;
import java.util.ArrayList;

/**
 * https://github.com/sinaweibosdk/weibo_android_sdk
 *
 * @author zpan
 * @date 2019/3/25
 */
public class WeiboHandler extends BaseWeiboHandler {

    private Activity mActivity;

    public WeiboHandler(Activity activity, SnsPlatform platform) {
        super(activity, platform);
        this.mActivity = activity;
    }

    @Override
    public void sendMessage(SnsMedia media) {
        String content;
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        ImageObject imageObject = new ImageObject();
        // 有content取content内容， 没有则取title
        if (!TextUtils.isEmpty(media.content)) {
            content = media.content;
        } else {
            content = media.title;
        }

        content += "  " + media.webpageUrl;
        textObject.text = content;
        if (media.getBitmapData() != null) {
            imageObject.imageData = media.getBitmapData();
        } else if (media.getLocalImageUrl() != null) {
            imageObject.imagePath = media.getLocalImageUrl();
        } else {
            Resources r = mActivity.getResources();
            imageObject.setImageObject(BitmapFactory.decodeResource(r, R.mipmap.ic_launcher));
        }
        if (media.type != SnsMediaType.TYPE_IMAGE) {
            weiboMultiMessage.textObject = textObject;
        }
        weiboMultiMessage.imageObject = imageObject;
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }

    /**
     * 创建多媒体（网页）消息对象-预留
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "测试title";
        mediaObject.description = "测试描述";
        Resources resources = mActivity.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里   设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://news.sina.com.cn/c/2013-10-22/021928494669.shtml";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    /***
     * 创建多图-预留
     * pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博
     * 最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效可以通过WbSdk.hasSupportMultiImage
     * 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成
     * 分享失败
     * @return 多图对象
     */
    private MultiImageObject getMultiImageObject() {
        MultiImageObject multiImageObject = new MultiImageObject();
        ArrayList<Uri> pathList = new ArrayList<>();
        pathList.add(Uri.fromFile(new File("/test.png")));
        pathList.add(Uri.fromFile(new File("/test1.jpg")));
        multiImageObject.setImageList(pathList);
        return multiImageObject;
    }

    /**
     * 获取视频-预留
     *
     * @return 视频对象
     */
    private VideoSourceObject getVideoObject() {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.videoPath = Uri.fromFile(new File("/test.mp4"));
        return videoSourceObject;
    }
}
