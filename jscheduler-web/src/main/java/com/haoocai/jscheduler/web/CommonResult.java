package com.haoocai.jscheduler.web;

/**
 * Common Result Encapsulation
 *
 * @author Michael Jiang on 16/5/12.
 */
public class CommonResult<T> {
    private String code;
    private T data;

    public CommonResult(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
                "code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
