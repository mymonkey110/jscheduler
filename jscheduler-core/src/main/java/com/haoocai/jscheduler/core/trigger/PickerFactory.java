package com.haoocai.jscheduler.core.trigger;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.trigger.impl.RoundRobinPicker;
import com.haoocai.jscheduler.core.trigger.impl.RandomPicker;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

/**
 * @author Michael Jiang on 16/4/15.
 */
public class PickerFactory {

    public static Picker createPicker(TaskDescriptor taskDescriptor, ZKAccessor zkAccessor) {
        Preconditions.checkNotNull(zkAccessor);
        Preconditions.checkNotNull(taskDescriptor);
        switch (taskDescriptor.getPickStrategy()) {
            case RANDOM:
                return new RandomPicker(taskDescriptor, zkAccessor);
            case ROUND_ROBIN:
                return new RoundRobinPicker(taskDescriptor, zkAccessor);
            default:
                throw new RuntimeException("unknown pick strategy!");
        }
    }
}
