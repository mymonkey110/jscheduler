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

package com.haoocai.jscheduler.core.zk;

import com.haoocai.jscheduler.core.shared.JschedulerConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.haoocai.jscheduler.core.shared.Constants.UTF8_CHARSET;

/**
 * @author Michael Jiang on 16/3/16.
 */
@Repository
public class ZKAccessor {
    private final JschedulerConfig jschedulerConfig;

    private CuratorFramework client;

    private static Logger LOG = LoggerFactory.getLogger(ZKAccessor.class);

    @Autowired
    public ZKAccessor(JschedulerConfig jschedulerConfig) {
        this.jschedulerConfig = jschedulerConfig;
    }

    @PostConstruct
    public void init() throws Exception {
        String zkConnectStr = jschedulerConfig.getConnectStr();
        LOG.info("trying to connect to zookeeper:{}", zkConnectStr);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkConnectStr, retryPolicy);
        client.start();
        LOG.info("connected to zookeeper:{}", zkConnectStr);
    }

    public CuratorFramework getClient() {
        return this.client;
    }

    public boolean checkNodeExist(String path) throws ZKRuntimeException {
        try {
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    /**
     * create permanent node with specify path
     *
     * @param path    node absolute path
     * @param content node data
     */
    public void create(String path, byte[] content) {
        try {
            client.create().forPath(path, content);
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

    /**
     * create permanent node with parent path
     * <p>
     * If parent path is not exist, automatic create it.
     * </p>
     *
     * @param parentPath parent path
     * @param node       node name
     * @param content    node data
     */
    public void mkdirAndCreate(String parentPath, String node, byte[] content) {
        try {
            ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), parentPath);
            create(parentPath + "/" + node, content);
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

    /**
     * recursively delete path
     *
     * @param path node absolute path
     */
    public void deleteRecursive(String path) {
        try {
            ZKPaths.deleteChildren(client.getZookeeperClient().getZooKeeper(), path, true);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    /**
     * get the specify node children
     *
     * @param path node absolute path
     * @return children
     */
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    /**
     * get the specify node data
     *
     * @param path node absolute path
     * @return node data
     */
    public byte[] getData(String path) {
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public void setClient(CuratorFramework client) {
        client.getData().
    }

    /**
     * get the specify node data, wrap into utf8 string
     * {@link #getData}
     * @param path node absolute path
     * @return string data
     */
    public String getDataStr(String path) {
        return new String(getData(path), UTF8_CHARSET);
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

    public void addDataListener(String path, Watcher watcher) {
        try {
            client.getData().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }
}
