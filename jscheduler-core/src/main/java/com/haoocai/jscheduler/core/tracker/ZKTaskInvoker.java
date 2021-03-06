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

package com.haoocai.jscheduler.core.tracker;

import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task invoker with zookeeper implementation
 *
 * @author Michael Jiang on 16/3/16.
 */
class ZKTaskInvoker implements TaskInvoker {
    private final ZKAccessor zkAccessor;

    private static Logger LOG = LoggerFactory.getLogger(ZKTaskInvoker.class);

    ZKTaskInvoker(ZKAccessor zkAccessor) {
        this.zkAccessor = zkAccessor;
    }

    //todo add failure & success scenario handler
    @Override
    public void invoke(TaskID taskID, SchedulerUnit schedulerUnit) {
        checkNotNull(taskID);
        checkNotNull(schedulerUnit);

        try {
            zkAccessor.setData(taskID.identify() + "/servers/" + schedulerUnit.identify(), Long.toHexString(System.currentTimeMillis()).getBytes());
            LOG.info("invoke task:{} on scheduler unit:{} successfully", taskID, schedulerUnit);
            //todo success handler
        } catch (Exception e) {
            LOG.info("invoke error:{}.", e.getMessage(), e);
            //todo exception handler
        }
    }

}
