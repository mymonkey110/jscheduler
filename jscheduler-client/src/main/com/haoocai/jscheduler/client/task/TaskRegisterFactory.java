package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.util.StringUtils;
import com.haoocai.jscheduler.client.zk.ZKClient;

import static com.haoocai.jscheduler.client.util.Validate.checkArguments;
import static com.haoocai.jscheduler.client.util.Validate.checkNotNull;

/**
 * @author Michael Jiang on 16/4/1.
 */
public class TaskRegisterFactory {
    private ZKClient zkClient;
    private String app;

    public TaskRegisterFactory setZkClient(ZKClient zkClient) {
        this.zkClient = zkClient;
        return this;
    }

    public TaskRegisterFactory setApp(String app) {

        this.app = app;
        return this;
    }

    public TaskRegister build() {
        checkNotNull(zkClient);
        checkArguments(StringUtils.isNotBlank(app), "app name can't be blank");

        return new TaskRegister(zkClient, app);
    }
}
