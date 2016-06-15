package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.TaskRepository;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

import static com.haoocai.jscheduler.core.Constants.UTF8_CHARSET;

/**
 * @author Michael Jiang on 16/6/15.
 */
@Repository
public class ZKTaskRepository implements TaskRepository {

    @Resource
    private ZKAccessor zkAccessor;

    @Override
    public void save(Task task) {
        TaskID taskID = task.getTaskID();

        //create task node first
        zkAccessor.create(taskID.identify(), new byte[0]);

        //initialize config node
        zkAccessor.create(taskID.identify() + "/config", new byte[0]);
        zkAccessor.create(taskID.identify() + "/config/cron", task.getCronExpression().getBytes(UTF8_CHARSET));

        //initialize server node
        zkAccessor.create(taskID.identify() + "/servers", new byte[0]);
    }

    @Override
    public Task load(TaskID taskID) {
        return null;
    }

    @Override
    public List<Task> getAppTask(String namespace, String app) {
        return null;
    }


    @Override
    public void delete(TaskID taskID) {

    }

    @Override
    public void update(TaskID taskID) {

    }
}
