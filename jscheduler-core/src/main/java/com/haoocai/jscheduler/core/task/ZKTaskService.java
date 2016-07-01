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

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.algorithm.PickStrategy;
import com.haoocai.jscheduler.core.exception.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.CronExpressionException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.exception.TaskExistException;
import com.haoocai.jscheduler.core.register.TaskRegisterCenter;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Zookeeper Task Manager
 *
 * @author Michael Jiang on 16/3/16.
 */
@Service
public class ZKTaskService implements TaskService {
    private final ZKAccessor zkAccessor;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskService.class);

    @Autowired
    public ZKTaskService(ZKAccessor zkAccessor) {
        this.zkAccessor = zkAccessor;
    }

    @Override
    public void create(TaskID taskID, Cron cron)
            throws NamespaceNotExistException, AppNotFoundException, TaskExistException, CronExpressionException {
        if (!zkAccessor.checkNodeExist("/" + taskID.getNamespace())) {
            throw new NamespaceNotExistException();
        }
        if (!zkAccessor.checkNodeExist("/" + taskID.getNamespace() + "/" + taskID.getApp())) {
            throw new AppNotFoundException();
        }
        String taskPath = taskID.identify();
        if (zkAccessor.checkNodeExist(taskPath)) {
            throw new TaskExistException();
        }

        Task task = new Task(taskID, cron, zkAccessor);
        task.initNode();

        LOG.info("create task:{} with cron:{} success.", taskID.getName(), cron);
    }

    @Override
    public void delete(TaskID taskID) {
        LOG.info("deleting the task:{}.", taskID);
        zkAccessor.deleteRecursive(taskID.identify());
        LOG.info("deleted the task:{} node.", taskID.identify());
    }

    @Override
    public void load(TaskID taskID) {
        Task task = Task.load(zkAccessor, taskID);
        if (!TaskRegisterCenter.exist(taskID)) {
            TaskRegisterCenter.register(taskID, task);
        } else {
            throw new RuntimeException("already load task:" + taskID);
        }
    }

    @Override
    public Task find(TaskID taskID) {
        return Task.load(zkAccessor, taskID);
    }

    @Override
    public void updateConfig(TaskID taskID, Cron cron, PickStrategy pickStrategy) {
        Task task = find(taskID);
        task.changeConfig(cron, pickStrategy);
    }

    @Override
    public List<TaskDescriptor> getAppTasks(String namespace, String app) {
        Preconditions.checkArgument(StringUtils.isNotBlank(app), "app name can't be blank!");

        List<String> children = zkAccessor.getChildren("/" + namespace + "/" + app);

        List<TaskDescriptor> taskList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(children)) {
            for (String taskName : children) {
                Task task = Task.load(zkAccessor, new TaskID(namespace, app, taskName));
                taskList.add(task.getTaskDescriptor());
            }
        }

        return taskList;
    }

    @Override
    public TaskDescriptor getSpecTask(TaskID taskID) {
        return Task.load(zkAccessor, taskID).getTaskDescriptor();
    }
}
