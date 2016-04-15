package com.haoocai.jscheduler.core.scheduler;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskTracker;
import com.haoocai.jscheduler.core.task.impl.ZKTaskTracker;
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
class ZKSchedulerService implements SchedulerService {

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


    private List<TaskDescriptor> getAllTasks() {
        List<TaskDescriptor> taskDescriptors = new ArrayList<>();
        List<String> apps = zkManager.getAbsNodeChildrenWithRoot(null);
        LOG.info("found apps:{}.", apps);
        for (String app : apps) {
            List<String> tasks = zkManager.getAbsNodeChildrenWithRoot(app);
            LOG.info("found app:{} tasks:{}.", app, tasks);
            for (String task : tasks) {
                TaskDescriptor taskDescriptor = zkManager.getNodeData(app + "/" + task, TaskDescriptor.class);
                taskDescriptors.add(taskDescriptor);
            }
        }

        return taskDescriptors;
    }

    class SchedulerStarter extends Thread {

        @Override
        public void run() {
            List<TaskDescriptor> taskDescriptorList = getAllTasks();
            for (TaskDescriptor taskDescriptor : taskDescriptorList) {
                TaskTracker taskTracker = new ZKTaskTracker(zkManager, taskDescriptor);
                taskTracker.track();
            }
        }
    }

}
