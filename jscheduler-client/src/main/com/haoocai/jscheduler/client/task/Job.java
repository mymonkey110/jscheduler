package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.SchedulerContext;

/**
 * @author Michael Jiang on 16/3/31.
 */
public interface Job {
    /**
     * task name for job
     *
     * @return task name
     */
    String task();

    /**
     * execute specific job
     *
     * @param context schedule context
     */
    void doJob(SchedulerContext context);
}
