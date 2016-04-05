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
import java.util.Random;

/**
 * Random picker will random choose one server
 * to execute the job.
 *
 * @author Michael Jiang on 16/3/16.
 */
@Component("randomPicker")
class RandomPicker implements Picker {
    @Autowired
    private ZKManager zkManager;

    private static Logger LOG = LoggerFactory.getLogger(RandomPicker.class);
    private static Random random = new Random(System.currentTimeMillis());

    @Override
    public PickStrategy identify() {
        return PickStrategy.RANDOM;
    }

    @Override
    public void init(TaskDescriptor taskDescriptor) {
        //do nothing
    }

    @Override
    public SchedulerUnit assign(TaskDescriptor taskDescriptor) {
        String taskNodePath = taskDescriptor.getApp() + "/" + taskDescriptor.getName();
        List<String> children = zkManager.getNodeChildren(taskNodePath);
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
