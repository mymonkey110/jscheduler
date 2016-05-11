package com.haoocai.jscheduler.core.scheduler;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskTracker;
import com.haoocai.jscheduler.core.task.TaskTrackerFactory;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Scheduler Service with Zookeeper implementation
 *
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
    public void startTask(TaskDescriptor taskDescriptor) {
        LOG.trace("trying start task:{}.", taskDescriptor);

        String taskPath = taskDescriptor.taskPath();
        //reset start flag to zk
        PersistentNode persistentNode = new PersistentNode(zkManager.getClient(), CreateMode.EPHEMERAL, true, taskPath + "/status", "RUNNING".getBytes());
        persistentNode.start();
        //start task tracker
        TaskTracker taskTracker = TaskTrackerFactory.getTaskTracker(taskDescriptor, zkManager.getClient());
        taskTracker.track();

        LOG.info("started task:{} successfully.", taskDescriptor);
    }

    @Override
    public void stopTask(TaskDescriptor taskDescriptor) {
        String taskPath = taskDescriptor.taskPath();
        TaskTracker taskTracker = TaskTrackerFactory.getTaskTracker(taskDescriptor, zkManager.getClient());
        if (taskTracker == null) {
            throw new RuntimeException("local has no task tracker.");
        }
        //cancel local task tracker
        taskTracker.untrack();
        //set untrack flag to zk
        try {
            zkManager.getClient().delete().forPath(taskPath + "/status");
        } catch (Exception e) {
            LOG.error("delete node:{} error:{}", taskPath + "/status", e.getMessage(), e);
        }
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

    private void start() {
        new SchedulerStarter().start();
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

    private class SchedulerStarter extends Thread {

        @Override
        public void run() {
            List<TaskDescriptor> taskDescriptorList = getAllTasks();
            for (TaskDescriptor taskDescriptor : taskDescriptorList) {
                startTask(taskDescriptor);
            }
        }
    }

}
