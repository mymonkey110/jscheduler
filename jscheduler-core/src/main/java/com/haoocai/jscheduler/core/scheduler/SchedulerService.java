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

package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.task.TaskID;

import java.util.List;

/**
 * Scheduler Service
 * <p>
 * Scheduler service is responsible for scheduling the time task of all system.
 * When it started, it should watch every task running, and invoke the task by reserved the
 * pick strategy.
 * </p>
 *
 * @author Michael Jiang on 16/3/16.
 */
public interface SchedulerService {
    /**
     * Start the specified task
     *
     * @param taskID task identify
     */
    void startTask(TaskID taskID);

    /**
     * stop the specified task
     *
     * @param taskID task identify
     */
    void stopTask(TaskID taskID);

}
