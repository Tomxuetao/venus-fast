package com.venus.modules.sys.enums;

public enum LoginTypeEnum {
    USERNAME(1),
    Account(2);
    private final int value;

    LoginTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
