package com.haoocai.jscheduler.core.task.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.client.SchedulerContext;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskInvoker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Service
public class TaskInvokerImpl implements TaskInvoker {
    @Resource
    private ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(TaskInvokerImpl.class);

    private final static String INVOKE_PATH_TEMPLATE = "%s/%s/%s";

    @Override
    public void invoke(TaskDescriptor taskDescriptor, SchedulerUnit schedulerUnit) {
        Preconditions.checkNotNull(taskDescriptor);
        Preconditions.checkNotNull(schedulerUnit);

        String invokePath = String.format(INVOKE_PATH_TEMPLATE, taskDescriptor.getApp(), taskDescriptor.getName(), schedulerUnit.identify());

        SchedulerContext context = new SchedulerContext(taskDescriptor.getName(), System.currentTimeMillis(), taskDescriptor.getExtraParams());

        try {
            zkManager.writeNodeData(invokePath, context);
        } catch (Exception e) {
            LOG.error("try to invoke task:{} on {} encounter error,code:{}.", taskDescriptor.getName(), schedulerUnit.identify(), e.getMessage());
        }
    }

}
