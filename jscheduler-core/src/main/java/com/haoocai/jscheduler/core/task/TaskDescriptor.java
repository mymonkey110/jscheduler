package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.Map;

/**
 * Task Description
 *
 * @author Michael Jiang on 16/3/16.
 */
public class TaskDescriptor implements Serializable {
    private static final long serialVersionUID = 5224933468107682449L;
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

    public TaskDescriptor(String app, String name, String cronExpression) {
        this(app, name, cronExpression, PickStrategy.RANDOM, null);
    }

    public TaskDescriptor(String app, String name, String cronExpression, PickStrategy pickStrategy, Map extraParams) {
        Validate.isTrue(StringUtils.isNotBlank(app), "app name can't be blank");
        Validate.isTrue(StringUtils.isNotBlank(name), "name name can't be blank");
        Validate.isTrue(CronExpression.isValidExpression(cronExpression), "cron expression is not valid,please refer to 'https://en.wikipedia.org/wiki/Cron'");
        Validate.notNull(pickStrategy, "please specified the pick strategy");

        this.app = app;
        this.name = name;
        this.cronExpression = cronExpression;
        this.pickStrategy = pickStrategy;
        if (extraParams != null && !extraParams.isEmpty()) {
            this.extraParams = extraParams;
        }
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

    @Override
    public String toString() {
        return "TaskDescriptor{" +
                "app='" + app + '\'' +
                ", name='" + name + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", pickStrategy=" + pickStrategy +
                ", extraParams=" + extraParams +
                '}';
    }
}
