package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskManager;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class ZKTaskManager implements TaskManager {
    @Resource
    private ZKManager zkManager;

    @Override
    public void register(TaskDescriptor taskDescriptor) {
        
    }

    @Override
    public void unregister(TaskDescriptor taskDescriptor) {

    }

    @Override
    public List<TaskDescriptor> getAllTaskDescriptor() {
        return null;
    }

    @Override
    public TaskDescriptor getSpecTaskDescriptor(String app, String taskName) {
        return null;
    }
}
