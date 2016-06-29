package com.haoocai.jscheduler.core.tracker;

import com.haoocai.jscheduler.core.algorithm.Picker;
import com.haoocai.jscheduler.core.algorithm.PickerFactory;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.ZKTaskService;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
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
    private final Task task;
    private final TaskID taskID;
    private final TaskInvoker invoker;
    private Timer innerTimer;
    private final Picker picker;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskService.class);

    ZKTaskTracker(ZKAccessor zkAccessor, Task task) {
        this.task = checkNotNull(task);
        this.taskID = task.getTaskID();
        this.picker = PickerFactory.createPicker(zkAccessor, taskID, task.getPickStrategy());
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
        TaskID taskID = task.getTaskID();
        SchedulerUnit schedulerUnit;
        try {
            schedulerUnit = picker.assign();
            if (schedulerUnit != null) {
                LOG.trace("app:{} task:{} this time scheduler unit is:{}.", taskID.getApp(), taskID.getName(), schedulerUnit);
                invoker.invoke(taskID, schedulerUnit);
            } else {
                LOG.trace("not found available server for task:{}.", taskID.getName());
            }
        } catch (Exception e) {
            LOG.error("chose scheduler unit error:{}.", e.getMessage(), e);
        } finally {
            Date nextRunTime = task.calcNextRunTime();
            LOG.trace("task:{} next run time:{}.", taskID.getName(), nextRunTime);
            innerTimer = new Timer(taskID.getApp() + "-" + taskID.getName() + "-" + "tracker");
            innerTimer.schedule(this, nextRunTime);
        }
    }

}
