package com.haoocai.jscheduler.core.task;

/**
 * @author Michael Jiang on 16/3/25.
 */
public class TaskException extends Exception {
    private static final long serialVersionUID = -7059596686748306753L;

    private int errorCode;
    private String errorMsg;

    public TaskException(int errorCode, String errorMsg) {
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }

    public TaskException(Throwable cause, int errorCode, String errorMsg) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
