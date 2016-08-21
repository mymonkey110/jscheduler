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

import com.haoocai.jscheduler.client.util.StringUtils;
import com.haoocai.jscheduler.client.util.Validate;
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
    private String connectStr;
    private final static int SLEEP = 1000;
    private final static int TRY_TIMES = 3;

    private static Logger LOG = LoggerFactory.getLogger(ZKClient.class);

    public ZKClient() {

    }

    public ZKClient(String connectStr) {
        Validate.checkArguments(StringUtils.isNotBlank(connectStr), "zookeeper connect string can't be blank");
        this.connectStr = connectStr;
    }

    public void init() {
        LOG.info("trying to connect to zookeeper:{}", connectStr);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(SLEEP, TRY_TIMES);
        client = CuratorFrameworkFactory.newClient(connectStr, retryPolicy);
        client.start();
        LOG.info("connected to zookeeper:{}", connectStr);
    }

    public void setConnectStr(String connectStr) {
        Validate.checkArguments(StringUtils.isNotBlank(connectStr), "zookeeper connect string can't be blank");
        this.connectStr = connectStr;
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
     * set data to specify node
     *
     * @param path node path
     * @param data new data
     */
    public void setData(String path, byte[] data) {
        try {
            client.setData().forPath(path, data);
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

    public boolean checkNodeExist(String path) throws ZKRuntimeException {
        try {
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    /**
     * delete the specify node
     *
     * @param path node absolute path
     */
    public void delete(String path) {
        try {
            client.delete().forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }
}
