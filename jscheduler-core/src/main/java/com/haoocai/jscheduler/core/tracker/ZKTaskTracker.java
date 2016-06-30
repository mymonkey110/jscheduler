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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Zookeeper task tracker implementation
 *
 * @author Michael Jiang on 16/4/5.
 */
class ZKTaskTracker extends TimerTask implements TaskTracker {
    private final ZKAccessor zkAccessor;
    private final Task task;
    private final TaskID taskID;
    private final TaskInvoker invoker;
    private Timer innerTimer;
    private final Picker picker;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskService.class);

    ZKTaskTracker(ZKAccessor zkAccessor, Task task) {
        this.zkAccessor = checkNotNull(zkAccessor);
        this.task = checkNotNull(task);
        this.taskID = task.getTaskID();
        this.picker = PickerFactory.createPicker(zkAccessor, task);
        this.invoker = new ZKTaskInvoker(zkAccessor);
    }

    @Override
    public void track() {
        LOG.info("start a tacker for app:{}'s task:{}.", taskID.getApp(), taskID.getName());
        Date nextRunTime = task.calcNextRunTime();
        innerTimer = new Timer(taskID.getApp() + "-" + taskID.getName() + "-" + "tracker");
        innerTimer.schedule(this, nextRunTime);
    }

    @Override
    public void untrack() {
        innerTimer.cancel();
    }

    @Override
    public void run() {
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
            Date nextRunTime = task.calcNextRunTime();
            LOG.info("task:{} next run time:{}.", taskID.getName(), DateFormatUtils.format(nextRunTime,"yyyy-MM-dd HH:mm:ss"));
            innerTimer = new Timer(taskID.getApp() + "-" + taskID.getName() + "-" + "tracker");
            innerTimer.schedule(new ZKTaskTracker(this.zkAccessor, this.task), nextRunTime);
        }
    }

}
