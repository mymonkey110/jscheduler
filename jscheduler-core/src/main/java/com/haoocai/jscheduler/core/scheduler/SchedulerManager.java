package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.task.TaskDescriptor;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public interface SchedulerManager {

    void register();

    void reloadAllTask();

    void reloadSpecTask(String taskName);

    TaskDescriptor getSpecTaskDescriptor(String taskName);

    void start();
}
