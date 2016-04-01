package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.SchedulerContext;

import static com.haoocai.jscheduler.client.util.Validate.checkNotNull;

/**
 * @author Michael Jiang on 16/4/1.
 */
public class JobRunner implements Runnable {
    private final SchedulerContext context;
    private final Job job;

    JobRunner(Job job, SchedulerContext context) {
        this.job = checkNotNull(job);
        this.context = context;
    }

    @Override
    public void run() {
        job.doJob(context);
    }
}
