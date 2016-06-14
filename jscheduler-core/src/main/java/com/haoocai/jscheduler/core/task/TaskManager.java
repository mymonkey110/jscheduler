package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.app.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;

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
    /**
     * create task
     *
     * @param namespace      namespace
     * @param app            app name the task belong to
     * @param taskName       task name
     * @param cronExpression task cronExpression
     * @throws AppNotFoundException task exception
     */
    void create(String namespace, String app, String taskName, String cronExpression) throws NamespaceNotExistException, AppNotFoundException, TaskExistException, CronExpressionException;

    /**
     * delete task by task id
     *
     * @param taskID task id
     */
    void delete(TaskID taskID);

    /**
     * get app all the tasks
     *
     * @param namespace namespace
     * @param app       app
     * @return all the task descriptors
     */
    List<TaskDescriptor> getAppTasks(String namespace, String app);

    /**
     * get the specified task descriptor by task id
     *
     * @param taskID task id
     * @return task descriptor
     */
    TaskDescriptor getSpecTaskDescriptor(TaskID taskID);
}
