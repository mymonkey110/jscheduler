package com.haoocai.jscheduler.core.task.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.exception.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.CronExpressionException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.exception.TaskExistException;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.TaskManager;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.haoocai.jscheduler.core.Constants.UTF8_CHARSET;

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
    public void create(TaskID taskID, String cronExpression)
            throws NamespaceNotExistException, AppNotFoundException, TaskExistException, CronExpressionException {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new CronExpressionException();
        }
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

        zkAccessor.mkdirAndCreate(taskPath + "/config", "cron", cronExpression.getBytes(UTF8_CHARSET));
        LOG.info("create task:{} with cron:{} success.", taskID.getName(), cronExpression);
    }

    @Override
    public void delete(TaskID taskID) {
        LOG.info("deleting the task:{}.", taskID);
        zkAccessor.delete(taskID.identify());
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
