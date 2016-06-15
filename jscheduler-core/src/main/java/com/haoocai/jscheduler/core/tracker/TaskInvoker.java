package com.haoocai.jscheduler.core.tracker;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;

/**
 * Task invoker
 * @author Michael Jiang on 16/3/16.
 */
public interface TaskInvoker {

    /**
     * invoke name to scheduler unit.
     *
     * @param taskDescriptor name descriptor
     * @param schedulerUnit  scheduler unit
     */
    void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit);
}
