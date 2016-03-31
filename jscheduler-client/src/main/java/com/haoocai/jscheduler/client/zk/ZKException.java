package com.haoocai.jscheduler.client.zk;

/**
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public class ZKException extends Exception {

    private static final long serialVersionUID = -3889767496723782130L;

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
