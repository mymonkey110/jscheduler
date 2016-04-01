package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.SchedulerContext;

import static com.haoocai.jscheduler.client.util.Validate.checkNotNull;

/**
 * @author Michael Jiang on 16/4/1.
 */
class TaskRunner implements Runnable {
    private final SchedulerContext context;
    private final Task task;

    TaskRunner(Task task, SchedulerContext context) {
        this.task = checkNotNull(task);
        this.context = context;
    }

    @Override
    public void run() {
        task.run(context);
    }
}
