package com.haoocai.jscheduler.core.trigger;

import com.haoocai.jscheduler.core.task.TaskDescriptor;

/**
 * @author Michael Jiang on 16/5/11.
 */
public interface PickerService {

    void setPickStrategy(TaskDescriptor taskDescriptor, PickStrategy pickStrategy);

    PickStrategy getTaskPickerStrategy(TaskDescriptor taskDescriptor);
}
