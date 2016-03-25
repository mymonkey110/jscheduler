package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class ZKSchedulerService implements SchedulerService {

    @Resource
    private ZKManager zkManager;

    @PostConstruct
    public void init() {

    }

    @Override
    public void reloadAllTask() {

    }

    @Override
    public void reloadSpecTask(TaskDescriptor taskDescriptor) {

    }

    @Override
    public List<SchedulerUnit> getAllSchedulerUnits(TaskDescriptor taskDescriptor) {
        return null;
    }

    @Override
    public TaskDescriptor getSpecTaskDescriptor(TaskDescriptor taskDescriptor) {
        return null;
    }
}
