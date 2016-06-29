package com.haoocai.jscheduler.core.algorithm.impl;

import com.haoocai.jscheduler.core.algorithm.AbstractPickStrategy;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael Jiang on 16/4/1.
 */
public class RoundRobinPicker extends AbstractPickStrategy {

    private static Logger LOG = LoggerFactory.getLogger(RoundRobinPicker.class);

    public RoundRobinPicker(ZKAccessor zkAccessor, Task task) {
        super(zkAccessor, task);
    }

    //todo
    @Override
    public SchedulerUnit assign() throws Exception {
        throw new UnsupportedOperationException();
    }
}
