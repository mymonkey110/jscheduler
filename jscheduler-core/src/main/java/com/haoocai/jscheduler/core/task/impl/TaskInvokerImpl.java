package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.client.SchedulerContext;
import com.haoocai.jscheduler.core.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.haoocai.jscheduler.core.Constants.ID;
import static com.haoocai.jscheduler.core.Constants.PATH_SEP;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class TaskInvokerImpl implements TaskInvoker {
    @Resource
    private ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(TaskInvokerImpl.class);

    @Override
    public void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit) {

        String invokePath = zkManager.getRootPath() + PATH_SEP + ID + PATH_SEP + taskDescriptor.getApp() + PATH_SEP + schedulerUnit.identify();

        SchedulerContext context = new SchedulerContext(taskDescriptor.getName(), System.currentTimeMillis(), taskDescriptor.getExtraParams());

        try {
            Stat stat = zkManager.getZooKeeper().exists(invokePath, false);


        } catch (Exception e) {
            LOG.error("try to invoke task:{} on {} encounter error,code:{}.", taskDescriptor.getName(), schedulerUnit.identify(), e.getMessage());
        }
    }

}
