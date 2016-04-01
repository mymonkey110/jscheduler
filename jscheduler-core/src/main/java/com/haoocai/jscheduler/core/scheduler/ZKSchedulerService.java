package com.haoocai.jscheduler.core.scheduler;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Jiang on 16/3/16.
 */
@Service
public class ZKSchedulerService implements SchedulerService {

    @Resource
    private ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKSchedulerService.class);

    @PostConstruct
    public void init() {
        start();
    }

    @Override
    public void reloadSpecTask(TaskDescriptor taskDescriptor) {

    }

    @Override
    public List<SchedulerUnit> getAllSchedulerUnits(TaskDescriptor taskDescriptor) {
        Preconditions.checkNotNull(taskDescriptor);

        List<SchedulerUnit> schedulerUnitList = new ArrayList<>();
        List<String> children = zkManager.getNodeChildren(taskDescriptor.getApp() + "/" + taskDescriptor.getName());

        for (String child : children) {
            String[] addr = child.split(":");
            SchedulerUnit schedulerUnit = new SchedulerUnit(addr[0], Integer.parseInt(addr[1]));
            schedulerUnitList.add(schedulerUnit);
        }

        return schedulerUnitList;
    }

    @Override
    public void start() {

    }

    class SchedulerStarter extends Thread {

        @Override
        public void run() {
            super.run();
        }
    }

}
