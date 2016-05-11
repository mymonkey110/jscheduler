package com.haoocai.jscheduler.core.task;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.impl.ZKTaskTracker;
import org.apache.curator.framework.CuratorFramework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael Jiang on 16/5/11.
 */
public class TaskTrackerFactory {

    private static Map<TaskID, TaskTracker> taskTrackerRegMap = new ConcurrentHashMap<>();

    public synchronized static TaskTracker getTaskTracker(TaskDescriptor taskDescriptor, CuratorFramework client) {
        Preconditions.checkNotNull(taskDescriptor);

        TaskID taskID = new TaskID(taskDescriptor);
        TaskTracker taskTracker = taskTrackerRegMap.get(taskID);
        if (taskTracker == null) {
            taskTracker = new ZKTaskTracker(client, taskDescriptor);
            taskTrackerRegMap.put(taskID, taskTracker);
        }

        return taskTracker;
    }
}
