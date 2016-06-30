package com.haoocai.jscheduler.client.task;

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

    private static Logger LOG = LoggerFactory.getLogger(TaskRegister.class);

    TaskRegister(String namespace, String app, ZKClient zkClient) {
        this.namespace = Validate.checkNotNull(namespace);
        this.app = Validate.checkNotNull(app);
        this.zkClient = Validate.checkNotNull(zkClient);
    }

    public synchronized void register(SimpleTask simpleTask) {
        Validate.checkNotNull(simpleTask);

        if (taskWatcherMap.containsKey(simpleTask.name())) {
            throw new RuntimeException("name:" + simpleTask.name() + " already registered!");
        }

        initTask(simpleTask);

        TaskWatcher taskWatcher = new TaskWatcher(zkClient, simpleTask);
        taskWatcherMap.put(simpleTask.name(), taskWatcher);
        taskWatcher.start();
        LOG.info("registered task:{} successfully.", simpleTask.name());
    }

    private void initTask(SimpleTask simpleTask) {
        simpleTask.setNamespace(namespace);
        simpleTask.setApp(app);
    }

}
