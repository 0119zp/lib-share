package com.zpan.sharelib.sns.util;

import android.graphics.Bitmap;

/**
 * 基础图片工具类
 *
 * @author zpan
 * @date 2019/3/20
 */
public class ImageUtil {

    /**
     * 压缩Bitmap至指定大小
     *
     * @param isSameScaleRate 是否保持一致的压缩比, true:缩放后将大于指定width和height的裁剪掉
     * false:仅按比例缩放保证图片宽和高不大于width和height
     */
    public static Bitmap getInSampleBitmap(Bitmap bmp, int destWidth, int destHeight, boolean isSameScaleRate, boolean isRecyle) {
        if (bmp == null) {
            return null;
        }
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        // 压缩后的图片比原图大, 则返回原图
        if (width < destWidth && height < destHeight) {
            return bmp;
        }

        if (destWidth == 0) {
            destWidth = -1;
        }

        if (destHeight == 0) {
            destHeight = -1;
        }
        Bitmap b = null;
        if (isSameScaleRate) {
            float widthScale = width * 1.0f / destWidth;
            float heightScale = height * 1.0f / destHeight;
            float scale = widthScale > heightScale ? widthScale : heightScale;
            b = Bitmap.createScaledBitmap(bmp, (int) (width / scale), (int) (height / scale), true);
        } else {
            b = Bitmap.createScaledBitmap(bmp, destWidth, destHeight, true);
        }
        if (isRecyle && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
        return b;
    }
}
