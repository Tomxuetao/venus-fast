package com.venus.modules.msg.enums;

public enum SendTypeEnum {
    FAIL(0),
    SUCCESS(1);
    private final int value;

    SendTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
