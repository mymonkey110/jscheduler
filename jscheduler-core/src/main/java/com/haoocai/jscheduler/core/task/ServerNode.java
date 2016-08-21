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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.List;

/**
 * Task Server Node
 *
 * @author Michael Jiang on 16/6/15.
 */
class ServerNode extends AbstractNode {
    private final static String ROOT = "/servers";

    private ServerNode(ZKAccessor zkAccessor, TaskID taskID) {
        super(zkAccessor, taskID);
    }

    static ServerNode load(ZKAccessor zkAccessor, TaskID taskID) {
        return new ServerNode(zkAccessor, taskID);
    }

    @Override
    NodeIdentify identify() {
        return NodeIdentify.SERVER;
    }

    //initialize server node
    public void init() {
        zkAccessor.create(taskID.identify() + "/servers", new byte[0]);
    }

    List<SchedulerUnit> getTaskSchedulerUnits() {
        List<String> servers = zkAccessor.getChildren(taskID.identify() + ROOT);

        return Lists.newArrayList(Lists.transform(servers, new Function<String, SchedulerUnit>() {
            @Override
            public SchedulerUnit apply(String input) {
                return new SchedulerUnit(input);
            }
        }));
    }

    boolean isSchedulerUnitExist(SchedulerUnit schedulerUnit) {
        return zkAccessor.checkNodeExist(taskID + ROOT + "/" + schedulerUnit.identify());
    }
}
