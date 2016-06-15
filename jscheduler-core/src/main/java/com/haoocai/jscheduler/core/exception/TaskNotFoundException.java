package com.haoocai.jscheduler.core.exception;

/**
 * Task not found exception
 *
 * @author Michael Jiang on 16/6/15.
 */
public class TaskNotFoundException extends AbstractCheckedException {
    private static final long serialVersionUID = -4853068764256693096L;

    @Override
    protected ErrorCode errorCode() {
        return ErrorCode.TASK_NOT_FOUND;
    }
}
