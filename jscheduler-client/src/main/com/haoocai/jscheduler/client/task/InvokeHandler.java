package com.haoocai.jscheduler.client.task;

/**
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public interface InvokeHandler {
    <T> void handler(T event);
}
