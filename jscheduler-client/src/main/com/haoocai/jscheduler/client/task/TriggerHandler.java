package com.haoocai.jscheduler.client.task;

/**
 * @author Michael Jiang on 16/3/31.
 */
public interface TriggerHandler<T> {
    void handler(T event);
}
