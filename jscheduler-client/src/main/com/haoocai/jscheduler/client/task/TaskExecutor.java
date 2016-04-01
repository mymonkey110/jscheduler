package com.haoocai.jscheduler.client.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Michael Jiang on 16/3/31.
 */
public class TaskExecutor {
    private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public void submitJob(TaskRunner taskRunner) {
        executor.execute(taskRunner);
    }
}
