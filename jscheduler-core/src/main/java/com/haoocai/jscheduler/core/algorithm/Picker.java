package com.haoocai.jscheduler.core.algorithm;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;

/**
 * Scheduler Unit Picker Algorithm
 *
 * @author Michael Jiang on 16/3/16.
 */
public interface Picker {

    SchedulerUnit assign() throws Exception;
}
