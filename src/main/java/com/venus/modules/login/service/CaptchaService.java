package com.venus.modules.login.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CaptchaService {

    /**
     * 缓存验证码
     */
    void setCache(String key, String value);
    /**
     * 图片验证码
     */
    void create(HttpServletResponse response, String uuid) throws IOException;

    /**
     * 验证码效验
     *
     * @param uuid uuid
     * @param code 验证码
     */
    boolean validate(String uuid, String code);
}

