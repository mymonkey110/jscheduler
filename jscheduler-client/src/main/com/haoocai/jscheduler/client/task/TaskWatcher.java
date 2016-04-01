package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.SchedulerContext;
import com.haoocai.jscheduler.client.util.StringUtils;
import com.haoocai.jscheduler.client.zk.ZKClient;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.haoocai.jscheduler.client.util.Validate.checkArguments;
import static com.haoocai.jscheduler.client.util.Validate.checkNotNull;

/**
 * @author Michael Jiang on 16/3/31.
 */
class TaskWatcher implements TriggerHandler {
    private final ZKClient zkClient;
    private final String app;
    private final Task task;

    private static Logger LOG = LoggerFactory.getLogger(TaskWatcher.class);

    TaskWatcher(ZKClient zkClient, String app, Task task) {
        this.zkClient = checkNotNull(zkClient);
        this.task = checkNotNull(task);
        checkArguments(StringUtils.isNotBlank(app), "app name is blank");
        this.app = app;
    }

    //todo to be write
    void start() {
        String zkConnectAddr = zkClient.clientIdentify();
        String listenPath = "/" + app + "/" + task + "/" + zkConnectAddr;

        try {
            zkClient.createListenNode(listenPath);
            zkClient.listenNodeOnDateChange(listenPath, this);
        } catch (Exception e) {
            if (e instanceof KeeperException) {
                KeeperException err = (KeeperException) e;
                if (err.code() == KeeperException.Code.NODEEXISTS) {
                    throw new RuntimeException(String.format("app:%s name:%s already exist in zookeeper", app, task.name()));
                }
                if (err.code() == KeeperException.Code.NONODE) {
                    throw new RuntimeException(String.format("please start jscheduler center node first, and then create the %s in app:%s.", task.name(), app));
                }
                LOG.error("create task:{} listen node error,code:{},message:{}.", task.name(), err.code(), err.getMessage());
                throw new RuntimeException(e);
            }
            LOG.info("create task:{} listen node error:{}.", task.name(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handler(Object event) {
        if (event instanceof SchedulerContext) {

        }
        LOG.error("task watcher receive unexpected event.");
    }
}
