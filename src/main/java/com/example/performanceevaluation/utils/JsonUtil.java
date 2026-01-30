package com.example.performanceevaluation.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONWriter;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类（基于FastJSON2）
 */
public class JsonUtil {

    /**
     * 对象转JSON字符串
     */
    public static String toJsonString(Object obj) {
        return obj == null ? null : JSON.toJSONString(obj);
    }

    /**
     * 对象转JSON字符串（格式化）
     */
    public static String toJsonStringPretty(Object obj) {
        return obj == null ? null : JSON.toJSONString(obj, JSONWriter.Feature.PrettyFormat);
    }

    /**
     * JSON字符串转对象
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        return StringUtil.isBlank(jsonStr) ? null : JSON.parseObject(jsonStr, clazz);
    }

    /**
     * JSON字符串转List
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        return StringUtil.isBlank(jsonStr) ? null : JSON.parseArray(jsonStr, clazz);
    }

    /**
     * JSON字符串转JSONObject
     */
    public static JSONObject parseObject(String jsonStr) {
        return StringUtil.isBlank(jsonStr) ? null : JSON.parseObject(jsonStr);
    }

    /**
     * JSON字符串转JSONArray
     */
    public static JSONArray parseArray(String jsonStr) {
        return StringUtil.isBlank(jsonStr) ? null : JSON.parseArray(jsonStr);
    }

    /**
     * 对象转JSONObject
     */
    public static JSONObject toJsonObject(Object obj) {
        return obj == null ? null : (JSONObject) JSON.toJSON(obj);
    }

    /**
     * 对象转JSONArray
     */
    public static JSONArray toJsonArray(Object obj) {
        return obj == null ? null : (JSONArray) JSON.toJSON(obj);
    }

    /**
     * Map转对象
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        return map == null ? null : JSON.parseObject(JSON.toJSONString(map), clazz);
    }

    /**
     * 对象转Map
     */
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        return jsonObject != null ? jsonObject.toJavaObject(Map.class) : null;
    }

    /**
     * 判断字符串是否为有效JSON
     */
    public static boolean isValidJson(String jsonStr) {
        if (StringUtil.isBlank(jsonStr)) {
            return false;
        }
        try {
            JSON.parse(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为有效JSON对象
     */
    public static boolean isValidJsonObject(String jsonStr) {
        if (StringUtil.isBlank(jsonStr)) {
            return false;
        }
        try {
            JSON.parseObject(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为有效JSON数组
     */
    public static boolean isValidJsonArray(String jsonStr) {
        if (StringUtil.isBlank(jsonStr)) {
            return false;
        }
        try {
            JSON.parseArray(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 深拷贝对象（通过JSON序列化/反序列化）
     */
    public static <T> T deepClone(T obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        String json = toJsonString(obj);
        return parseObject(json, clazz);
    }
}

