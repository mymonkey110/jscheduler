package com.haoocai.jscheduler.core.zk;

/**
 * @author Michael Jiang on 16/3/25.
 */
public class ZKRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -7952890645635888751L;

    public ZKRuntimeException() {
    }

    public ZKRuntimeException(String message) {
        super(message);
    }

    public ZKRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZKRuntimeException(Throwable cause) {
        super(cause);
    }

    public ZKRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
