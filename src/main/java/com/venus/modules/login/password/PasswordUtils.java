package com.venus.modules.login.password;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Base64;

public class PasswordUtils {

    /**
     * 加密
     *
     * @param str 字符串
     * @return 返回加密字符串
     */
    public static String encode(String str) {
        return Base64.getEncoder().encodeToString(BCrypt.hashpw(str, BCrypt.gensalt()).getBytes());
    }


    /**
     * 比较密码是否相等
     *
     * @param str      明文密码
     * @param password 加密后密码
     * @return true：成功    false：失败
     */
    public static boolean matches(String str, String password) {
        return BCrypt.checkpw(str, new String(Base64.getDecoder().decode(password)));
    }


    public static void main(String[] args) {
        String str = "Wang#645678";
        String password = encode(str);

        System.out.println(password);
        System.out.println(matches(str, password));
    }
}
