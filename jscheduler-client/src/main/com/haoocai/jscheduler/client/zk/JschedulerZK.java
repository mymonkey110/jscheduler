package com.haoocai.jscheduler.client.zk;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * @author mymonkey110@gmail.com on 16/4/1.
 */
public class JschedulerZK extends ZooKeeper {

    public JschedulerZK(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
        super(connectString, sessionTimeout, watcher);
    }

    public SocketAddress getLocalConnectedAddr() {
        return null;
    }
}
