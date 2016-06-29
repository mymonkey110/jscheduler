package com.haoocai.jscheduler.core.algorithm;

import com.haoocai.jscheduler.core.algorithm.impl.RandomPicker;
import com.haoocai.jscheduler.core.algorithm.impl.RoundRobinPicker;
import com.haoocai.jscheduler.core.task.Task;
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
     * @param zkAccessor zkAccessor
     * @param task       task
     * @return specified picker algorithm
     */
    public static Picker createPicker(ZKAccessor zkAccessor, Task task) {
        checkNotNull(zkAccessor);
        checkNotNull(task);

        switch (task.getPickStrategy()) {
            case RANDOM:
                return new RandomPicker(zkAccessor, task);
            case ROUND_ROBIN:
                return new RoundRobinPicker(zkAccessor, task);
            default:
                throw new IllegalArgumentException("unknown pick strategy");
        }
    }
}
