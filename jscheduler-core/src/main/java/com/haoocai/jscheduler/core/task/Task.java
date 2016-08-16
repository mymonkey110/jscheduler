/*
 * Copyright 2016  Michael Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.algorithm.PickStrategy;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.tracker.ZKTaskTracker;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.Date;
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

    private StatusNode statusNode;

    private ZKTaskTracker taskTracker;

    public Task(TaskID taskID, ZKAccessor zkAccessor) {
        this.taskID = checkNotNull(taskID);
        this.zkAccessor = checkNotNull(zkAccessor);
    }

    public Task(TaskID taskID, Cron cron, ZKAccessor zkAccessor) {
        this(taskID, cron, PickStrategy.RANDOM, zkAccessor);
    }

    Task(TaskID taskID, Cron cron, PickStrategy pickStrategy, ZKAccessor zkAccessor) {
        this.taskID = taskID;
        this.zkAccessor = zkAccessor;
        this.configNode = new ConfigNode(zkAccessor, taskID, cron, pickStrategy);
        this.serverNode = ServerNode.load(zkAccessor, taskID);
        this.statusNode = StatusNode.load(zkAccessor, taskID);
    }

    private void setConfigNode(ConfigNode configNode) {
        this.configNode = configNode;
    }

    private void setServerNode(ServerNode serverNode) {
        this.serverNode = serverNode;
    }

    private void setStatusNode(StatusNode statusNode) {
        this.statusNode = statusNode;
    }

    public TaskID getTaskID() {
        return taskID;
    }

    static Task load(ZKAccessor zkAccessor, TaskID taskID) {
        Task task = new Task(taskID, zkAccessor);
        task.setConfigNode(ConfigNode.load(zkAccessor, taskID));
        task.setServerNode(ServerNode.load(zkAccessor, taskID));
        task.setStatusNode(StatusNode.load(zkAccessor, taskID));
        return task;
    }

    void initNode() {
        //create task node first
        zkAccessor.create(taskID.identify(), new byte[0]);

        //initialize node
        configNode.init();
        serverNode.init();
        statusNode.init();
    }

    void changeCron(Cron cron) {
        this.configNode.changeCron(cron);
        if (taskTracker != null) {
            taskTracker.updateConfig();
        }
    }

    void changePickStrategy(PickStrategy pickStrategy) {
        this.configNode.changePickStrategy(pickStrategy);
        if (taskTracker != null) {
            taskTracker.updateConfig();
        }
    }

    void changeConfig(Cron cron, PickStrategy pickStrategy) {
        this.configNode.changeCron(cron);
        this.configNode.changePickStrategy(pickStrategy);
        if (taskTracker != null) {
            taskTracker.updateConfig();
        }
    }

    public String getCron() {
        return this.configNode.getCron().cron();
    }

    public Date calcNextRunTime() {
        return this.configNode.calcNextRunTime();
    }

    public boolean isRunning() {
        return statusNode.isRunning();
    }

    public void init() {
        this.statusNode.init();
    }

    public void start() {
        this.statusNode.init();
        if (taskTracker == null) {
            taskTracker = new ZKTaskTracker(zkAccessor, this);
            taskTracker.track();
        }
    }

    public void stop() {
        this.taskTracker.untrack();

        this.statusNode.delete();
    }

    public void registerNewTracker(ZKTaskTracker taskTracker) {
        this.taskTracker = taskTracker;
    }

    public PickStrategy getPickStrategy() {
        return this.configNode.getPickStrategy();
    }

    public List<SchedulerUnit> getTaskSchedulerUnits() {
        return serverNode.getTaskSchedulerUnits();
    }

    TaskDescriptor getTaskDescriptor() {
        return new TaskDescriptor(taskID, getCron(), getPickStrategy());
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", configNode=" + configNode +
                ", serverNode=" + serverNode +
                ", statusNode=" + statusNode +
                '}';
    }
}
