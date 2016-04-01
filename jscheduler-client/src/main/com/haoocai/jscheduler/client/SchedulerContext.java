package com.haoocai.jscheduler.client;

import com.haoocai.jscheduler.client.util.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Scheduler Context
 *
 * @author Michael Jiang on 16/3/16.
 */
public class SchedulerContext implements Serializable {
    private static final long serialVersionUID = -4629236096395980429L;

    private String taskName;

    private long invokeTime;

    private Map extraParams;

    public SchedulerContext(String taskName, long invokeTime) {
        setTaskName(taskName);
        setInvokeTime(invokeTime);
    }

    public SchedulerContext(String taskName, long invokeTime, Map extraParams) {
        setTaskName(taskName);
        setInvokeTime(invokeTime);
        setExtraParams(extraParams);
    }

    private void setTaskName(String taskName) {
        if (StringUtils.isBlank(taskName)) {
            throw new IllegalArgumentException("name name can't be blank");
        }
        this.taskName = taskName;
    }

    private void setInvokeTime(long invokeTime) {
        if (invokeTime <= 0) {
            throw new IllegalArgumentException("invoke time <0");
        }
        this.invokeTime = invokeTime;
    }

    private void setExtraParams(Map extraParams) {
        this.extraParams = extraParams;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getInvokeTime() {
        return invokeTime;
    }

    public Map getExtraParams() {
        return extraParams;
    }

    @Override
    public String toString() {
        return "SchedulerContext{" +
                "taskName='" + taskName + '\'' +
                ", invokeTime=" + invokeTime +
                ", extraParams=" + extraParams +
                '}';
    }
}
