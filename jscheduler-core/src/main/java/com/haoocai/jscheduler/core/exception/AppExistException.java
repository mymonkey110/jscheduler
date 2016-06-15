package com.haoocai.jscheduler.core.exception;

import com.haoocai.jscheduler.core.exception.AbstractCheckedException;

/**
 * @author Michael Jiang on 16/5/30.
 */
public class AppExistException extends AbstractCheckedException {
    private static final long serialVersionUID = 6091411486818687787L;

    @Override
    protected ErrorCode errorCode() {
        return ErrorCode.APP_ALREADY_EXIST;
    }
}
