package com.haoocai.jscheduler.core.algorithm;

import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

/**
 * @author Michael Jiang on 16/6/15.
 */
public abstract class AbstractPickStrategy implements Picker{
    protected final ZKAccessor zkAccessor;
    protected final TaskID taskID;

    public AbstractPickStrategy(ZKAccessor zkAccessor, TaskID taskID) {
        this.zkAccessor = zkAccessor;
        this.taskID = taskID;
    }
}
