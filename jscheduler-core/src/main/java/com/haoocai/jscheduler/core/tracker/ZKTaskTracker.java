package com.haoocai.jscheduler.core.tracker;


import com.haoocai.jscheduler.core.util.CronExpression;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.ZKTaskManager;
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
    private final Task task;
    private TaskInvoker taskInvoker;
    private Timer innerTimer;
    private Random random = new Random(System.currentTimeMillis());

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskManager.class);

    public ZKTaskTracker(ZKAccessor zkAccessor, Task task) {
        this.zkAccessor = checkNotNull(zkAccessor);
        this.task = checkNotNull(task);
    }

    @Override
    public void track() {
        TaskID taskID = task.getTaskID();
        LOG.info("start a tacker for app:{}'s task:{}.", taskID.getApp(), taskID.getName());
        taskInvoker = new ZKTaskInvoker(zkAccessor);
        Date nextRunTime = calcNextRunTime();
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
            schedulerUnit = choseSchedulerUnit();
            if (schedulerUnit != null) {
                LOG.trace("app:{} task:{} this time scheduler unit is:{}.", taskID.getApp(), taskID.getName(), schedulerUnit);
                taskInvoker.invoke(taskID, schedulerUnit);
            } else {
                LOG.trace("not found available server for task:{}.", taskID.getName());
            }
        } catch (Exception e) {
            LOG.error("chose scheduler unit error:{}.", e.getMessage(), e);
        } finally {
            Date nextRunTime = calcNextRunTime();
            LOG.trace("task:{} next run time:{}.", taskID.getName(), nextRunTime);
            innerTimer = new Timer(taskID.getApp() + "-" + taskID.getName() + "-" + "tracker");
            innerTimer.schedule(this, nextRunTime);
        }
    }

    /**
     * chose current run scheduler unit
     *
     * @return scheduler unit
     */
    private SchedulerUnit choseSchedulerUnit() throws Exception {
        TaskID taskID = task.getTaskID();
        String taskPath = taskID.identify();
        List<String> servers = zkAccessor.getChildren(taskPath + "/servers");
        if (CollectionUtils.isNotEmpty(servers)) {
            String chosenServer = servers.get(random.nextInt(servers.size()));
            String[] ipAddr = chosenServer.split(":");
            return new SchedulerUnit(ipAddr[0], Integer.parseInt(ipAddr[1]));
        } else {
            LOG.info("no available scheduler server for task:{}.", taskID.getName());
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
            CronExpression cexp = new CronExpression(task.getCron());
            return cexp.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
