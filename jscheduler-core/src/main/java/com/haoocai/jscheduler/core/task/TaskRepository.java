package com.haoocai.jscheduler.core.task;

import java.util.List;

/**
 * @author Michael Jiang on 16/6/15.
 */
public interface TaskRepository {
    void save(Task task);

    Task load(TaskID taskID);

    List<Task> getAppTask(String namespace, String app);

    void delete(TaskID taskID);

    void update(TaskID taskID);
}
