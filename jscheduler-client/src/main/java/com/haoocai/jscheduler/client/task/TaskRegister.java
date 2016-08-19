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

package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.shared.JvmIdentify;
import com.haoocai.jscheduler.client.util.StringUtils;
import com.haoocai.jscheduler.client.util.Validate;
import com.haoocai.jscheduler.client.zk.ZKClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * task register
 *
 * @author Michael Jiang on 16/3/31.
 */
public class TaskRegister {
    private static Logger LOG = LoggerFactory.getLogger(TaskRegister.class);
    private static TaskRegister instance;
    private final Map<String, TaskWatcher> taskWatcherMap = new ConcurrentHashMap<>();
    private final ZKClient zkClient;
    private final String pathPrefix;

    private TaskRegister(String namespace, String app, ZKClient zkClient) {
        this.zkClient = Validate.checkNotNull(zkClient);
        this.pathPrefix = "/" + namespace + "/" + app;
    }

    public static synchronized TaskRegister getInstance(String namespace, String app, ZKClient zkClient) {
        Validate.checkArguments(StringUtils.isNotBlank(namespace), "namespace is blank");
        Validate.checkArguments(StringUtils.isNotBlank(app), "app name is blank");

        if (instance == null) {
            instance = new TaskRegister(namespace, app, zkClient);
        }
        return instance;
    }

    public synchronized void register(Task task) {
        Validate.checkNotNull(task);

        if (taskWatcherMap.containsKey(task.name())) {
            throw new RuntimeException("name:" + task.name() + " already registered!");
        }
        if (!zkClient.checkNodeExist(pathPrefix + "/" + task.name())) {
            throw new RuntimeException("task " + task.name() + " not exist, please create task first.");
        }

        registerToZK(task);
        TaskWatcher taskWatcher = new TaskWatcher(zkClient, task, pathPrefix);
        taskWatcherMap.put(task.name(), taskWatcher);
        taskWatcher.start();
        LOG.info("registered task:{} successfully.", task.name());
    }

    private void registerToZK(Task task) {
        String serverNode = pathPrefix + "/" + task.name() + "/servers/" + JvmIdentify.id();
        if (zkClient.checkNodeExist(serverNode)) {
            zkClient.delete(serverNode);
        }
        zkClient.createEphemeralNode(serverNode, new byte[0]);
    }

}
