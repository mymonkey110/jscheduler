package com.haoocai.jscheduler.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haoocai.jscheduler.core.AbstractCheckedException;

/**
 * Common Result Encapsulation
 *
 * @author Michael Jiang on 16/5/12.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommonResult<T> {
    private int code;
    private String msg;
    private T data;

    private CommonResult() {
    }

    public static CommonResult errorOf(AbstractCheckedException e) {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(e.code());
        commonResult.setMsg(e.getMessage());
        return commonResult;
    }

    public static CommonResult successRet() {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(AbstractCheckedException.successCode());
        return commonResult;
    }

    public CommonResult(T data) {
        setCode(AbstractCheckedException.successCode());
        setData(data);
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    private void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    private void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
