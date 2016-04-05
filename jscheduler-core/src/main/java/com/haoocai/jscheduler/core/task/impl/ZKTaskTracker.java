package com.haoocai.jscheduler.core.task.impl;


import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskTracker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.lang3.Validate;

import java.util.TimerTask;

/**
 * Zookeeper task tracker implementation
 *
 * @author Michael Jiang on 16/4/5.
 */
public class ZKTaskTracker implements TaskTracker {
    private final ZKManager zkManager;
    private final TaskDescriptor taskDescriptor;

    public ZKTaskTracker(ZKManager zkManager, TaskDescriptor taskDescriptor) {
        Validate.notNull(zkManager);
        Validate.notNull(taskDescriptor);

        this.zkManager = zkManager;
        this.taskDescriptor = taskDescriptor;
    }

    //todo
    @Override
    public void track() {
        String cronExpression = taskDescriptor.getCronExpression();
        //TimerTask timerTask =
    }
}
