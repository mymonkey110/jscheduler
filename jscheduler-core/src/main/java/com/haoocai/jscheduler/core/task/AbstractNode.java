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

import static com.haoocai.jscheduler.core.task.AbstractNode.Persist.FALSE;
import static com.haoocai.jscheduler.core.task.AbstractNode.Persist.TRUE;

/**
 * Abstraction Node
 *
 * @author Michael Jiang on 6/28/16.
 */
abstract class AbstractNode {
    protected ZKAccessor zkAccessor;

    protected TaskID taskID;

    abstract NodeIdentify identify();

    abstract void init();

    AbstractNode(ZKAccessor zkAccessor, TaskID taskID) {
        this.zkAccessor = zkAccessor;
        this.taskID = taskID;
    }

    enum Persist {
        TRUE,
        FALSE
    }

    enum NodeIdentify {
        CONFIG("/config", TRUE),
        SERVER("/servers", TRUE),
        STATUS("/status", FALSE);

        private String root;
        private Persist persist;

        NodeIdentify(String root, Persist persist) {
            this.root = root;
            this.persist = persist;
        }

        public String getRoot() {
            return root;
        }

        public Persist getPersist() {
            return persist;
        }
    }

    public boolean isPersist() {
        return identify().getPersist() == Persist.TRUE;
    }

}
