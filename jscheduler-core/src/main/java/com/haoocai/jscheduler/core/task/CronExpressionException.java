package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.exception.AbstractCheckedException;

/**
 * Cron Expression Error Exception
 *
 * @author Michael Jiang on 16/6/14.
 */
public class CronExpressionException extends AbstractCheckedException {
    private static final long serialVersionUID = -7421764173556457519L;

    @Override
    protected ErrorCode errorCode() {
        return ErrorCode.CRON_EXPRESSION_ERROR;
    }
}
