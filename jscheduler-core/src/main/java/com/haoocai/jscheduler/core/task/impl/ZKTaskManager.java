package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskManager;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public class ZKTaskManager implements TaskManager {
    private final ZooKeeper zooKeeper;
    private final String rootPath;

    public ZKTaskManager(ZooKeeper zooKeeper, String rootPath) {
        Validate.notNull(zooKeeper, "zookeeper is null");
        Validate.isTrue(StringUtils.isNotBlank(rootPath), "jscheduler root path can't be blank");

        this.zooKeeper = zooKeeper;
        this.rootPath = rootPath;
    }

    @Override
    public List<TaskDescriptor> getAllTaskDescriptor() {
        return null;
    }

    @Override
    public TaskDescriptor getSpecTaskDescriptor(String taskName) {
        return null;
    }
}
