package com.haoocai.jscheduler.client.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public class TaskExecutor {
    private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public static void main(String[] args) {

    }
}
