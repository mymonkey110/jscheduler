package com.haoocai.jscheduler.client.task;

/**
 * @author Michael Jiang on 16/3/31.
 */
public interface InvokeHandler {
    <T> void handler(T event);
}
