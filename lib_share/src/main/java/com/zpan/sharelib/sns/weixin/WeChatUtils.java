package com.zpan.sharelib.sns.weixin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage.IMediaObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zpan.sharelib.sns.config.SnsConstants;
import com.zpan.sharelib.sns.util.CheckUrlUtils;
import java.io.ByteArrayOutputStream;

/**
 * 微信分享工具类
 * 1.1.1版本新增 网络,本地，byte[]形式的分享方式(本地,网络方式暂未开通对外实现)
 * 优先级-> byte[]>bitmap>本地>网络
 * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN
 *
 * @author zpan
 * @date 2019/3/20
 */
public class WeChatUtils {

    private IWXAPI api;
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    public WeChatUtils(Activity context) {
        api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(SnsConstants.SNS_WEIXIN_APP_ID);
    }

    /**
     * @param bmp 图片对象
     * @param url webpage地址
     */
    public void share(Bitmap bmp, String url, String title, String description, boolean isMoment) {
        if (!TextUtils.isEmpty(url)) {
            shareWebPage(bmp, url, title, description, isMoment);
        } else if (bmp != null) {
            shareImageFromBitmap(bmp, title, description, isMoment);
        } else if (!title.isEmpty()) {
            shareOnlyText(title, isMoment);
        }
    }

    /**
     * @param data 图片字节数组
     * @param url webpage地址
     */
    public void share(byte[] data, String url, String title, String description, boolean isMoment) {
        if (!TextUtils.isEmpty(url)) {
            shareWebPage(data, url, title, description, isMoment);
        } else if (data != null) {
            shareImageFromByteArray(data, title, description, isMoment);
        } else if (!title.isEmpty()) {
            shareOnlyText(title, isMoment);
        }
    }

    /**
     * 1.1.1版本暂未对外开通
     */
    public void shareImageFromUrl(String url, String title, String description, boolean isMoment) {
        WXImageObject wxImageObject = new WXImageObject();
        if (CheckUrlUtils.isFromLocal(url)) {
            wxImageObject.setImagePath(url);
        } else {
            wxImageObject.imagePath = url;
        }
        //url模式的发送传递
        sendMsgFromUrl(url, title, description, wxImageObject, isMoment);
    }

    public void shareImageFromBitmap(Bitmap bmp, String title, String description, boolean isMoment) {
        WXImageObject imgObj = new WXImageObject(bmp);
        sendMsg(bmp, title, description, imgObj, isMoment);
    }

    public void shareImageFromByteArray(byte[] data, String title, String description, boolean isMoment) {
        WXImageObject imgObj = new WXImageObject(data);
        sendMsg(data, title, description, imgObj, isMoment);
    }

    /**
     * 分享网页
     *
     * @param bmp 图片数据源
     * @param webpageUrl webpage地址
     * @param webpageTitle 网页标题
     * @param webpageDescription 网页描述
     * @param isMoment 显示类别
     */
    public void shareWebPage(Bitmap bmp, String webpageUrl, String webpageTitle, String webpageDescription, boolean isMoment) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = webpageTitle;
        msg.description = webpageDescription;
        if (bmp != null) {
            msg.setThumbImage(getScaledBitmap(bmp));
        }
        apiSend(msg, isMoment);
    }

    /**
     * 分享网页
     *
     * @param data 图片数据源
     * @param webpageUrl webpage地址
     * @param webpageTitle 网页标题
     * @param webpageDescription 网页描述
     * @param isMoment 显示类别
     */
    public void shareWebPage(byte[] data, String webpageUrl, String webpageTitle, String webpageDescription, boolean isMoment) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = webpageTitle;
        msg.description = webpageDescription;
        if (data != null) {
            msg.thumbData = data;
        }
        apiSend(msg, isMoment);
    }

    private void sendMsg(Bitmap bmp, String title, String description, IMediaObject mediaObj, boolean isMoment) {
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = mediaObj;
        msg.title = title;
        msg.description = description;
        if (bmp != null) {
            msg.setThumbImage(getScaledBitmap(bmp));
        }
        apiSend(msg, isMoment);
        // mContext.startActivity(new Intent(mContext, WXEntryActivity.class));
    }

    /**
     * 微信暂未开通相关支持，不实现内容
     */
    private void sendMsgFromUrl(String url, String title, String description, IMediaObject mediaObj, boolean isMoment) {
    }

    private void sendMsg(byte[] data, String title, String description, IMediaObject mediaObj, boolean isMoment) {
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = mediaObj;
        msg.title = title;
        msg.description = description;
        if (data != null) {
            msg.thumbData = data;
        }
        apiSend(msg, isMoment);
        // mContext.startActivity(new Intent(mContext, WXEntryActivity.class));
    }

    //发送分享请求
    private void apiSend(WXMediaMessage msg, boolean isMoment) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction();
        req.message = msg;
        req.scene = isMoment ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 只分享分本内容
     *
     * @param title 分享的文本
     * @param isMoment 朋友圈还是好友
     */
    private void shareOnlyText(String title, boolean isMoment) {
        //构造文本对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = title;
        //构造消息对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = title;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction();
        req.message = msg;
        req.scene = isMoment ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 生成唯一key
     *
     * @return 唯一key
     */
    private String buildTransaction() {
        return String.valueOf(System.currentTimeMillis() + SnsConstants.SNS_WEIXIN_APP_ID);
    }

    /**
     * get getScaledBitmap width and height less than 200, required size less than 32KB.
     */
    public static Bitmap getScaledBitmap(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int size = width > height ? width : height;
        double scale = 1;

        if (size > 200) {
            scale = size * 1.0 / 200;
        }

        return Bitmap.createScaledBitmap(bmp, (int) (width / scale), (int) (height / scale), true);
    }

    public boolean isWXAppSupportMoment() {
        int wxSdkVersion = api.getWXAppSupportAPI();
        return wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
    }

    public boolean isWXAppInstalled() {
        return api.isWXAppInstalled();
    }

    public boolean launchWechat() {
        return api.openWXApp();
    }

    private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 80, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void shareApp(Bitmap bmp, String title, String description, String extInfo, boolean isMoment) {
        final WXAppExtendObject appdata = new WXAppExtendObject();
        appdata.fileData = bmpToByteArray(bmp, false);
        appdata.extInfo = extInfo;

        sendMsg(bmp, title, description, appdata, isMoment);
    }

    public void shareMusic(Bitmap bmp, String url, String title, String description, boolean isMoment) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = url;

        sendMsg(bmp, title, description, music, isMoment);
    }
}
