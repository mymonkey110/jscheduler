/*
 * Copyright 2016  Michael Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haoocai.jscheduler.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.haoocai.jscheduler.core.exception.AbstractCheckedException;

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
        commonResult.setMsg(e.msg());
        return commonResult;
    }

    public static CommonResult errorOf(AbstractCheckedException.ErrorCode errorCode) {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(errorCode.code());
        commonResult.setMsg(errorCode.msg());
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
