package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.task.TaskDescriptor;

import java.util.List;

/**
 * @author Michael Jiang on 16/3/16.
 */
public interface SchedulerService {

    void reloadSpecTask(TaskDescriptor taskDescriptor);

    List<SchedulerUnit> getAllSchedulerUnits(TaskDescriptor taskDescriptor);

    void start();
}
