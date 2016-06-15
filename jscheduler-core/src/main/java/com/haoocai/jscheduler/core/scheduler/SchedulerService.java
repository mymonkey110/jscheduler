package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.task.TaskID;

import java.util.List;

/**
 * Scheduler Service
 * <p>
 * Scheduler service is responsible for scheduling the time task of all system.
 * When it started, it should watch every task running, and invoke the task by reserved the
 * pick strategy.
 * </p>
 *
 * @author Michael Jiang on 16/3/16.
 */
public interface SchedulerService {
    /**
     * Start the specified task
     *
     * @param taskID task identify
     */
    void startTask(TaskID taskID);

    /**
     * stop the specified task
     *
     * @param taskID task identify
     */
    void stopTask(TaskID taskID);

    /**
     * get all available schedule unit by specifiy task
     *
     * @param taskID task identify
     * @return scheduler units
     */
    List<SchedulerUnit> getAllSchedulerUnits(TaskID taskID);
}
