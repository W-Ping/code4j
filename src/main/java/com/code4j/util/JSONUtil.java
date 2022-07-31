package com.code4j.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author liu_wp
 * @date 2020/11/3
 * @see
 */
public class JSONUtil {
    private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);

    /**
     * @param object
     * @return
     */
    public final static String Object2JSON(Object object) {

        return JSON.toJSONString(object);
    }

    public final static Map jsonToMap(String json) {
        return JSON.parseObject(json, Map.class);
    }

    public final static JSONObject Str2JSONObject(String str) {
        try {
            return JSON.parseObject(str);
        } catch (Exception e) {
            log.error("Str2JSONObject fail", e);
            return null;
        }
    }

    public final static <T> List<T> JSON2List(String jsonStr, Class<T> cls) {
        if (jsonStr == null || jsonStr.length() <= 0) {
            return null;
        }
        try {
            return JSON.parseArray(jsonStr, cls);
        } catch (Exception e) {
            log.error("JSON2List fail", e);
            return null;
        }
    }

    public final static <T> T JSON2Object(String jsonStr, Class<T> cls) {
        if (jsonStr == null || jsonStr.length() <= 0) {
            return null;
        }
        try {
            if (String.class.equals(cls)) {
                return (T) jsonStr;
            }
            return JSON.parseObject(jsonStr, cls);
        } catch (Exception e) {
            log.error("JSON2Object fail", e);
            return null;
        }
    }
}
