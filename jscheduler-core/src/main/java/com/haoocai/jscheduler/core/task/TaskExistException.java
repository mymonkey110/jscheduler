package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.exception.AbstractCheckedException;

/**
 * @author Michael Jiang on 16/5/31.
 */
public class TaskExistException extends AbstractCheckedException {
    private static final long serialVersionUID = -6542554148778284414L;

    @Override
    protected ErrorCode errorCode() {
        return ErrorCode.TASK_ALREADY_EXIST;
    }
}
