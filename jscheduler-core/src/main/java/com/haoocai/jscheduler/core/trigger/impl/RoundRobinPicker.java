package com.haoocai.jscheduler.core.trigger.impl;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.trigger.AbstractPickStrategy;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.trigger.Picker;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Michael Jiang on 16/4/1.
 */
public class RoundRobinPicker extends AbstractPickStrategy {


    private static Logger LOG = LoggerFactory.getLogger(RoundRobinPicker.class);

    public RoundRobinPicker(ZKAccessor zkAccessor, TaskID taskID) {
        super(zkAccessor, taskID);
    }

    @Override
    public PickStrategy identify() {
        return PickStrategy.ROUND_ROBIN;
    }

    //todo
    @Override
    public SchedulerUnit assign() throws Exception {
        String taskNodePath = taskID.identify();
        List<String> children = zkAccessor.getChildren(taskNodePath);
        if (CollectionUtils.isEmpty(children)) {
            LOG.warn("not found available scheduler unit.");
            return null;
        }

        return null;
    }
}
