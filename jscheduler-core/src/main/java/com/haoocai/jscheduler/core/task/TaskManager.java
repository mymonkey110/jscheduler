package com.haoocai.jscheduler.core.task;

import java.util.List;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public interface TaskManager {

    List<TaskDescriptor> getAllTaskDescriptor();

    TaskDescriptor getSpecTaskDescriptor(String taskName);
}
