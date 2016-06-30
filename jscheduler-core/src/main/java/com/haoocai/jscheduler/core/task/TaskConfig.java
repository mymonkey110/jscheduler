/*
 * Copyright 2016  Michael Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
