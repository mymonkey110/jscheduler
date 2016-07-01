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

package com.haoocai.jscheduler.core.exception;

/**
 * Abstract Base Checked Exception
 * <p>
 * All Server Checked Exception should inherit the class.
 * </p>
 *
 * @author Michael Jiang on 16/5/30.
 */
public abstract class AbstractCheckedException extends Exception {

    private static final long serialVersionUID = -3143228702981231790L;

    public AbstractCheckedException() {
    }

    protected abstract ErrorCode errorCode();

    public int code() {
        return errorCode().code();
    }

    public String msg() {
        return errorCode().msg();
    }

    public static int successCode() {
        return ErrorCode.SUCCESS.code();
    }

    public enum ErrorCode {
        SUCCESS(1000, "success"),
        PARAM_ERROR(1001, "parameter error"),
        ILLEGAL_REQUEST(1002, "illegal request"),
        SYS_ERROR(1003, "system error"),
        NAMESPACE_NOT_FOUND(2001, "namespace not found"),
        NAMESPACE_ALREADY_EXIST(2002, "namespace already exist"),
        APP_NOT_FOUND(2003, "app not found"),
        APP_ALREADY_EXIST(2004, "app already exist"),
        TASK_NOT_FOUND(2005, "task not found"),
        TASK_ALREADY_EXIST(2006, "task already exist"),
        CRON_EXPRESSION_ERROR(2007, "cron expression error"),
        ZOOKEEPER_ERROR(3001, "zookeeper error"),
        NODE_NOT_EXIST(3002, "node not exist"),
        NODE_ALREADY_EXIST(3003, "node already exist"),
        UNKNOWN_ERROR(9999, "unknown error");

        private int code;
        private String msg;

        ErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int code() {
            return code;
        }

        public String msg() {
            return msg;
        }

        public static ErrorCode getErrorCode(int code) {
            for (ErrorCode it : ErrorCode.values()) {
                if (it.code() == code) {
                    return it;
                }
            }
            return UNKNOWN_ERROR;
        }
    }
}
