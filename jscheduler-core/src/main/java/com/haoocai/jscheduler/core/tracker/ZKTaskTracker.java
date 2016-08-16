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

package com.haoocai.jscheduler.core.tracker;

import com.haoocai.jscheduler.core.algorithm.Picker;
import com.haoocai.jscheduler.core.algorithm.PickerFactory;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.ZKTaskService;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task Tracker with Zookeeper Implementation
 * <p>
 * Every single task should have its own task tracker, they are one-to-one relationship.
 * Task tracker is responsible for calculation the task next run time point, and choose
 * one scheduler unit for the task.
 * </p>
 *
 * @author Michael Jiang on 16/4/5.
 */
public class ZKTaskTracker extends TimerTask {
    private final ZKAccessor zkAccessor;
    private final Task task;
    private final TaskID taskID;
    private final TaskInvoker invoker;
    private final Picker picker;

    private transient volatile boolean runFlag = false;
    private Lock runLock = new ReentrantLock();

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskService.class);

    public ZKTaskTracker(ZKAccessor zkAccessor, Task task) {
        this.zkAccessor = checkNotNull(zkAccessor);
        this.task = checkNotNull(task);
        this.taskID = task.getTaskID();
        this.picker = PickerFactory.createPicker(zkAccessor, task);
        this.invoker = new ZKTaskInvoker(zkAccessor);
    }

    public void track() {
        setNextTimer(this);
    }

    public void untrack() {
        cancel();
    }

    public void updateConfig() {
        if (!runFlag && runLock.tryLock()) {
            //cancel current timer first
            untrack();

            //set next timer
            setNextTimer(new ZKTaskTracker(zkAccessor, task));
        }
    }

    @Override
    public void run() {
        runFlag = true;
        if (runLock.tryLock()) {
            SchedulerUnit schedulerUnit;
            try {
                schedulerUnit = picker.assign();
                if (schedulerUnit != null) {
                    LOG.info("app:{} task:{} this time scheduler unit is:{}.", taskID.getApp(), taskID.getName(), schedulerUnit);
                    invoker.invoke(taskID, schedulerUnit);
                } else {
                    LOG.info("not found available server for task:{}.", taskID.getName());
                }
            } catch (Exception e) {
                LOG.error("chose scheduler unit error:{}.", e.getMessage(), e);
            } finally {
                setNextTimer(new ZKTaskTracker(zkAccessor, task));
            }
        }
    }

    private void setNextTimer(ZKTaskTracker zkTaskTracker) {
        Date nextRunTime = task.calcNextRunTime();
        LOG.info("task:{} next run time:{}.", taskID.getName(), DateFormatUtils.format(nextRunTime, "yyyy-MM-dd HH:mm:ss"));
        Timer timer = new Timer(taskID.getApp() + "-" + taskID.getName() + "-" + "tracker");
        task.registerNewTracker(zkTaskTracker);
        timer.schedule(zkTaskTracker, nextRunTime);
    }
}
