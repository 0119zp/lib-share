package com.zpan.sharelib.sns.util;

import java.util.regex.Pattern;

/**
 * @author zpan
 * @date 2019/3/20
 */
public class CheckUrlUtils {
    public static boolean isFromLocal(String data) {
        return Pattern.matches(".*http.*", data);
    }
}
