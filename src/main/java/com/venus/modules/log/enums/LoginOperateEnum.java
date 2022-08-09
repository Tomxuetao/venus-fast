package com.venus.modules.log.enums;

public enum LoginOperateEnum {
    /**
     * 用户登录
     */
    LOGIN(0),
    /**
     * 用户退出
     */
    LOGOUT(1);

    private final int value;

    LoginOperateEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
