package com.haoocai.jscheduler.core.zk;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.haoocai.jscheduler.core.Constants.ID;
import static com.haoocai.jscheduler.core.Constants.VERSION;

/**
 * @author Michael Jiang on 16/3/16.
 */
@Repository
public class ZKManager {
    private static Logger LOG = LoggerFactory.getLogger(ZKManager.class);
    private ZooKeeper zk;
    private List<ACL> acl = new ArrayList<>();
    private boolean isCheckParentPath = true;

    private String rootPath;
    private String connectString;
    private String username;
    private String password;
    private int sessionTimeout;
    private String prefixPath;

    @PostConstruct
    public void init() throws Exception {
        connect();
        this.prefixPath = rootPath + "/" + ID + "/";
    }


    /**
     * reConnect to zookeeper
     *
     * @throws Exception
     */
    private synchronized void reConnection() throws Exception {
        if (this.zk != null) {
            this.zk.close();
            this.zk = null;
            this.connect();
        }
    }

    private void connect() throws Exception {
        createZookeeper();
    }

    private void createZookeeper() throws Exception {
        zk = new ZooKeeper(connectString, sessionTimeout,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        sessionEvent(event);
                    }
                });
        String authString = this.username + ":" + this.password;
        zk.addAuthInfo("digest", authString.getBytes());
        acl.clear();
        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest(authString))));
        acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
    }

    private void sessionEvent(WatchedEvent event) {
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
            LOG.info("Connect to zookeeper successfully!");
        } else if (event.getState() == Watcher.Event.KeeperState.Expired) {
            LOG.error("Connect timeout,try reConnect...");
            try {
                reConnection();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        } else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
            LOG.info("jscheduler disconnect with zookeeper,try reConnect...");
            try {
                reConnection();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        } else if (event.getState() == Watcher.Event.KeeperState.NoSyncConnected) {
            LOG.info("jscheduler NoSyncConnected，try reConnect...");
            try {
                reConnection();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        } else {
            LOG.info("jscheduler received unexpected status，event.getState() =" + event.getState() + ", event  value=" + event.toString());
        }
    }

    @PreDestroy
    public void close() throws InterruptedException {
        LOG.info("close connection with zookeeper.");
        if (zk == null) {
            return;
        }
        this.zk.close();
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setCheckParentPath(boolean checkParentPath) {
        isCheckParentPath = checkParentPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public boolean exist(String path) {
        Preconditions.checkArgument(StringUtils.isNotBlank(path), "path is blank");

        try {
            Stat stat = zk.exists(prefixPath + path, false);
            return stat != null;
        } catch (Exception e) {
            LOG.warn("judge path:{} exists error:{}.", path, e.getMessage(), e);
            throw new ZKRuntimeException(e);
        }
    }

    public void createNode(String path, Serializable data) throws ZKException {
        Preconditions.checkArgument(StringUtils.isNotBlank(path), "path is blank");
        Preconditions.checkNotNull(data);

        try {
            zk.create(prefixPath + path, SerializationUtils.serialize(data), acl, CreateMode.PERSISTENT);
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public void deleteNode(String path) {
        Preconditions.checkArgument(StringUtils.isNotBlank(path), "path is blank");

        try {
            zk.delete(prefixPath + path, -1);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public List<String> getNodeChildren(String path) {
        Preconditions.checkArgument(StringUtils.isNotBlank(path), "path is blank");

        try {
            return zk.getChildren(prefixPath + path, false);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public List<String> getAbsNodeChildrenWithRoot(String path) {
        try {
            return zk.getChildren(rootPath + "/" + path, false);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public <T> T getNodeData(String path, Class T) {
        Preconditions.checkArgument(StringUtils.isNotBlank(path), "path is blank");
        Preconditions.checkNotNull(T);

        try {
            byte[] data = zk.getData(prefixPath + path, false, new Stat());
            return SerializationUtils.deserialize(data);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    public void writeNodeData(String path, Serializable data) {
        Preconditions.checkArgument(StringUtils.isNotBlank(path), "path is blank");
        Preconditions.checkNotNull(data);

        try {
            zk.setData(prefixPath + path, SerializationUtils.serialize(data), -1);
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }

    private boolean checkZookeeperState() throws Exception {
        return zk != null && zk.getState() == ZooKeeper.States.CONNECTED;
    }

    public void initial() throws Exception {
        //当zk状态正常后才能调用
        if (zk.exists(rootPath, false) == null) {
            ZKTools.createPath(zk, rootPath, CreateMode.PERSISTENT, acl);
            if (isCheckParentPath) {
                checkParent(zk, rootPath);
            }
            //设置版本信息
            zk.setData(rootPath, VERSION.getBytes(), -1);
        } else {
            //先校验父亲节点，本身是否已经是schedule的目录
            if (isCheckParentPath) {
                checkParent(zk, rootPath);
            }
            byte[] value = zk.getData(rootPath, false, null);
            if (value == null) {
                zk.setData(rootPath, VERSION.getBytes(), -1);
            }
        }
    }

    private static void checkParent(ZooKeeper zk, String path) throws Exception {
        String[] list = path.split("/");
        String zkPath = "";
        for (int i = 0; i < list.length - 1; i++) {
            String str = list[i];
            if (!str.equals("")) {
                zkPath = zkPath + "/" + str;
                if (zk.exists(zkPath, false) != null) {
                    byte[] value = zk.getData(zkPath, false, null);
                    if (value != null) {
                        String tmpVersion = new String(value);
                        if (tmpVersion.contains("taobao-pamirs-schedule-")) {
                            throw new Exception("\"" + zkPath + "\"  is already a schedule instance's root directory, its any subdirectory cannot as the root directory of others");
                        }
                    }
                }
            }
        }
    }

    public ZooKeeper getZooKeeper() throws Exception {
        if (!this.checkZookeeperState()) {
            reConnection();
        }
        return this.zk;
    }
}
