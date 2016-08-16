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

package com.haoocai.jscheduler.client.task;

import com.haoocai.jscheduler.client.util.SerializationUtils;
import com.haoocai.jscheduler.client.zk.ZKClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.haoocai.jscheduler.client.util.Validate.checkNotNull;

/**
 * Task Watcher
 *
 * @author Michael Jiang on 16/3/31.
 */
class TaskWatcher implements Watcher {
    private final ZKClient zkClient;
    private final Task task;
    private final String pathPrefix;

    private static Logger LOG = LoggerFactory.getLogger(TaskWatcher.class);

    TaskWatcher(ZKClient zkClient, Task task, String pathPrefix) {
        this.zkClient = checkNotNull(zkClient);
        this.task = checkNotNull(task);
        this.pathPrefix = pathPrefix;
    }

    public void start() {
        zkClient.addListener(pathPrefix + "/" + task.name(), this);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDataChanged) {
            byte[] data = zkClient.getData(event.getPath());
            try {
                SchedulerContext schedulerContext = SerializationUtils.deserialize(data);
                task.run(schedulerContext);
            } catch (Exception e) {
                LOG.error("process task error:{}.", e.getMessage(), e);
            }
        } else {
            LOG.error("unexpected event type:{}.", event.getType());
        }
    }
}
