package com.zpan.sharelib.sns.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.reflect.Type;
import java.util.List;

/**
 * json工具类<br>
 * http://wangym.iteye.com/blog/738933<br>
 * http://www.cnblogs.com/windlaughing/p/3241776.html<br>
 * <br>
 * fastjson有bug 属性名首字母小写但第二个字母大写的情况会无法解析javabean属性丢失 https://github.com/alibaba/fastjson/pull/106
 * <br>
 * <br>
 * 性能考虑，使用jackon
 *
 * <br>
 * jackson不是针对android平台的json lib, 在android 5.0以上会引起IncompatibleClassChangeError错误
 * https://github.com/FasterXML/jackson-databind/issues/782
 * 上述fastjson的bug目前官方已解决，故换回fastjson，针对android平台的json解析
 * <br>
 * fastjson相对于jackson比较轻量级，依赖jar比较少
 *
 * @author zpan
 * @date 2019/3/20
 */
public class JsonUtil {

    /**
     * 解析实体
     *
     * @param jsonStr json字符串
     * @param entityClass 实体类型
     * @param <T> 实体对象
     */
    public static <T> T parseObject(String jsonStr, Class<T> entityClass) {
        T ret = null;

        try {
            ret = JSON.parseObject(jsonStr, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static <T> T parseObject(String jsonStr, Type type) {
        T obj = null;
        try {
            obj = JSON.parseObject(jsonStr, type, Feature.AutoCloseSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> T parseObject(String jsonStr, TypeReference<T> tf) {
        T obj = null;
        try {
            obj = JSON.parseObject(jsonStr, tf, Feature.AutoCloseSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 解析List
     */
    public static <T> List<T> parseList(String jsonStr, Class<T> entityClass) {
        List<T> ret = null;

        try {
            ret = JSON.parseArray(jsonStr, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String toJSONString(Object obj) {
        String ret = null;

        try {
            ret = JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}
