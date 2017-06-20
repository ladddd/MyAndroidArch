package com.ladddd.mylib.rx.network;

/**
 * Created by 陈伟达 on 2017/6/19.
 */

public class StatusResponse<T> {
    private Status status = Status.OK;
    private T response;

    public enum Status {
        OK,
        NETERR,
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
