package com.haoocai.jscheduler.client.task;

import java.util.Objects;

/**
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public class TaskWatcher {
    private final Job job;

    public TaskWatcher(Job job) {
        Objects.requireNonNull(job, "job is null");
        this.job = job;
    }

    public void start() {

    }
}
