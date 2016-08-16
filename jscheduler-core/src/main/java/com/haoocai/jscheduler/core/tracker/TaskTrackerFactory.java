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

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.register.TaskRegisterCenter;
import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael Jiang on 16/5/11.
 */
public class TaskTrackerFactory {

    private static Map<TaskID, ZKTaskTracker> taskTrackerRegMap = new ConcurrentHashMap<>();

    public synchronized static ZKTaskTracker getTaskTracker(TaskID taskID, ZKAccessor zkAccessor) throws Exception {
        Preconditions.checkNotNull(taskID);
        Preconditions.checkNotNull(zkAccessor);

        Task task = TaskRegisterCenter.task(taskID);
        ZKTaskTracker taskTracker = taskTrackerRegMap.get(taskID);
        if (taskTracker == null) {
            taskTracker = new ZKTaskTracker(zkAccessor, task);
            taskTrackerRegMap.put(taskID, taskTracker);
        }

        return taskTracker;
    }
}
