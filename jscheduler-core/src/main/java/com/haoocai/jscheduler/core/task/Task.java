package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task Domain Object
 *
 * @author Michael Jiang on 16/6/14.
 */
public class Task {

    private ZKAccessor zkAccessor;

    private TaskID taskID;

    private ConfigNode configNode;

    private ServerNode serverNode;

    public Task(TaskID taskID, ZKAccessor zkAccessor) {
        this.taskID = checkNotNull(taskID);
        this.zkAccessor = checkNotNull(zkAccessor);
    }

    Task(TaskID taskID, Cron cron, ZKAccessor zkAccessor) {
        this(taskID, cron, PickStrategy.RANDOM, zkAccessor);
    }

    Task(TaskID taskID, Cron cron, PickStrategy pickStrategy, ZKAccessor zkAccessor) {
        this.taskID = taskID;
        this.zkAccessor = zkAccessor;
        this.serverNode = new ServerNode(zkAccessor, taskID);
        this.configNode = new ConfigNode(zkAccessor, taskID, cron, pickStrategy);
    }

    public void setConfigNode(ConfigNode configNode) {
        this.configNode = configNode;
    }

    public void setServerNode(ServerNode serverNode) {
        this.serverNode = serverNode;
    }

    public TaskID getTaskID() {
        return taskID;
    }

    public static Task load(ZKAccessor zkAccessor, TaskID taskID) {
        Task task = new Task(taskID, zkAccessor);
        task.setConfigNode(ConfigNode.load(zkAccessor, taskID));
        task.setServerNode(ServerNode.load(zkAccessor, taskID));
        return task;
    }

    public void init() {
        //create task node first
        zkAccessor.create(taskID.identify(), new byte[0]);

        configNode.init();
        serverNode.init();
    }

    public void changeCron(Cron cron) {
        this.configNode.changeCron(cron);
    }

    public String getCron() {
        return this.configNode.getCron().cron();
    }

    public PickStrategy getPickStrategy() {
        return this.configNode.getPickStrategy();
    }

    public List<SchedulerUnit> getTaskSchedulerUnits() {
        return serverNode.getTaskSchedulerUnits();
    }

    public TaskDescriptor getTaskDescriptor() {
        return new TaskDescriptor(taskID, getCron(), getPickStrategy());
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", configNode=" + configNode +
                ", serverNode=" + serverNode +
                '}';
    }
}
