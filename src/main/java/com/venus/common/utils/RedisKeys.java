package com.venus.common.utils;

/**
 * Redis所有Keys
 *
 * @author Tomxuetao
 */
public class RedisKeys {

    /**
     * 系统缓存Key
     *
     * @param key: String
     * @return String
     */
    public static String getSysConfigKey(String key) {
        return "sys:config:" + key;
    }

    /**
     * 地理空间数据缓存Key
     *
     * @param key: String
     * @return String
     */
    public static String getGeoConfigKey(String key) {
        return "geo:config:" + key;
    }
}
