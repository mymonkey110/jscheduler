package com.haoocai.jscheduler.core.task;

import java.util.List;

/**
 * @author Michael Jiang on 16/3/16.
 */
public interface TaskManager {

    void create(String namespace,String app,String taskName,String cronExpression);

    void delete(TaskDescriptor taskDescriptor);

    void register(TaskDescriptor taskDescriptor) throws TaskException;

    void unregister(TaskDescriptor taskDescriptor);

    List<TaskDescriptor> getAppTasks(String app);

    TaskDescriptor getSpecTaskDescriptor(String app, String taskName);
}
