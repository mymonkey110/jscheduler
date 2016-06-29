package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.zk.ZKAccessor;

/**
 * Task Status Node, Ephemeral Node
 *
 * @author Michael Jiang on 6/28/16.
 */
class StatusNode extends AbstractNode {
    private final static String ROOT = "/status";

    private StatusNode(ZKAccessor zkAccessor, TaskID taskID) {
        super(zkAccessor, taskID);
    }

    @Override
    NodeIdentify identify() {
        return NodeIdentify.STATUS;
    }

    @Override
    void init() {
        zkAccessor.createEphemeralNode(taskID.identify() + "/status", "RUNNING".getBytes());
    }

    boolean isRunning() {
        return zkAccessor.checkNodeExist(taskID.identify() + ROOT);
    }

    static StatusNode load(ZKAccessor zkAccessor, TaskID taskID) {
        return new StatusNode(zkAccessor, taskID);
    }
}
