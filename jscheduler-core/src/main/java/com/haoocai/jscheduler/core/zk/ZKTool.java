package com.haoocai.jscheduler.core.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.util.List;

public class ZKTool {
    static void createPath(ZooKeeper zk, String path, CreateMode createMode, List<ACL> acl) throws Exception {
        String[] list = path.split("/");
        String zkPath = "";
        for (String str : list) {
            if (!str.equals("")) {
                zkPath = zkPath + "/" + str;
                if (zk.exists(zkPath, false) == null) {
                    zk.create(zkPath, null, acl, createMode);
                }
            }
        }
    }

    public static boolean checkNodeExist(CuratorFramework curator, String path) throws ZKRuntimeException {
        try {
            return curator.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new ZKRuntimeException(e);
        }
    }
}
