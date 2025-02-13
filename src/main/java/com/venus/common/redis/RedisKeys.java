package com.venus.common.redis;

public class RedisKeys {
    /**
     * 系统参数Key
     */
    public static String getSysParamsKey() {
        return "sys:params";
    }

    /**
     * 授权验证码Key
     */
    public static String getAuthCodeKey(String code) {
        return "auth:code:" + code;
    }

    /**
     * 授权Token Key
     */
    public static String getAuthTokenKey(String accessToken) {
        return "auth:token:" + accessToken;
    }

    /**
     * 登录Token Key
     */
    public static String getTokenKey(String token) {
        return "sys:token:" + token;
    }

    /**
     * 验证码Key
     */
    public static String getCaptchaKey(String uuid) {
        return "sys:captcha:" + uuid;
    }

    /**
     * 数据权限Key
     */
    public static String getDataScopeKey(Long userId) {
        return "sys:data:" + userId;
    }

    /**
     * 登录用户Key
     */
    public static String getSecurityUserKey(Long id) {
        return "sys:user:" + id;
    }

    /**
     * 系统日志Key
     */
    public static String getSysLogKey() {
        return "sys:log";
    }

    /**
     * 系统资源Key
     */
    public static String getSysResourceKey() {
        return "sys:resource";
    }

    /**
     * 用户菜单导航Key
     */
    public static String getUserMenuNavKey(Long userId) {
        return "sys:user:nav:" + userId;
    }

    /**
     * 用户权限标识Key
     */
    public static String getUserPermissionsKey(Long userId) {
        return "sys:user:permissions:" + userId;
    }
}
