package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.SchedulerContext;

/**
 * @author Michael Jiang on 16/3/31.
 */
public interface Task {
    /**
     * name name for job
     *
     * @return name name
     */
    String name();

    /**
     * execute specific job
     *
     * @param context schedule context
     */
    void run(SchedulerContext context);
}
