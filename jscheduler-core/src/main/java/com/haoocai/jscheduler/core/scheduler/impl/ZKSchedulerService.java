package com.haoocai.jscheduler.core.scheduler.impl;

import com.haoocai.jscheduler.core.shared.JschedulerConfig;
import com.haoocai.jscheduler.core.register.TaskRegisterCenter;
import com.haoocai.jscheduler.core.scheduler.SchedulerService;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.ZKTaskManager;
import com.haoocai.jscheduler.core.tracker.TaskTracker;
import com.haoocai.jscheduler.core.tracker.TaskTrackerFactory;
import com.haoocai.jscheduler.core.tracker.ZKTaskTracker;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Scheduler Service with Zookeeper implementation
 *
 * @author Michael Jiang on 16/3/16.
 */
@Service
class ZKSchedulerService implements SchedulerService {
    private final ZKAccessor zkAccessor;
    private final JschedulerConfig jschedulerConfig;

    private final ZKTaskManager zkTaskManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKSchedulerService.class);

    @Autowired
    public ZKSchedulerService(ZKAccessor zkAccessor, JschedulerConfig jschedulerConfig, ZKTaskManager zkTaskManager) {
        this.zkAccessor = zkAccessor;
        this.jschedulerConfig = jschedulerConfig;
        this.zkTaskManager = zkTaskManager;
    }

    @PostConstruct
    public void init() {
        new SchedulerStarter().start();
    }

    @Override
    public void startTask(TaskID taskID) {
        LOG.info("trying start task:{}.", taskID);
        String taskPath = taskID.identify();
        try {
            //reset start flag to zk
            zkAccessor.createEphemeralNode(taskPath + "/status", "RUNNING".getBytes());

            //start task tracker
            TaskTracker taskTracker = TaskTrackerFactory.getTaskTracker(taskID, zkAccessor);
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
            TaskTracker taskTracker = TaskTrackerFactory.getTaskTracker(taskID, zkAccessor);
            if (taskTracker == null) {
                throw new RuntimeException("local has no task tracker.");
            }
            //cancel local task tracker
            taskTracker.untrack();
            //set untrack flag to zk
            zkAccessor.getClient().delete().forPath(taskPath + "/status");
        } catch (Exception e) {
            LOG.error("stop task:{} error:{}.", taskID, e.getMessage(), e);
        }
    }

    private class SchedulerStarter extends Thread {

        @Override
        public void run() {
            List<String> namespaces = jschedulerConfig.getNamespaces();
            LOG.info("found config namespace:{}.", namespaces);
            for (String namespace : namespaces) {
                initNamespace(namespace);
                List<String> apps = zkAccessor.getChildren("/" + namespace);
                LOG.info("namespace:{} has apps:{}.", namespace, apps);
                for (String app : apps) {
                    List<String> taskNames = zkAccessor.getChildren("/" + namespace + "/" + app);
                    for (String name : taskNames) {
                        TaskID taskID = new TaskID(namespace, app, name);
                        zkTaskManager.load(taskID);
                        Task task = TaskRegisterCenter.task(taskID);
                        TaskTracker taskTracker = new ZKTaskTracker(zkAccessor, task);
                        taskTracker.track();
                    }
                }
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
     */
    private void initNamespace(String namespace) {
        String namespacePath = "/" + namespace;
        if (!zkAccessor.checkNodeExist(namespacePath)) {
            LOG.info("namespace:{} doesn't exist,going to create node.", namespace);
            zkAccessor.create(namespacePath, new byte[0]);
        } else {
            LOG.info("namespace:{} exist.", namespace);
        }
    }

}
