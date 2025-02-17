package com.venus.common.exception;

import com.venus.common.utils.MessageUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VenusException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;

    public VenusException(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(code);
    }

    public VenusException(int code, String... params) {
        this.code = code;
        this.msg = MessageUtils.getMessage(code, params);
    }

    public VenusException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code);
    }

    public VenusException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code, params);
    }

    public VenusException(String msg) {
        super(msg);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public VenusException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

}
