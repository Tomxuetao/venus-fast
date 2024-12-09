package com.venus.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * String 工具类
 *
 * @author Tomxuetao
 */
public class StrUtils {
    private static final Logger logger = LoggerFactory.getLogger(StrUtils.class);

    public static String randomCode(int len) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
