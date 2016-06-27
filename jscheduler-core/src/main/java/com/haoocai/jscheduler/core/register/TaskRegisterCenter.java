package com.haoocai.jscheduler.core.register;

import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael Jiang on 6/27/16.
 */
public class TaskRegisterCenter {
    private static ConcurrentHashMap<TaskID, Task> taskMap = new ConcurrentHashMap<>(8);

    public synchronized static void register(TaskID taskID, Task task) {
        if (!taskMap.containsKey(taskID)) {
            taskMap.put(taskID, task);
        }
    }

    public static Task task(TaskID taskID) {
        return taskMap.get(taskID);
    }

    public static boolean exist(TaskID taskID) {
        return taskMap.containsKey(taskID);
    }
}
