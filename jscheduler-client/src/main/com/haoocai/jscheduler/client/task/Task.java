package com.haoocai.jscheduler.client.task;

/**
 * @author Michael Jiang on 6/30/16.
 */
interface Task {

    String name();

    void run(SchedulerContext context);
}
