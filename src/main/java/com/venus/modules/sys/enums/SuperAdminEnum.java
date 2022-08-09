package com.venus.modules.sys.enums;

public enum SuperAdminEnum {
    YES(1),
    NO(0);

    private final int value;

    SuperAdminEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
