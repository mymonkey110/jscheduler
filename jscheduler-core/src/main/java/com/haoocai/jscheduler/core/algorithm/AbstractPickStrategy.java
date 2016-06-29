package com.haoocai.jscheduler.core.algorithm;

import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

/**
 * @author Michael Jiang on 16/6/15.
 */
public abstract class AbstractPickStrategy implements Picker{
    protected final ZKAccessor zkAccessor;
    protected final Task task;

    public AbstractPickStrategy(ZKAccessor zkAccessor, Task task) {
        this.zkAccessor = zkAccessor;
        this.task = task;
    }
}
