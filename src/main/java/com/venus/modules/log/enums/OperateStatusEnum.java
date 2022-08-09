package com.venus.modules.log.enums;

public enum OperateStatusEnum {
    /**
     * 失败
     */
    FAIL(0),
    /**
     * 成功
     */
    SUCCESS(1);

    private final int value;

    OperateStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
