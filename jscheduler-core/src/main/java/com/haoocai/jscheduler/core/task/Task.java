package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task Domain Object
 *
 * @author Michael Jiang on 16/6/14.
 */
public class Task {
    private final ZKAccessor zkAccessor;
    private final String namespace;
    private final String app;
    private final String name;

    Task(ZKAccessor zkAccessor, String namespace, String app, String name) {
        checkArgument(StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(app) && StringUtils.isNotBlank(name));

        this.zkAccessor = checkNotNull(zkAccessor);
        this.namespace = namespace;
        this.app = app;
        this.name = name;
    }

    public void init() {

    }

    public void updateCron(String newCronExpression) {

    }



}
