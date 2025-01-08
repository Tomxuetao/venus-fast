package com.venus.common.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class SseMsg<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int type;

    private T body;

    public SseMsg() {
    }

    public SseMsg(int type, T body) {
        this.type = type;
        this.body = body;
    }
}
