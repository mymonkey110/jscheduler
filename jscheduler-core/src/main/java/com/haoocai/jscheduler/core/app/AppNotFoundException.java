package com.haoocai.jscheduler.core.app;

import com.haoocai.jscheduler.core.exception.AbstractCheckedException;

/**
 * @author Michael Jiang on 16/3/25.
 */
public class AppNotFoundException extends AbstractCheckedException {
    private static final long serialVersionUID = -7059596686748306753L;

    @Override
    protected ErrorCode errorCode() {
        return ErrorCode.APP_NOT_FOUND;
    }
}
