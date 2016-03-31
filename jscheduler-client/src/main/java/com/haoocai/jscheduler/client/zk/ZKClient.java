package com.haoocai.jscheduler.client.zk;

import com.haoocai.jscheduler.client.util.StringUtils;
import com.haoocai.jscheduler.client.util.Validate;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.haoocai.jscheduler.client.Constants.ID;

/**
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public class ZKClient {
    private ZooKeeper zooKeeper;

    private final String connectStr;
    private final String username;
    private final String password;
    private final int sessionTimeout;
    private final String prefixPath;
    private List<ACL> acl = new ArrayList<ACL>();

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

    public void createNode(String path, Watcher watcher) throws ZKException {
        try {
            zooKeeper.create(prefixPath + path, "ready".getBytes(), acl, CreateMode.EPHEMERAL);
        } catch (KeeperException | InterruptedException e) {
            throw new ZKException(e);
        }
    }
}
