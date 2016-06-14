package com.haoocai.jscheduler.core.task.impl;


import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import com.haoocai.jscheduler.core.task.TaskTracker;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Zookeeper task tracker implementation
 *
 * @author Michael Jiang on 16/4/5.
 */
public class ZKTaskTracker extends TimerTask implements TaskTracker {
    private final ZKAccessor zkAccessor;
    private final TaskDescriptor taskDescriptor;
    private TaskInvoker taskInvoker;
    private Timer innerTimer;
    private Random random = new Random(System.currentTimeMillis());

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskManager.class);

    public ZKTaskTracker(ZKAccessor zkAccessor, TaskDescriptor taskDescriptor) {
        this.zkAccessor = checkNotNull(zkAccessor);
        this.taskDescriptor = checkNotNull(taskDescriptor);
    }

    @Override
    public void track() {
        LOG.info("start a tacker for app:{} 's task:{}.", taskDescriptor.getApp(), taskDescriptor.getName());
        taskInvoker = new ZKTaskInvoker(zkAccessor);
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
        SchedulerUnit schedulerUnit;
        try {
            schedulerUnit = choseSchedulerUnit();
            if (schedulerUnit != null) {
                LOG.trace("app:{} task:{} this time scheduler unit is:{}.", taskDescriptor.getApp(), taskDescriptor.getName(), schedulerUnit);
                taskInvoker.invoke(taskDescriptor, schedulerUnit);
            } else {
                LOG.trace("not found available server for task:{}.", taskDescriptor.getName());
            }
        } catch (Exception e) {
            LOG.error("chose scheduler unit error:{}.", e.getMessage(), e);
        } finally {
            Date nextRunTime = calcNextRunTime();
            LOG.trace("task:{} next run time:{}.", taskDescriptor.getName(), nextRunTime);
            innerTimer = new Timer(taskDescriptor.getApp() + "-" + taskDescriptor.getName() + "-" + "tracker");
            innerTimer.schedule(this, nextRunTime);
        }
    }

    /**
     * chose current run scheduler unit
     *
     * @return scheduler unit
     * @throws Exception
     */
    private SchedulerUnit choseSchedulerUnit() throws Exception {
        String taskPath = taskDescriptor.taskPath();
        List<String> servers = zkAccessor.getChildren(taskPath + "/servers");
        if (CollectionUtils.isNotEmpty(servers)) {
            String chosenServer = servers.get(random.nextInt(servers.size()));
            String[] ipAddr = chosenServer.split(":");
            return new SchedulerUnit(ipAddr[0], Integer.parseInt(ipAddr[1]));
        } else {
            LOG.info("no available scheduler server for task:{}.", taskDescriptor.getName());
            return null;
        }
    }

    /**
     * calculate the task next run time point
     *
     * @return next run time point
     */
    private Date calcNextRunTime() {
        try {
            CronExpression cexp = new CronExpression(taskDescriptor.getCronExpression());
            return cexp.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
