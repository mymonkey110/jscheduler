package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.util.SerializationUtils;
import com.haoocai.jscheduler.client.zk.ZKClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.haoocai.jscheduler.client.util.Validate.checkNotNull;

/**
 * Task Watcher
 *
 * @author Michael Jiang on 16/3/31.
 */
class TaskWatcher implements Watcher {
    private final ZKClient zkClient;
    private final SimpleTask task;

    private static Logger LOG = LoggerFactory.getLogger(TaskWatcher.class);

    TaskWatcher(ZKClient zkClient, SimpleTask task) {
        this.zkClient = checkNotNull(zkClient);
        this.task = checkNotNull(task);
    }

    public void start() {
        zkClient.addListener(task.taskPath(), this);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDataChanged) {
            byte[] data = zkClient.getData(event.getPath());
            try {
                SchedulerContext schedulerContext = SerializationUtils.deserialize(data);
                task.run(schedulerContext);
            } catch (Exception e) {
                LOG.error("process task error:{}.", e.getMessage(), e);
            }
        } else {
            LOG.error("unexpected event type:{}.", event.getType());
        }
    }
}
