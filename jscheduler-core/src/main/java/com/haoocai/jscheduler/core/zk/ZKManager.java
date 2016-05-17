package com.haoocai.jscheduler.core.zk;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.JschedulerConfig;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
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
    @Resource
    private JschedulerConfig jschedulerConfig;
    private CuratorFramework client;

    private static Logger LOG = LoggerFactory.getLogger(ZKManager.class);

    @PostConstruct
    public void init() throws Exception {
        String zkConnectStr = jschedulerConfig.getConnectStr();
        LOG.trace("trying to connect to zookeeper:{}.", zkConnectStr);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkConnectStr, retryPolicy);
        LOG.info("connected to zookeeper:{}.", zkConnectStr);
    }



    public CuratorFramework getClient() {
        return this.client;
    }
}
