package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.util.Validate;
import com.haoocai.jscheduler.client.zk.ZKClient;
import com.haoocai.jscheduler.client.SchedulerContext;
import com.haoocai.jscheduler.client.util.StringUtils;

import static com.haoocai.jscheduler.client.util.Validate.checkArguments;

/**
 * @author Michael Jiang on 16/3/31.
 */
public class TaskWatcher implements InvokeHandler {
    private final ZKClient zkClient;
    private final String app;
    private final Job job;

    public TaskWatcher(ZKClient zkClient, String app, Job job) {
        this.zkClient = Validate.checkNotNull(zkClient);
        this.job = Validate.checkNotNull(job);

        Validate.checkArguments(StringUtils.isNotBlank(app), "app name is blank");
        this.app = app;
    }

    //todo to be write
    public void start() {

        //zkClient.createNode();
    }

    @Override
    public <T> void handler(T event) {
        if (event instanceof SchedulerContext) {

        }


    }
}
