/*
 * Copyright 2016  Michael Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.register.TaskRegisterCenter;
import com.haoocai.jscheduler.core.shared.JschedulerConfig;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.ZKTaskService;
import com.haoocai.jscheduler.core.tracker.TaskTracker;
import com.haoocai.jscheduler.core.tracker.TaskTrackerFactory;
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

    private final ZKTaskService zkTaskManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKSchedulerService.class);

    @Autowired
    public ZKSchedulerService(ZKAccessor zkAccessor, JschedulerConfig jschedulerConfig, ZKTaskService zkTaskManager) {
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
        try {
            Task task = TaskRegisterCenter.task(taskID);
            if (task == null) {
                throw new Exception("task not found");
            }
            task.start();

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
                        startTask(taskID);
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
