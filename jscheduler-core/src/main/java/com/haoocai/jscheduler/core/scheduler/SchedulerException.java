package com.haoocai.jscheduler.core.scheduler;

/**
 * @author Michael Jiang on 16/5/11.
 */
public class SchedulerException extends RuntimeException {
    private static final long serialVersionUID = -5072350010881812000L;

    public SchedulerException() {
    }

    public SchedulerException(String message) {
        super(message);
    }

    public SchedulerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchedulerException(Throwable cause) {
        super(cause);
    }

    public SchedulerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
