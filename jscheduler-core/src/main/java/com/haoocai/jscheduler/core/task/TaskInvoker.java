package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.SchedulerUnit;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public interface TaskInvoker {

    void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit);
}
