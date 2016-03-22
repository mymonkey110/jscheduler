package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.client.SchedulerContext;
import com.haoocai.jscheduler.core.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.haoocai.jscheduler.core.Constants.ID;
import static com.haoocai.jscheduler.core.Constants.PATH_SEP;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class TaskInvokerImpl implements TaskInvoker {
    private String rootPath;
    private ZooKeeper zooKeeper;

    private static Logger LOG = LoggerFactory.getLogger(TaskInvokerImpl.class);

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit) {
        String invokePath = rootPath + PATH_SEP + ID + PATH_SEP + taskDescriptor.getApp() + PATH_SEP + schedulerUnit.identify();


        try {
            Stat stat = zooKeeper.exists(invokePath, false);
        } catch (KeeperException e) {
            LOG.error("try to invoke task:{} on {} encounter error,code:{}.", taskDescriptor.getName(), schedulerUnit.identify(), e.code());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //fixme
    private SchedulerContext assembleScheContext(TaskDescriptor taskDescriptor) {
        return null;
    }
}
