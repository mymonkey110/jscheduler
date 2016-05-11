package com.haoocai.jscheduler.core.task.impl;


import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import com.haoocai.jscheduler.core.task.TaskTracker;
import com.haoocai.jscheduler.core.trigger.Picker;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Zookeeper task tracker implementation
 *
 * @author Michael Jiang on 16/4/5.
 */
public class ZKTaskTracker extends TimerTask implements TaskTracker {
    private final CuratorFramework client;
    private final TaskDescriptor taskDescriptor;
    private Picker picker;
    private TaskInvoker taskInvoker;
    private Timer innerTimer;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskManager.class);

    public ZKTaskTracker(CuratorFramework client, TaskDescriptor taskDescriptor) {
        this.client = checkNotNull(client);
        this.taskDescriptor = checkNotNull(taskDescriptor);
    }

    @Override
    public void track() {
        LOG.info("start tacker for app:{} 's task:{}.", taskDescriptor.getApp(), taskDescriptor.getName());

        taskInvoker = new ZKTaskInvoker(client);

        Date nextRunTime = calcNextRunTime();
        innerTimer = new Timer(taskDescriptor.getApp() + "-" + taskDescriptor.getName() + "-" + "tracker");
        innerTimer.schedule(this, nextRunTime);
    }

    @Override
    public void untrack() {
        innerTimer.cancel();
    }

    @Override
    public void run() {
        //todo add pick later
        SchedulerUnit schedulerUnit = picker.assign();
        LOG.info("app:{} task:{} this time scheduler unit is:{}.", taskDescriptor.getApp(), taskDescriptor.getName(), schedulerUnit);
        taskInvoker.invoke(taskDescriptor, schedulerUnit);

        Date nextRunTime = calcNextRunTime();
        innerTimer = new Timer(taskDescriptor.getApp() + "-" + taskDescriptor.getName() + "-" + "tracker");
        innerTimer.schedule(this, nextRunTime);
    }

    private Date calcNextRunTime() {
        try {
            CronExpression cexp = new CronExpression(taskDescriptor.getCronExpression());
            return cexp.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
