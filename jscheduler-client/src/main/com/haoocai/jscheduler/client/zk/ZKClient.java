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
