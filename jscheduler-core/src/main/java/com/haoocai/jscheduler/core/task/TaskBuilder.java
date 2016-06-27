package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Michael Jiang on 16/6/14.
 */
class TaskBuilder {
    private String namespace;
    private String app;
    private String taskName;
    private String cronExpression;
    private PickStrategy pickStrategy;

    public TaskBuilder(String namespace, String app, String taskName) {
        this.namespace = namespace;
        this.app = app;
        this.taskName = taskName;
    }

    public TaskBuilder setCron(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public TaskBuilder setPickStrategy(PickStrategy pickStrategy) {
        this.pickStrategy = checkNotNull(pickStrategy);
        return this;
    }

    //fixme
    public Task create() {
        checkArgument(StringUtils.isNotBlank(namespace), "namespace is blank");
        checkArgument(StringUtils.isNotBlank(app), "app is blank");
        checkArgument(StringUtils.isNotBlank(taskName), "task name is blank");
        checkArgument(CronExpression.isValidExpression(cronExpression), "cron expression:%s is not valid", cronExpression);

        TaskID taskID = new TaskID(namespace, app, taskName);

    }
}
