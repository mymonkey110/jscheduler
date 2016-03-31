package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;

/**
 * Job invoker
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public interface TaskInvoker {

    /**
     * invoke task to scheduler unit.
     *
     * @param taskDescriptor task descriptor
     * @param schedulerUnit  scheduler unit
     */
    void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit);
}
