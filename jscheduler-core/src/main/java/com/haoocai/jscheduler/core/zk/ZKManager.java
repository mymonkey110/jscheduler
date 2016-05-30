package com.haoocai.jscheduler.core.zk;

import com.haoocai.jscheduler.core.JschedulerConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Michael Jiang on 16/3/16.
 */
@Repository
public class ZKManager {
    private final JschedulerConfig jschedulerConfig;

    private CuratorFramework client;

    private static Logger LOG = LoggerFactory.getLogger(ZKManager.class);

    @Autowired
    public ZKManager(JschedulerConfig jschedulerConfig) {
        this.jschedulerConfig = jschedulerConfig;
    }

    @PostConstruct
    public void init() throws Exception {
        String zkConnectStr = jschedulerConfig.getConnectStr();
        LOG.trace("trying to connect to zookeeper:{}.", zkConnectStr);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkConnectStr, retryPolicy);
        client.start();
        LOG.info("connected to zookeeper:{}.", zkConnectStr);
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

    public void create(String path, byte[] content) throws ZKRuntimeException {
        try {
            client.create().forPath(path, content);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public void delete(String path) throws ZKRuntimeException {
        try {
            client.create().forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public List<String> getChildren(String path) throws ZKRuntimeException {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }
}
