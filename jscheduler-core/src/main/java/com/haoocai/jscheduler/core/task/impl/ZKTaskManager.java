package com.haoocai.jscheduler.core.task.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskException;
import com.haoocai.jscheduler.core.task.TaskManager;
import com.haoocai.jscheduler.core.zk.ZKException;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.haoocai.jscheduler.core.Constants.PATH_SEP;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class ZKTaskManager implements TaskManager {
    @Resource
    private ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskManager.class);

    @Override
    public void register(TaskDescriptor taskDescriptor) throws TaskException {
        Preconditions.checkNotNull(taskDescriptor);

        String path = taskDescriptor.getApp() + PATH_SEP + taskDescriptor.getName();

        try {
            zkManager.createNode(path, taskDescriptor);
        } catch (ZKException e) {
            LOG.error("register task:{} for app:{} error:{}.", taskDescriptor.getName(), taskDescriptor.getApp(), e.getMessage(), e);
            throw new TaskException(e);
        }
    }

    @Override
    public void unregister(TaskDescriptor taskDescriptor) {
        Preconditions.checkNotNull(taskDescriptor);

        String path = taskDescriptor.getApp() + PATH_SEP + taskDescriptor.getName();

        zkManager.deleteNode(path);
    }

    @Override
    public List<TaskDescriptor> getAppTasks(String app) {
        Preconditions.checkArgument(StringUtils.isNotBlank(app), "app name can't be blank!");

        List<String> children = zkManager.getNodeChildren(app);

        List<TaskDescriptor> taskList = new ArrayList<>();
        for (String taskName : children) {
            taskList.add(getSpecTaskDescriptor(app, taskName));
        }

        return taskList;
    }

    @Override
    public TaskDescriptor getSpecTaskDescriptor(String app, String taskName) {
        return null;
    }
}
