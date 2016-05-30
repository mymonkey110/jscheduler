package com.haoocai.jscheduler.core;

/**
 * @author Michael Jiang on 16/5/16.
 */
public class ErrorCode {
    public final static int SUCCESS = 1000;

    public final static int PARAM_ERROR = 1001;
    public final static int ILLEGAL_REQUEST = 1002;
    public final static int SYS_ERROR = 1003;

    public final static int NAMESPACE_NOT_FOUND = 2001;
    public final static int NAMESPACE_ALREADY_EXIST = 2002;
    public final static int APP_NOT_FOUND = 2003;
    public final static int APP_ALREADY_EXIST = 2004;
    public final static int TASK_NOT_FOUND = 2005;
    public final static int TASK_ALREADY_EXIST = 2006;

    public final static int ZK_ERROR = 3001;
    public final static int NODE_NOT_EXIST = 3002;
    public final static int NODE_ALREADY_EXIST = 3003;
}
