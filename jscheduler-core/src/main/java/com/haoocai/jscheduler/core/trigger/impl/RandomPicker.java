package com.haoocai.jscheduler.core.trigger.impl;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.trigger.Picker;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Random picker will random choose one server
 * to execute the job.
 *
 * @author Michael Jiang on 16/3/16.
 */
public class RandomPicker implements Picker {
    private ZKAccessor zkAccessor;
    private TaskDescriptor taskDescriptor;

    private static Logger LOG = LoggerFactory.getLogger(RandomPicker.class);
    private static Random random = new Random(System.currentTimeMillis());

    public RandomPicker(TaskDescriptor taskDescriptor, ZKAccessor zkAccessor) {
        this.zkAccessor = checkNotNull(zkAccessor);
        this.taskDescriptor = checkNotNull(taskDescriptor);
    }

    @Override
    public PickStrategy identify() {
        return PickStrategy.RANDOM;
    }

    @Override
    public SchedulerUnit assign() throws Exception {
        String taskNodePath = taskDescriptor.getApp() + "/" + taskDescriptor.getName();
        List<String> children = zkAccessor.getClient().getChildren().forPath(taskNodePath);
        if (CollectionUtils.isEmpty(children)) {
            LOG.warn("not found available scheduler unit.");
            return null;
        }

        String schedulerNode = children.get(random.nextInt(children.size()));
        String[] addr = schedulerNode.split(":");
        String ip = addr[0];
        int port = Integer.parseInt(addr[1]);

        return new SchedulerUnit(ip, port);
    }
}
