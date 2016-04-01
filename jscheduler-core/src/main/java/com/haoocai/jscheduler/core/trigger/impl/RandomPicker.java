package com.haoocai.jscheduler.core.trigger.impl;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.trigger.Picker;
import org.springframework.stereotype.Service;

/**
 * Random picker will random choose one server
 * to execute the job.
 *
 * @author Michael Jiang on 16/3/16.
 */
@Service
public class RandomPicker implements Picker {


    @Override
    public SchedulerUnit assign(String taskName) {
        return null;
    }
}
