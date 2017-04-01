package com.ladddd.mylib.event;

/**
 * Created by 陈伟达 on 2017/3/30.
 */

public class BaseEvent<T> {

    private int code;
    private T data;

    public BaseEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
