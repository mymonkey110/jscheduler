package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task invoker with zookeeper implementation
 *
 * @author Michael Jiang on 16/3/16.
 */
class ZKTaskInvoker implements TaskInvoker {
    private final ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskInvoker.class);

    ZKTaskInvoker(ZKManager zkManager) {
        this.zkManager = zkManager;
    }

    //todo add failure & success scenario handler
    @Override
    public void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit) {
        checkNotNull(taskDescriptor);
        checkNotNull(schedulerUnit);

        LOG.trace("invoke task:{} on scheduler unit:{}.", taskDescriptor, schedulerUnit);
        try {
            zkManager.setData(taskDescriptor.taskPath() + "/" + schedulerUnit.identify(), Long.toHexString(System.currentTimeMillis()).getBytes());
            //todo success handler
        } catch (Exception e) {
            LOG.info("invoke error:{}.", e.getMessage(), e);
            //todo exception handler
        }
    }

}
