package com.haoocai.jscheduler.core.app;

import com.haoocai.jscheduler.core.AbstractCheckedException;

/**
 * @author Michael Jiang on 16/5/30.
 */
public class NamespaceNotExistException extends AbstractCheckedException {
    private static final long serialVersionUID = 8473772222314849825L;


    @Override
    protected ErrorCode errorCode() {
        return ErrorCode.NAMESPACE_NOT_FOUND;
    }
}
