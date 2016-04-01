package com.haoocai.jscheduler.core.trigger;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;

/**
 * @author Michael Jiang on 16/3/16.
 */
public interface Picker {
    SchedulerUnit assign(String taskName);
}
