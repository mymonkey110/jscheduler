package com.haoocai.jscheduler.web;

/**
 * Common Result Encapsulation
 *
 * @author Michael Jiang on 16/5/12.
 */
public class CommonResult<T> {
    private int code;
    private T data;

    public CommonResult(int code, T data) {
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

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
