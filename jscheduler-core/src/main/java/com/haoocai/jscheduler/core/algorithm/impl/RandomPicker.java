package com.haoocai.jscheduler.core.algorithm.impl;

import com.haoocai.jscheduler.core.algorithm.AbstractPickStrategy;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * Random picker will random choose one server
 * to execute the job.
 *
 * @author Michael Jiang on 16/3/16.
 */
public class RandomPicker extends AbstractPickStrategy {

    private static Logger LOG = LoggerFactory.getLogger(RandomPicker.class);
    private static Random random = new Random(System.currentTimeMillis());

    public RandomPicker(ZKAccessor zkAccessor, Task task) {
        super(zkAccessor, task);
    }

    @Override
    public SchedulerUnit assign() throws Exception {
        List<SchedulerUnit> availableSchedulerUnits = task.getTaskSchedulerUnits();
        LOG.info("available scheduler units:{}", availableSchedulerUnits);

        if (CollectionUtils.isNotEmpty(availableSchedulerUnits)) {
            return availableSchedulerUnits.get(random.nextInt(availableSchedulerUnits.size()));
        } else {
            return null;
        }
    }
}
