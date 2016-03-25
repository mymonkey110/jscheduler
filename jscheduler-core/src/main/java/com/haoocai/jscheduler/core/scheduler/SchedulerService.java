package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;

import java.util.List;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public interface SchedulerService {

    void reloadAllTask();

    void reloadSpecTask(TaskDescriptor taskDescriptor);

    List<SchedulerUnit> getAllSchedulerUnits(TaskDescriptor taskDescriptor);

    TaskDescriptor getSpecTaskDescriptor(TaskDescriptor taskDescriptor);
}
