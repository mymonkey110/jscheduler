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

package com.haoocai.jscheduler.client.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zookeeper Visitor Client
 *
 * @author Michael Jiang on 16/3/31.
 */
public class ZKClient {

    private CuratorFramework client;

    private static Logger LOG = LoggerFactory.getLogger(ZKClient.class);

    ZKClient(String connectStr) {
        this(connectStr, 1000, 3);
    }

    ZKClient(String connectStr, int sleep, int tryTimes) {
        LOG.info("trying to connect to zookeeper:{}", connectStr);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(sleep, tryTimes);
        client = CuratorFrameworkFactory.newClient(connectStr, retryPolicy);
        client.start();
        LOG.info("connected to zookeeper:{}", connectStr);
    }

    public void addListener(String path, Watcher watcher) {
        try {
            client.getData().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public byte[] getData(String path) {
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    /**
     * create ephemeral node with specify path
     *
     * @param path    node absolute path
     * @param content node data
     */
    public void createEphemeralNode(String path, byte[] content) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path, content);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }
}
