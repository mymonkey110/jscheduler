package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.Map;

/**
 * Task Description
 * <p>
 * Task description include meta data for a task.
 * Every task should belong to an app. App is the basic business logic unit, it could have many
 * time scheduler tasks.
 * </p>
 *
 * @author Michael Jiang on 16/3/16.
 */
public class TaskDescriptor implements Serializable {
    private static final long serialVersionUID = 5224933468107682449L;

    /**
     * namespace
     */
    private String namespace;
    /**
     * app name
     */
    private String app;
    /**
     * task name
     */
    private String name;
    /**
     * scheduler cron expression
     */
    private String cronExpression;
    /**
     * pick strategy
     */
    private PickStrategy pickStrategy;
    /**
     * extra map
     */
    private Map extraParams;

    public TaskDescriptor(String namespace, String app, String name, String cronExpression) {
        this(namespace, app, name, cronExpression, PickStrategy.RANDOM, null);
    }

    public TaskDescriptor(String namespace, String app, String name, String cronExpression, PickStrategy pickStrategy, Map extraParams) {
        Validate.isTrue(StringUtils.isNotBlank(app), "app name can't be blank");
        Validate.isTrue(StringUtils.isNotBlank(name), "name name can't be blank");
        Validate.isTrue(CronExpression.isValidExpression(cronExpression), "cron expression is not valid,please refer to 'https://en.wikipedia.org/wiki/Cron'");
        Validate.notNull(pickStrategy, "please specified the pick strategy");

        this.namespace = namespace;
        this.app = app;
        this.name = name;
        this.cronExpression = cronExpression;
        this.pickStrategy = pickStrategy;
        if (extraParams != null && !extraParams.isEmpty()) {
            this.extraParams = extraParams;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getApp() {
        return app;
    }

    public String getName() {
        return name;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public PickStrategy getPickStrategy() {
        return pickStrategy;
    }

    public Map getExtraParams() {
        return extraParams;
    }

    public String taskPath() {
        return "/" + namespace + app + "/" + name;
    }

    @Override
    public String toString() {
        return "TaskDescriptor{" +
                "namespace='" + namespace + '\'' +
                ", app='" + app + '\'' +
                ", name='" + name + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", pickStrategy=" + pickStrategy +
                ", extraParams=" + extraParams +
                '}';
    }
}
