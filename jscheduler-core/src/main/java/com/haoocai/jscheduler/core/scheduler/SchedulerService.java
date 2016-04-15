package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.task.TaskDescriptor;

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

    void reloadSpecTask(TaskDescriptor taskDescriptor);

    List<SchedulerUnit> getAllSchedulerUnits(TaskDescriptor taskDescriptor);

    void start();
}
