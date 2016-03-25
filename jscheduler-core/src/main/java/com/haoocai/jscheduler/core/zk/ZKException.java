package com.haoocai.jscheduler.core.zk;

/**
 * @author mymonkey110@gmail.com on 16/3/25.
 */
public class ZKException extends Exception {
    private static final long serialVersionUID = -6866020752033257046L;

    public ZKException() {
    }

    public ZKException(String message) {
        super(message);
    }

    public ZKException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZKException(Throwable cause) {
        super(cause);
    }

    public ZKException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
