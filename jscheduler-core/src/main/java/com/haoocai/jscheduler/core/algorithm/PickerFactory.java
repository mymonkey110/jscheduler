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

package com.haoocai.jscheduler.core.algorithm;

import com.haoocai.jscheduler.core.algorithm.impl.RandomPicker;
import com.haoocai.jscheduler.core.algorithm.impl.RoundRobinPicker;
import com.haoocai.jscheduler.core.task.Task;
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
