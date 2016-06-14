package com.haoocai.jscheduler.core.zk;

import com.haoocai.jscheduler.core.JschedulerConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
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
     * @throws ZKRuntimeException
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
     * @throws ZKRuntimeException
     */
    public void delete(String path) {
        try {
            client.create().forPath(path);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    /**
     * get the specify node children
     *
     * @param path node absolute path
     * @return children
     * @throws ZKRuntimeException
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
}
