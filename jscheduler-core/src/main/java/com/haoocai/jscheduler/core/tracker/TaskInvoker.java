package com.haoocai.jscheduler.core.tracker;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskID;

/**
 * Task invoker
 *
 * @author Michael Jiang on 16/3/16.
 */
public interface TaskInvoker {

    /**
     * invoke name to scheduler unit.
     *
     * @param taskID        task ID
     * @param schedulerUnit scheduler unit
     */
    void invoke(TaskID taskID, SchedulerUnit schedulerUnit);
}
