package com.haoocai.jscheduler.core.app.impl;

import com.haoocai.jscheduler.core.app.AppService;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Michael Jiang on 16/5/24.
 */
@Service
public class AppServiceImpl implements AppService {
    private final ZKManager zkManager;

    @Autowired
    public AppServiceImpl(ZKManager zkManager) {
        this.zkManager = zkManager;
    }

    @Override
    public List<String> getNamespaceApps(String namespace) {
        try {
            return zkManager.getClient().getChildren().forPath("/" + namespace);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
