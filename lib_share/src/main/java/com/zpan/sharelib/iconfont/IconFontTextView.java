package com.zpan.sharelib.iconfont;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

/**
 * @author zpan
 */
public class IconFontTextView extends AppCompatTextView {

    private Context context;

    public IconFontTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public IconFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        //加载字体文件
        Typeface typeface = IconFontTypeFace.getTypeface(context);
        this.setTypeface(typeface);
        // 去掉padding,这样iconfont和普通字体容易对齐
        // setIncludeFontPadding(false);
    }

    public void addIconLeft(String icon, int iconColor, float fontSize) {
        CharSequence text = getText();
        this.setText("");
        this.addIcon(icon, iconColor, fontSize);
        this.append(text);
    }

    public void addIconRight(String icon, int iconColor, float fontSize) {
        this.addIcon(icon, iconColor, fontSize);
    }

    public void addIcon(String... icons) {
        for (String icon : icons) {
            addIcon(icon, 0, 0);
        }
    }

    public void addIcon(String icon, int iconColor, float fontSize) {
        Spannable sp = new SpannableString(icon);
        if (iconColor > 0) {
            sp.setSpan(new ForegroundColorSpan(iconColor), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (fontSize > 0) {
            int fontSizePx = sp2px(context, fontSize);
            sp.setSpan(new AbsoluteSizeSpan(fontSizePx), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        this.append(sp);
    }

    public void addVerticalCenterIcon(String icon, int iconColor, float fontSize) {
        Spannable sp = new SpannableString(icon);
        if (iconColor > 0) {
            sp.setSpan(new ForegroundColorSpan(iconColor), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (fontSize > 0) {
            int fontSizePx = sp2px(context, fontSize);
            //垂直居中显示文字
            sp.setSpan(new CustomVerticalCenterSpan(fontSizePx), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        this.append(sp);
    }

    public void setIcon(String icon, int iconColor, float fontSize) {
        Spannable sp = new SpannableString(icon);
        if (iconColor != 0) {
            sp.setSpan(new ForegroundColorSpan(iconColor), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (fontSize > 0) {
            int fontSizePx = sp2px(context, fontSize);
            sp.setSpan(new AbsoluteSizeSpan(fontSizePx), 0, icon.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        this.setText(sp);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static class IconFontTypeFace {

        private static Typeface ttfTypeface = null;

        public static synchronized Typeface getTypeface(Context context) {
            if (ttfTypeface == null) {
                try {
                    ttfTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.ttf");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ttfTypeface;
        }

        public static synchronized void clearTypeface() {
            ttfTypeface = null;
        }
    }
}
