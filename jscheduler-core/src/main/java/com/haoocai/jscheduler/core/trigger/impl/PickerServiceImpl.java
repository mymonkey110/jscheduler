package com.haoocai.jscheduler.core.trigger.impl;

import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.trigger.PickerService;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Michael Jiang on 16/5/11.
 */
@Service
public class PickerServiceImpl implements PickerService {
    @Resource
    private ZKAccessor zkAccessor;

    @Override
    public void setPickStrategy(TaskDescriptor taskDescriptor, PickStrategy pickStrategy) {
        String taskPath = taskDescriptor.taskPath();
        try {
            zkAccessor.getClient().setData().forPath(taskPath+"/config/pickStrategy",pickStrategy.getIdentify().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PickStrategy getTaskPickerStrategy(TaskDescriptor taskDescriptor) {
        return null;
    }
}
