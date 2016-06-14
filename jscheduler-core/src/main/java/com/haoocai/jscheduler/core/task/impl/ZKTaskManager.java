package com.haoocai.jscheduler.core.task.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.app.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.task.*;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
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
    private final ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskManager.class);

    private final static Charset CHARSET = Charset.forName("UTF-8");

    @Autowired
    public ZKTaskManager(ZKManager zkManager) {
        this.zkManager = zkManager;
    }

    @Override
    public void create(String namespace, String app, String taskName, String cronExpression)
            throws NamespaceNotExistException, AppNotFoundException, TaskExistException, CronExpressionException {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new CronExpressionException();
        }
        if (!zkManager.checkNodeExist("/" + namespace)) {
            throw new NamespaceNotExistException();
        }
        if (!zkManager.checkNodeExist("/" + namespace + "/" + app)) {
            throw new AppNotFoundException();
        }
        String taskPath = "/" + namespace + "/" + app + "/" + taskName;
        if (zkManager.checkNodeExist(taskPath)) {
            throw new TaskExistException();
        }

        zkManager.mkdirAndCreate(taskPath + "/config", "cron", cronExpression.getBytes(CHARSET));
        LOG.info("create task:{} with cron:{} success.", taskName, cronExpression);
    }

    @Override
    public void delete(TaskID taskID) {
        LOG.info("deleting the task:{}.", taskID);
        zkManager.delete(taskID.identify());
        LOG.info("deleted the task:{} node.", taskID.identify());
    }

    @Override
    public List<TaskDescriptor> getAppTasks(String namespace, String app) {
        Preconditions.checkArgument(StringUtils.isNotBlank(app), "app name can't be blank!");

        List<String> children = zkManager.getChildren("/" + namespace + "/" + app);

        List<TaskDescriptor> taskList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(children)) {
            for (String taskName : children) {
                taskList.add(getSpecTaskDescriptor(new TaskID(namespace, app, taskName)));
            }
        }

        return taskList;
    }

    @Override
    public TaskDescriptor getSpecTaskDescriptor(TaskID taskID) {
        String taskPath = taskID.identify();

        try {
            String cronExpression = new String(zkManager.getClient().getData().forPath(taskPath + "/config/cronExpression"), UTF8_CHARSET);
            return new TaskDescriptor(taskID.getNamespace(), taskID.getApp(), taskID.getName(), cronExpression);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
