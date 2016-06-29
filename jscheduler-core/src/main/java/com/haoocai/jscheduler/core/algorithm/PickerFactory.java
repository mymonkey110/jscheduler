package com.haoocai.jscheduler.core.algorithm;

import com.haoocai.jscheduler.core.algorithm.impl.RandomPicker;
import com.haoocai.jscheduler.core.algorithm.impl.RoundRobinPicker;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Picker Factory
 *
 * @author Michael Jiang on 6/29/16.
 */
public class PickerFactory {
    /**
     * create specified picker algorithm by pick strategy
     *
     * @param zkAccessor   zkAccessor
     * @param taskID       task id
     * @param pickStrategy pick strategy
     * @return specified picker algorithm
     */
    public static Picker createPicker(ZKAccessor zkAccessor, TaskID taskID, PickStrategy pickStrategy) {
        checkNotNull(zkAccessor);
        checkNotNull(taskID);

        switch (pickStrategy) {
            case RANDOM:
                return new RandomPicker(zkAccessor, taskID);
            case ROUND_ROBIN:
                return new RoundRobinPicker(zkAccessor, taskID);
            default:
                throw new IllegalArgumentException("unknown pick strategy");
        }
    }
}
