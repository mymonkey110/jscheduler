package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class ZKSchedulerManager implements SchedulerManager {

    private ZooKeeper zooKeeper;

    public void setZooKeeper(ZKManager zkManager) throws Exception {
        Objects.requireNonNull(zooKeeper);
        this.zooKeeper = zkManager.getZooKeeper();
    }

    @PostConstruct
    public void init() {

    }

    @Override
    public void register() {

    }

    @Override
    public void reloadAllTask() {

    }

    @Override
    public void reloadSpecTask(String taskName) {

    }

    @Override
    public List<SchedulerUnit> getAllSchedulerUnits(TaskDescriptor taskDescriptor) {
        return null;
    }

    @Override
    public TaskDescriptor getSpecTaskDescriptor(TaskDescriptor taskDescriptor) {
        return null;
    }

    @Override
    public void start() {

    }
}
