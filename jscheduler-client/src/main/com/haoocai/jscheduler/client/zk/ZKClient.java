package com.haoocai.jscheduler.client.zk;

import com.haoocai.jscheduler.client.task.TriggerHandler;
import com.haoocai.jscheduler.client.util.Validate;
import com.haoocai.jscheduler.client.util.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.haoocai.jscheduler.client.Constants.ID;

/**
 * @author Michael Jiang on 16/3/31.
 */
public class ZKClient {
    private ZooKeeper zooKeeper;

    private final String connectStr;
    private final String username;
    private final String password;
    private final int sessionTimeout;
    private final String prefixPath;
    private List<ACL> acl = new ArrayList<>();
    private SocketAddress localSocketAddress;

    private static Logger LOG = LoggerFactory.getLogger(ZKClient.class);

    public ZKClient(String root, String connectStr, String username, String password, int sessionTimeout) throws Exception {
        Validate.checkArguments(StringUtils.isNotBlank(root), "root is blank");
        Validate.checkArguments(StringUtils.isNotBlank(connectStr), "connect str is blank");
        Validate.checkArguments(StringUtils.isNotBlank(username), "username is blank");
        Validate.checkArguments(StringUtils.isNotBlank(password), "password is blank");
        Validate.checkArguments(sessionTimeout > 0, "session must > 0");

        this.connectStr = connectStr;
        this.username = username;
        this.password = password;
        this.sessionTimeout = sessionTimeout;
        this.prefixPath = root + "/" + ID;
        connect();
    }

    /**
     * reConnect to zookeeper
     *
     * @throws Exception
     */
    private synchronized void reConnection() throws Exception {
        if (this.zooKeeper != null) {
            this.zooKeeper.close();
            this.zooKeeper = null;
            this.connect();
        }
    }

    private synchronized void connect() throws IOException, NoSuchAlgorithmException {
        zooKeeper = new ZooKeeper(connectStr, sessionTimeout,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                            LOG.info("Connect to zookeeper successfully!");
                        } else {
                            LOG.info("jscheduler received unexpected status，event.getState() =" + event.getState() + ", event  value=" + event.toString());
                        }
                    }
                });

        String authString = this.username + ":" + this.password;
        zooKeeper.addAuthInfo("digest", authString.getBytes());
        acl.clear();
        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest(authString))));
        acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
    }

    private void sessionEvent(WatchedEvent event) {
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
            LOG.info("Connect to zookeeper successfully!");
        } else {
            LOG.info("jscheduler received unexpected status，event.getState() =" + event.getState() + ", event  value=" + event.toString());
        }
    }

    public void createListenNode(String path) throws Exception {
        zooKeeper.create(prefixPath + path, "ready".getBytes(), acl, CreateMode.EPHEMERAL);
    }

    public void createNode(String path, boolean ephemeral) throws ZKException {
        try {
            if (ephemeral) {
                zooKeeper.create(prefixPath + path, "ready".getBytes(), acl, CreateMode.EPHEMERAL);
            } else {
                zooKeeper.create(prefixPath + path, "ready".getBytes(), acl, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new ZKException(e);
        }
    }


    //FIXME need to judge event type
    public void listenNodeOnDateChange(final String path, final TriggerHandler triggerHandler) throws ZKException {
        try {
            byte[] data = zooKeeper.getData(prefixPath + path, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    LOG.debug("zk path:{} receive event:{}.", event.getPath(), event);
                }
            }, new Stat());
            triggerHandler.handler(data);
        } catch (KeeperException | InterruptedException e) {
            throw new ZKException(e);
        }
    }

    //TODO get zookeeper client local socket address
    public String clientIdentify() {
        if (!zooKeeper.getState().isConnected()) {
            throw new RuntimeException("zookeeper is not connected!");
        }

        return "ip:port";
    }

}
