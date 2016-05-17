package com.haoocai.jscheduler.core.trigger.impl;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.trigger.Picker;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Michael Jiang on 16/4/1.
 */
public class RoundRobinPicker implements Picker {
    private ZKManager zkManager;
    private TaskDescriptor taskDescriptor;

    private static Logger LOG = LoggerFactory.getLogger(RoundRobinPicker.class);

    public RoundRobinPicker(TaskDescriptor taskDescriptor, ZKManager zkManager) {
        zkManager = checkNotNull(zkManager);
        taskDescriptor = checkNotNull(taskDescriptor);
    }

    @Override
    public PickStrategy identify() {
        return PickStrategy.ROUND_ROBIN;
    }

    //todo
    @Override
    public SchedulerUnit assign() throws Exception {
        String taskNodePath = taskDescriptor.getApp() + "/" + taskDescriptor.getName();
        List<String> children = zkManager.getClient().getChildren().forPath(taskNodePath);
        if (CollectionUtils.isEmpty(children)) {
            LOG.warn("not found available scheduler unit.");
            return null;
        }

        return null;
    }
}
