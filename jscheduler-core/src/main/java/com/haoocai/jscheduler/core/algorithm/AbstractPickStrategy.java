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

import com.haoocai.jscheduler.core.task.Task;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

/**
 * @author Michael Jiang on 16/6/15.
 */
public abstract class AbstractPickStrategy implements Picker{
    protected final ZKAccessor zkAccessor;
    protected final Task task;

    public AbstractPickStrategy(ZKAccessor zkAccessor, Task task) {
        this.zkAccessor = zkAccessor;
        this.task = task;
    }
}
