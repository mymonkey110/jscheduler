package com.haoocai.jscheduler.core.trigger;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;

/**
 * @author Michael Jiang on 16/3/16.
 */
public interface Picker {

    SchedulerUnit assign() throws Exception;
}
