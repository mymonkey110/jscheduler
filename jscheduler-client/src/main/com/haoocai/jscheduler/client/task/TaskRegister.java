package com.haoocai.jscheduler.client.task;

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
    private final Map<String, TaskWatcher> taskWatcherMap = new ConcurrentHashMap<>();

    private final String app;
    private final ZKClient zkClient;
    private static TaskRegister instance;

    private static Logger LOG = LoggerFactory.getLogger(TaskRegister.class);

    TaskRegister(ZKClient zkClient, String app) {
        this.zkClient = zkClient;
        this.app = app;
    }

    /*public synchronized static TaskRegister getInstance(String app) {
        Validate.checkArguments(StringUtils.isNotBlank(app), "app name can't be blank");

        if (instance == null) {
            instance = new TaskRegister(app);
        }
        return instance;
    }*/

    public synchronized <T extends Job> void register(T job) {
        Validate.checkNotNull(job);

        if (taskWatcherMap.containsKey(job.task())) {
            throw new RuntimeException("task:" + job.task() + " has already registered!");
        }

        taskWatcherMap.put(job.task(), new TaskWatcher(zkClient, app, job));
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
     * start all the registered task watcher
     */
    public synchronized void init() {
        LOG.debug("starting registered task watcher...");

        for (String task : taskWatcherMap.keySet()) {
            LOG.debug("start task:{} watcher.", task);
            TaskWatcher taskWatcher = taskWatcherMap.get(task);
            taskWatcher.start();
        }
    }

}
