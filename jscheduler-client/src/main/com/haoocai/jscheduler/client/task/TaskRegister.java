package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.util.Validate;
import com.haoocai.jscheduler.client.zk.ZKClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * name register
 *
 * @author Michael Jiang on 16/3/31.
 */
public class TaskRegister {
    private final Map<String, TaskWatcher> taskWatcherMap = new ConcurrentHashMap<>();

    private final String app;
    private final ZKClient zkClient;

    private static Logger LOG = LoggerFactory.getLogger(TaskRegister.class);

    TaskRegister(ZKClient zkClient, String app) {
        this.zkClient = zkClient;
        this.app = app;
    }

    public synchronized <T extends Task> void register(T job) {
        Validate.checkNotNull(job);

        if (taskWatcherMap.containsKey(job.name())) {
            throw new RuntimeException("name:" + job.name() + " has already registered!");
        }

        taskWatcherMap.put(job.name(), new TaskWatcher(zkClient, app, job));
    }

    /**
     * Encapsulate register runtime error
     *
     * @author Michael Jiang on 16/4/1.
     */
    public static class RegisterException extends RuntimeException {

        private static final long serialVersionUID = 2466552277133591930L;

        public RegisterException(String message) {
            super(message);
        }
    }

    /**
     * start all the registered name watcher
     */
    public synchronized void init() {
        LOG.debug("starting registered name watcher...");

        for (String task : taskWatcherMap.keySet()) {
            LOG.debug("start name:{} watcher.", task);
            TaskWatcher taskWatcher = taskWatcherMap.get(task);
            taskWatcher.start();
        }
    }

}
