package com.haoocai.jscheduler.core.task.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskException;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.TaskManager;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.haoocai.jscheduler.core.Constants.ENCODING;
import static com.haoocai.jscheduler.core.ErrorCode.*;

/**
 * Zookeeper Task Manager
 *
 * @author Michael Jiang on 16/3/16.
 */
@Service
public class ZKTaskManager implements TaskManager {
    private final ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskManager.class);

    @Autowired
    public ZKTaskManager(ZKManager zkManager) {
        this.zkManager = zkManager;
    }

    @Override
    public void create(String namespace, String app, String taskName, String cronExpression) throws TaskException {
        if (!zkManager.checkNodeExist("/" + namespace)) {
            throw new TaskException(NAMESPACE_NOT_FOUND, "namespace not found");
        }
        if (!zkManager.checkNodeExist("/" + namespace + "/" + app)) {
            throw new TaskException(APP_NOT_FOUND, "app not found");
        }
        String taskPath = "/" + namespace + "/" + app + "/" + taskName;
        if (zkManager.checkNodeExist(taskPath)) {
            throw new TaskException(TASK_ALREADY_EXIST, "task:" + taskName + " already exist.");
        }

        zkManager.create(taskPath + "/config/cronExpression", cronExpression.getBytes());
    }

    @Override
    public void delete(TaskID taskID) {
        LOG.trace("deleting the task:{}.", taskID);
        zkManager.delete(taskID.identify());
        LOG.info("deleted the task:{} node.", taskID.identify());
    }

    @Override
    public List<TaskDescriptor> getAppTasks(String namespace, String app) {
        Preconditions.checkArgument(StringUtils.isNotBlank(app), "app name can't be blank!");

        List<String> children;
        try {
            children = zkManager.getClient().getChildren().forPath("/" + namespace + "/" + app);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
            String cronExpression = new String(zkManager.getClient().getData().forPath(taskPath + "/config/cronExpression"), ENCODING);
            return new TaskDescriptor(taskID.getNamespace(), taskID.getApp(), taskID.getName(), cronExpression);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
