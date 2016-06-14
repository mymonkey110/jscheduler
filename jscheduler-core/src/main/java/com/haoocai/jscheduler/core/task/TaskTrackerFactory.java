package com.haoocai.jscheduler.core.task;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.impl.ZKTaskTracker;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.haoocai.jscheduler.core.Constants.UTF8_CHARSET;

/**
 * @author Michael Jiang on 16/5/11.
 */
public class TaskTrackerFactory {

    private static Map<TaskID, TaskTracker> taskTrackerRegMap = new ConcurrentHashMap<>();

    public synchronized static TaskTracker getTaskTracker(TaskID taskID, ZKAccessor zkAccessor) throws Exception {
        Preconditions.checkNotNull(taskID);

        TaskTracker taskTracker = taskTrackerRegMap.get(taskID);
        if (taskTracker == null) {
            byte[] data = zkAccessor.getData(taskID.identify() + "/config/cron");
            TaskDescriptor taskDescriptor = new TaskDescriptor(taskID.getNamespace(), taskID.getApp(), taskID.getName(), new String(data, UTF8_CHARSET));
            taskTracker = new ZKTaskTracker(zkAccessor, taskDescriptor);
            taskTrackerRegMap.put(taskID, taskTracker);
        }

        return taskTracker;
    }
}
