package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.zk.ZKAccessor;

/**
 * @author Michael Jiang on 6/28/16.
 */
public class StatusNode {
    private final ZKAccessor zkAccessor;
    private final TaskID taskID;
    private final static String ROOT = "/status";

    public StatusNode(ZKAccessor zkAccessor, TaskID taskID) {
        this.zkAccessor = zkAccessor;
        this.taskID = taskID;
    }

    public boolean isRunning() {
        return zkAccessor.checkNodeExist(taskID.identify() + ROOT);
    }
}
