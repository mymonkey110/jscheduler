/*
 * Copyright 2016 Michael Jiang
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

package com.haoocai.jscheduler.core.monitor;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Task Execution Monitor
 * Created by Michael Jiang on 2016/8/21.
 */
public class TaskExecutionMonitor implements Watcher {
    private final ZKAccessor zkAccessor;
    private final Task task;

    private long lastTriggerPoint;
    private SchedulerUnit lastSchedulerUnit;
    private Lock updateTriggerLock = new ReentrantLock();

    private long recvDonePoint;
    private SchedulerUnit recvDoneSchedulerUnit;
    private Lock updateDoneInfoLock = new ReentrantLock();

    public TaskExecutionMonitor(ZKAccessor zkAccessor, Task task) {
        this.zkAccessor = zkAccessor;
        this.task = task;
    }

    public void watch() {
        zkAccessor.addDataListener(task.getTaskID().identify() + "/servers", this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        watch();
        if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            byte[] doneSchedulerUnitBytes = zkAccessor.getData(watchedEvent.getPath());
            updateDoneInfoLock.lock();
            recvDoneSchedulerUnit = new SchedulerUnit(new String(doneSchedulerUnitBytes));
            recvDonePoint = System.currentTimeMillis();
            updateDoneInfoLock.unlock();
        }
    }

    public void setLastSchedulerUnit(SchedulerUnit schedulerUnit) {
        updateTriggerLock.lock();
        this.lastSchedulerUnit = schedulerUnit;
        this.lastTriggerPoint = System.currentTimeMillis();
        updateTriggerLock.unlock();
    }

    public boolean isLastJobRunOver() {
        if (lastSchedulerUnit == null) {
            return true;
        }
        updateTriggerLock.lock();
        boolean runOverFlag = this.lastSchedulerUnit.isSame(this.recvDoneSchedulerUnit) && this.lastTriggerPoint < this.recvDonePoint;
        updateTriggerLock.unlock();
        return runOverFlag;
    }

    public SchedulerUnit getLastSchedulerUnit() {
        return lastSchedulerUnit;
    }
}
