package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.exception.TaskNotFoundException;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Task Domain Object
 *
 * @author Michael Jiang on 16/6/14.
 */
@Service
public class Task {

    @Resource
    private ZKAccessor zkAccessor;

    private TaskID taskID;

    private ConfigNode configNode;

    private ServerNode serverNode;

    public Task() {
    }

    Task(TaskID taskID, String cronExpression) {
        this(taskID, cronExpression, PickStrategy.RANDOM);
    }

    Task(TaskID taskID, String cronExpression, PickStrategy pickStrategy) {
        this.taskID = taskID;
        this.serverNode = new ServerNode(zkAccessor, taskID);
        this.configNode = new ConfigNode(zkAccessor, taskID, cronExpression, pickStrategy);
    }

    public void setZkAccessor(ZKAccessor zkAccessor) {
        this.zkAccessor = zkAccessor;
    }

    public TaskID getTaskID() {
        return taskID;
    }

    public void init() {
        //create task node first
        zkAccessor.create(taskID.identify(), new byte[0]);

        configNode.init();
        serverNode.init();
    }

    public Task load(TaskID taskID) throws TaskNotFoundException {
        if (!zkAccessor.checkNodeExist(taskID.identify())) {
            throw new TaskNotFoundException();
        }


    }

    //todo set task tracker cron expression
    public void setCronExpr(String newCronExpression) {
        configNode.setCronExpr(newCronExpression);
    }


}
