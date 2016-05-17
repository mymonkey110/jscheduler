package com.haoocai.jscheduler.core.task;

import java.util.List;

/**
 * Task Manager
 * <p>
 * TaskManager include the basic task management.
 * </p>
 *
 * @author Michael Jiang on 16/3/16.
 */
public interface TaskManager {

    void create(String namespace, String app, String taskName, String cronExpression) throws TaskException;

    void delete(TaskID taskID);

    List<TaskDescriptor> getAppTasks(String namespace, String app);

    TaskDescriptor getSpecTaskDescriptor(TaskID taskID);
}
