package com.swj.ics.redislockframework;

/**
 * Created by swj on 2017/11/20.
 */
public class CacheLockException extends Throwable {
    private String msg;

    public CacheLockException(String msg) {
        this.msg = msg;
    }

    public CacheLockException() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
