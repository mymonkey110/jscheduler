package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.trigger.PickStrategy;

import java.io.Serializable;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

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
     * task is running flag
     */
    private boolean isRunning;
    /**
     * extra map
     */
    private Map extraParams;


    public TaskDescriptor(TaskID taskID, String cronExpression) {
        this(taskID, cronExpression, PickStrategy.RANDOM, null);
    }

    public TaskDescriptor(TaskID taskID, String cronExpression, PickStrategy pickStrategy) {
        this(taskID, cronExpression, pickStrategy, null);
    }

    public TaskDescriptor(TaskID taskID, String cronExpression, PickStrategy pickStrategy, Map extraParams) {
        checkNotNull(taskID, "task id name can't be blank");

        this.namespace = taskID.getNamespace();
        this.app = taskID.getApp();
        this.name = taskID.getName();
        this.cronExpression = cronExpression;
        this.pickStrategy = pickStrategy;
        if (extraParams != null && !extraParams.isEmpty()) {
            this.extraParams = extraParams;
        }
    }

    String getNamespace() {
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
        return "/" + namespace + "/" + app + "/" + name;
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
