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
