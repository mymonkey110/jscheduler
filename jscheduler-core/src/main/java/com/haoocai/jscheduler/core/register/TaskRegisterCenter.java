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

package com.haoocai.jscheduler.core.register;

import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.task.TaskID;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Michael Jiang on 6/27/16.
 */
public class TaskRegisterCenter {
    private static ConcurrentHashMap<TaskID, Task> taskMap = new ConcurrentHashMap<>(8);

    public synchronized static void register(TaskID taskID, Task task) {
        if (!taskMap.containsKey(taskID)) {
            taskMap.put(taskID, task);
        }
    }

    public static Task task(TaskID taskID) {
        return taskMap.get(taskID);
    }

    public static boolean exist(TaskID taskID) {
        return taskMap.containsKey(taskID);
    }
}
