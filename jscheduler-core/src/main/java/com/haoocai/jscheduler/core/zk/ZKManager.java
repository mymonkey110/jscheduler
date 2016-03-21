package com.haoocai.jscheduler.core.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
@Repository
public class ZKManager {
    private static Logger log = LoggerFactory.getLogger(ZKManager.class);
    private ZooKeeper zk;
    private List<ACL> acl = new ArrayList<ACL>();
    private boolean isCheckParentPath = true;

    private String rootPath;
    private String connectString;
    private String username;
    private String password;
    private int sessionTimeout;

    public ZKManager() throws Exception {
        this.connect();
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

    /**
     * 重连zookeeper
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
        CountDownLatch connectionLatch = new CountDownLatch(1);
        createZookeeper(connectionLatch);
        connectionLatch.await(10, TimeUnit.SECONDS);
    }

    private void createZookeeper(final CountDownLatch connectionLatch) throws Exception {
        zk = new ZooKeeper(connectString, sessionTimeout,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        sessionEvent(connectionLatch, event);
                    }
                });
        String authString = this.username + ":" + this.password;
        zk.addAuthInfo("digest", authString.getBytes());
        acl.clear();
        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest(authString))));
        acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
    }

    private void sessionEvent(CountDownLatch connectionLatch, WatchedEvent event) {
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
            log.info("收到ZK连接成功事件！");
            connectionLatch.countDown();
        } else if (event.getState() == Watcher.Event.KeeperState.Expired) {
            log.error("会话超时，等待重新建立ZK连接...");
            try {
                reConnection();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } // Disconnected：Zookeeper会自动处理Disconnected状态重连
        else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
            log.info("tb_hj_schedule Disconnected，等待重新建立ZK连接...");
            try {
                reConnection();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else if (event.getState() == Watcher.Event.KeeperState.NoSyncConnected) {
            log.info("tb_hj_schedule NoSyncConnected，等待重新建立ZK连接...");
            try {
                reConnection();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.info("tb_hj_schedule 会话有其他状态的值，event.getState() =" + event.getState() + ", event  value=" + event.toString());
            connectionLatch.countDown();
        }
    }

    public void close() throws InterruptedException {
        log.info("关闭zookeeper连接");
        if (zk == null) {
            return;
        }
        this.zk.close();
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setCheckParentPath(boolean checkParentPath) {
        isCheckParentPath = checkParentPath;
    }


    public boolean checkZookeeperState() throws Exception {
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
            zk.setData(rootPath, Version.getVersion().getBytes(), -1);
        } else {
            //先校验父亲节点，本身是否已经是schedule的目录
            if (isCheckParentPath) {
                checkParent(zk, rootPath);
            }
            byte[] value = zk.getData(rootPath, false, null);
            if (value == null) {
                zk.setData(rootPath, Version.getVersion().getBytes(), -1);
            } else {
                String dataVersion = new String(value);
                if (!Version.isCompatible(dataVersion)) {
                    throw new Exception("TBSchedule程序版本 " + Version.getVersion() + " 不兼容Zookeeper中的数据版本 " + dataVersion);
                }
                log.info("当前的程序版本:" + Version.getVersion() + " 数据版本: " + dataVersion);
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

    public List<ACL> getAcl() {
        return acl;
    }

    public ZooKeeper getZooKeeper() throws Exception {
        if (!this.checkZookeeperState()) {
            reConnection();
        }
        return this.zk;
    }
}
