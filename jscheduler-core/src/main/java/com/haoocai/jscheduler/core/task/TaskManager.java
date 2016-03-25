package com.haoocai.jscheduler.core.task;

import java.util.List;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public interface TaskManager {

    void register(TaskDescriptor taskDescriptor);

    void unregister(TaskDescriptor taskDescriptor);

    List<TaskDescriptor> getAllTaskDescriptor();

    TaskDescriptor getSpecTaskDescriptor(String app, String taskName);
}
