package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.shared.ValueObject;
import com.haoocai.jscheduler.core.algorithm.PickStrategy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Michael Jiang on 6/27/16.
 */
class TaskConfig implements ValueObject<TaskConfig> {
    private static final long serialVersionUID = 3395721189822664147L;

    private Cron cron;
    private PickStrategy pickStrategy;

    public TaskConfig(Cron cron, PickStrategy pickStrategy) {
        this.cron = checkNotNull(cron);
        this.pickStrategy = checkNotNull(pickStrategy);
    }

    public Cron getCron() {
        return cron;
    }

    public PickStrategy getPickStrategy() {
        return pickStrategy;
    }

    @Override
    public boolean isSame(TaskConfig other) {
        return false;
    }

    @Override
    public String toString() {
        return "TaskConfig{" +
                "cron=" + cron +
                ", pickStrategy=" + pickStrategy +
                '}';
    }
}
