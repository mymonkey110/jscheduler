package com.haoocai.jscheduler.core.scheduler.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.JschedulerConfig;
import com.haoocai.jscheduler.core.scheduler.SchedulerException;
import com.haoocai.jscheduler.core.scheduler.SchedulerService;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.TaskTracker;
import com.haoocai.jscheduler.core.task.TaskTrackerFactory;
import com.haoocai.jscheduler.core.task.impl.ZKTaskTracker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.haoocai.jscheduler.core.Constants.ENCODING;

/**
 * Scheduler Service with Zookeeper implementation
 *
 * @author Michael Jiang on 16/3/16.
 */
@Service
class ZKSchedulerService implements SchedulerService {
    @Resource
    private ZKManager zkManager;
    @Resource
    private JschedulerConfig jschedulerConfig;

    private static Logger LOG = LoggerFactory.getLogger(ZKSchedulerService.class);

    @PostConstruct
    public void init() {
        start();
    }

    @Override
    public void startTask(TaskID taskID) {
        LOG.trace("trying start task:{}.", taskID);
        String taskPath = taskID.identify();
        try {
            //reset start flag to zk
            zkManager.getClient().create().withMode(CreateMode.EPHEMERAL).forPath(taskPath + "/status", "RUNNING".getBytes());

            //start task tracker
            TaskTracker taskTracker = TaskTrackerFactory.getTaskTracker(taskID, zkManager.getClient());
            taskTracker.track();
            LOG.info("started task:{} successfully.", taskID);
        } catch (Exception e) {
            LOG.error("start task:{} error:{}.", taskID, e.getMessage(), e);
        }
    }

    @Override
    public void stopTask(TaskID taskID) {
        LOG.trace("trying to stop task:{}.", taskID);
        String taskPath = taskID.identify();
        try {
            TaskTracker taskTracker = TaskTrackerFactory.getTaskTracker(taskID, zkManager.getClient());
            if (taskTracker == null) {
                throw new RuntimeException("local has no task tracker.");
            }
            //cancel local task tracker
            taskTracker.untrack();
            //set untrack flag to zk
            zkManager.getClient().delete().forPath(taskPath + "/status");
        } catch (Exception e) {
            LOG.error("stop task:{} error:{}.", taskID, e.getMessage(), e);
        }
    }

    @Override
    public List<SchedulerUnit> getAllSchedulerUnits(TaskID taskID) {
        Preconditions.checkNotNull(taskID);

        List<SchedulerUnit> schedulerUnitList = new ArrayList<>();
        List<String> children;
        try {
            children = zkManager.getClient().getChildren().forPath(taskID.identify() + "/servers");
        } catch (Exception e) {
            throw new SchedulerException(e);
        }

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

    private class SchedulerStarter extends Thread {

        @Override
        public void run() {
            List<String> namespaces = jschedulerConfig.getNamespaces();
            LOG.info("found config namespace:{}.", namespaces);

            for (String namespace : namespaces) {
                try {
                    initNamespace(namespace);
                    List<String> apps = zkManager.getClient().getChildren().forPath("/" + namespace);
                    LOG.info("namespace:{} has apps:{}.", namespace, apps);
                    for (String app : apps) {
                        List<TaskDescriptor> taskDescriptorList = getTask(namespace, app);
                        for (TaskDescriptor taskDescriptor : taskDescriptorList) {
                            TaskTracker taskTracker = new ZKTaskTracker(zkManager.getClient(), taskDescriptor);
                            taskTracker.track();
                        }
                    }
                } catch (Exception e) {
                    LOG.error("init namespace:{} encounter error:{}.", namespace, e.getMessage(), e);
                    break;
                }
            }
        }

        /**
         * initialize namespace
         * <p>
         * Check the namespace node exist.
         * If the namespace doesn't exist,then create the node
         * </p>
         *
         * @param namespace namespace
         * @throws Exception
         */
        private void initNamespace(String namespace) throws Exception {
            String namespacePath = "/" + namespace;
            Stat stat = zkManager.getClient().checkExists().forPath(namespacePath);
            if (stat == null) {
                LOG.trace("namespace:{} doesn't exist,going to create node.", namespace);
                zkManager.getClient().create().withMode(CreateMode.EPHEMERAL).forPath("/namespacePath", new byte[0]);
            } else {
                LOG.info("namespace:{} exist.", namespace);
            }
        }
    }


    private List<TaskDescriptor> getTask(String namespace, String app) throws Exception {
        List<TaskDescriptor> taskDescriptors = new ArrayList<>();
        List<String> tasks;
        tasks = zkManager.getClient().getChildren().forPath("/" + namespace + "/" + app);
        LOG.info("namespace:{} app:{} tasks:{}.", namespace, app, tasks);
        for (String taskName : tasks) {
            byte[] data = zkManager.getClient().getData().forPath("/" + namespace + "/" + app + "/" + taskName + "/config/cronExpression");
            new TaskDescriptor(namespace, app, taskName, new String(data, ENCODING));
        }
        return taskDescriptors;
    }

}
