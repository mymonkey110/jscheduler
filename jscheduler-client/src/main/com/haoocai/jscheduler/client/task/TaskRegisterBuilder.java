package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.util.StringUtils;
import com.haoocai.jscheduler.client.util.Validate;
import com.haoocai.jscheduler.client.zk.ZKClient;

import static com.haoocai.jscheduler.client.util.Validate.checkArguments;

/**
 * @author Michael Jiang on 16/4/1.
 */
public class TaskRegisterBuilder {
    private final ZKClient zkClient;
    private String namespace;
    private String app;

    private TaskRegisterBuilder(ZKClient zkClient) {
        this.zkClient = Validate.checkNotNull(zkClient);
    }

    public static TaskRegisterBuilder newInstance(ZKClient zkClient) {
        return new TaskRegisterBuilder(zkClient);
    }

    public TaskRegisterBuilder setNamespace(String namespace) {
        checkArguments(StringUtils.isNotBlank(namespace), "namespace is blank");
        this.namespace = namespace;
        return this;
    }

    public TaskRegisterBuilder setApp(String app) {
        checkArguments(StringUtils.isNotBlank(app), "app is blank");
        this.app = app;
        return this;
    }

    public TaskRegister build() {
        return new TaskRegister(namespace, app, zkClient);
    }
}
