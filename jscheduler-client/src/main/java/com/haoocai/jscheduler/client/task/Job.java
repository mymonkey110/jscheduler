package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.SchedulerContext;

/**
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public interface Job {
    /**
     * execute specific job
     *
     * @param context schedule context
     */
    void doJob(SchedulerContext context);
}
