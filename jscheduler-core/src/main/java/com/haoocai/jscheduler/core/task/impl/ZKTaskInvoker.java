package com.haoocai.jscheduler.core.task.impl;

import com.haoocai.jscheduler.client.SchedulerContext;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Michael Jiang on 16/3/16.
 */
class ZKTaskInvoker implements TaskInvoker {
    private ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskInvoker.class);
    private final static String INVOKE_PATH_TEMPLATE = "%s/%s/%s";

    ZKTaskInvoker(ZKManager zkManager) {
        this.zkManager=checkNotNull(zkManager);
    }

    @Override
    public void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit) {
        checkNotNull(taskDescriptor);
        checkNotNull(schedulerUnit);

        String invokePath = String.format(INVOKE_PATH_TEMPLATE, taskDescriptor.getApp(), taskDescriptor.getName(), schedulerUnit.identify());

        SchedulerContext context = new SchedulerContext(taskDescriptor.getName(), System.currentTimeMillis(), taskDescriptor.getExtraParams());

        try {
            zkManager.writeNodeData(invokePath, context);
        } catch (Exception e) {
            LOG.error("try to invoke name:{} on {} encounter error,code:{}.", taskDescriptor.getName(), schedulerUnit.identify(), e.getMessage());
        }
    }

}
