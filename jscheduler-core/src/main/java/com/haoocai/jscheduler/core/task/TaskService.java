/*
 * Copyright 2016  Michael Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.algorithm.PickStrategy;
import com.haoocai.jscheduler.core.exception.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.CronExpressionException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.exception.TaskExistException;

import java.util.List;

/**
 * Task Manager
 * <p>
 * TaskService include the basic task management.
 * </p>
 *
 * @author Michael Jiang on 16/3/16.
 */
public interface TaskService {
    /**
     * create task
     *
     * @param taskID taskID
     * @param cron   task cronExpression
     * @throws NamespaceNotExistException namespace not found
     * @throws AppNotFoundException       task exception
     */
    void create(TaskID taskID, Cron cron) throws NamespaceNotExistException,
            AppNotFoundException, TaskExistException, CronExpressionException;

    /**
     * delete task by task id
     *
     * @param taskID task id
     */
    void delete(TaskID taskID);

    /**
     * load task to local
     *
     * @param taskID task id
     */
    void load(TaskID taskID);

    /**
     * find task id
     *
     * @param taskID task id
     * @return task
     */
    Task find(TaskID taskID);


    void updateConfig(TaskID taskID, Cron cron, PickStrategy pickStrategy);

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
    TaskDescriptor getSpecTask(TaskID taskID);
}
