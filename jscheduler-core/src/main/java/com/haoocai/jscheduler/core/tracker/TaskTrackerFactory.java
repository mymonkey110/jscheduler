package com.haoocai.jscheduler.core.tracker;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.register.TaskRegisterCenter;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskID;
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
        Preconditions.checkNotNull(zkAccessor);

        Task task = TaskRegisterCenter.task(taskID);
        TaskTracker taskTracker = taskTrackerRegMap.get(taskID);
        if (taskTracker == null) {
            taskTracker = new ZKTaskTracker(zkAccessor, task);
            taskTrackerRegMap.put(taskID, taskTracker);
        }

        return taskTracker;
    }
}
