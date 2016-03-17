package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.core.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public class TaskInvokerImpl implements TaskInvoker {
    private final ZooKeeper zooKeeper;

    public TaskInvokerImpl(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    @Override
    public void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit) {

    }
}
