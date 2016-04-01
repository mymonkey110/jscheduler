package com.haoocai.jscheduler.core.task;

/**
 * @author Michael Jiang on 16/3/25.
 */
public class TaskException extends Exception {
    private static final long serialVersionUID = -7059596686748306753L;

    public TaskException() {
    }

    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskException(Throwable cause) {
        super(cause);
    }

    public TaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
