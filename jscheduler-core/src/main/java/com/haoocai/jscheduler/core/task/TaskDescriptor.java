package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.CronExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.Map;

/**
 * Job Description
 *
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public class TaskDescriptor implements Serializable {
    private static final long serialVersionUID = 5224933468107682449L;

    private String app;

    private String name;

    private String cronExpression;

    private Map extraParams;

    public TaskDescriptor(String app, String name, String cronExpression) {
        this(app, name, cronExpression, null);
    }

    public TaskDescriptor(String app, String name, String cronExpression, Map extraParams) {
        Validate.isTrue(StringUtils.isNotBlank(app), "app name can't be blank");
        Validate.isTrue(StringUtils.isNotBlank(name), "task name can't be blank");
        Validate.isTrue(CronExpression.isValidExpression(cronExpression), "cron expression is not valid,please refer to 'https://en.wikipedia.org/wiki/Cron'");

        this.app = app;
        this.name = name;
        this.cronExpression = cronExpression;
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

    public Map getExtraParams() {
        return extraParams;
    }

    @Override
    public String toString() {
        return "TaskDescriptor{" +
                "app='" + app + '\'' +
                ", name='" + name + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", extraParams=" + extraParams +
                '}';
    }
}
