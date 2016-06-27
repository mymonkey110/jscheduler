package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.exception.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.CronExpressionException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.exception.TaskExistException;

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
     * @param taskID         taskID
     * @param cronExpression task cronExpression
     * @throws NamespaceNotExistException namespace not found
     * @throws AppNotFoundException       task exception
     */
    void create(TaskID taskID, String cronExpression) throws NamespaceNotExistException,
            AppNotFoundException, TaskExistException, CronExpressionException;

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
    Task getSpecTask(TaskID taskID);
}
