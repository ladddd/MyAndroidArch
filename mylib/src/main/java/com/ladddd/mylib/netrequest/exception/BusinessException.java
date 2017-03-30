package com.ladddd.mylib.netrequest.exception;

/**
 * Created by 陈伟达 on 2017/3/29.
 */

public class BusinessException extends RuntimeException {
    private int errorCode;
    private String message;

    public BusinessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
