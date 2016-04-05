package com.haoocai.jscheduler.core.trigger.impl;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.trigger.Picker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Michael Jiang on 16/4/1.
 */
@Component("rrPicker")
public class RoundRobinPicker implements Picker {
    @Autowired
    private ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(RoundRobinPicker.class);

    @Override
    public PickStrategy identify() {
        return PickStrategy.ROUND_ROBIN;
    }

    @Override
    public void init() {

    }

    //todo
    @Override
    public SchedulerUnit assign(TaskDescriptor taskDescriptor) {
        String taskNodePath = taskDescriptor.getApp() + "/" + taskDescriptor.getName();
        List<String> children = zkManager.getNodeChildren(taskNodePath);
        if (CollectionUtils.isEmpty(children)) {
            LOG.warn("not found available scheduler unit.");
            return null;
        }

        return null;
    }
}
