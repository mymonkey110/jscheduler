package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.shared.JvmIdentify;
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
class TaskRegister {
    private final Map<String, TaskWatcher> taskWatcherMap = new ConcurrentHashMap<>();

    private final String namespace;
    private final String app;
    private final ZKClient zkClient;
    private final String pathPrefix;

    private static Logger LOG = LoggerFactory.getLogger(TaskRegister.class);

    TaskRegister(String namespace, String app, ZKClient zkClient) {
        this.namespace = Validate.checkNotNull(namespace);
        this.app = Validate.checkNotNull(app);
        this.zkClient = Validate.checkNotNull(zkClient);
        this.pathPrefix = "/" + namespace + "/" + app;
    }

    public synchronized void register(Task task) {
        Validate.checkNotNull(task);

        if (taskWatcherMap.containsKey(task.name())) {
            throw new RuntimeException("name:" + task.name() + " already registered!");
        }

        registerToZK(task);
        TaskWatcher taskWatcher = new TaskWatcher(zkClient, task, pathPrefix);
        taskWatcherMap.put(task.name(), taskWatcher);
        taskWatcher.start();
        LOG.info("registered task:{} successfully.", task.name());
    }

    private void registerToZK(Task task) {
        String serverNode = pathPrefix + "/" + task.name() + "/servers/" + JvmIdentify.id();

        zkClient.createEphemeralNode(serverNode, new byte[0]);
    }

}
