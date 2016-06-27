package com.haoocai.jscheduler.core.task.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.exception.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.CronExpressionException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.exception.TaskExistException;
import com.haoocai.jscheduler.core.task.*;
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
public class ZKTaskManager implements TaskManager {
    private final ZKAccessor zkAccessor;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskManager.class);

    @Autowired
    public ZKTaskManager(ZKAccessor zkAccessor) {
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
        task.init();

        LOG.info("create task:{} with cron:{} success.", taskID.getName(), cron);
    }

    @Override
    public void delete(TaskID taskID) {
        LOG.info("deleting the task:{}.", taskID);
        zkAccessor.deleteRecursive(taskID.identify());
        LOG.info("deleted the task:{} node.", taskID.identify());
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
