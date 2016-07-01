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

import com.haoocai.jscheduler.core.zk.ZKAccessor;

/**
 * Task Status Node, Ephemeral Node
 *
 * @author Michael Jiang on 6/28/16.
 */
class StatusNode extends AbstractNode {
    private final static String ROOT = "/status";

    private StatusNode(ZKAccessor zkAccessor, TaskID taskID) {
        super(zkAccessor, taskID);
    }

    @Override
    NodeIdentify identify() {
        return NodeIdentify.STATUS;
    }

    @Override
    void init() {
        zkAccessor.createEphemeralNode(taskID.identify() + "/status", "RUNNING".getBytes());
    }

    boolean isRunning() {
        return zkAccessor.checkNodeExist(taskID.identify() + ROOT);
    }

    void delete() {
        zkAccessor.delete(taskID.identify() + "/status");
    }

    static StatusNode load(ZKAccessor zkAccessor, TaskID taskID) {
        return new StatusNode(zkAccessor, taskID);
    }
}
