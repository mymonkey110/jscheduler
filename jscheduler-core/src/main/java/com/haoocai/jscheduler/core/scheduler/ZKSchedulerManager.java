package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.JschedulerConfig;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.springframework.stereotype.Service;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class ZKSchedulerManager implements SchedulerManager {

    private ZKManager zkManager;

    //todo init zkManager
    public ZKSchedulerManager(JschedulerConfig config) {

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
    public TaskDescriptor getSpecTaskDescriptor(String taskName) {
        return null;
    }

    @Override
    public void start() {

    }
}
