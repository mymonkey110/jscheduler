package com.haoocai.jscheduler.core.scheduler.impl;

import com.haoocai.jscheduler.core.scheduler.SchedulerException;
import com.haoocai.jscheduler.core.scheduler.SchedulerManager;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael Jiang on 16/5/12.
 */
@Service
public class ZKSchedulerManager implements SchedulerManager {
    @Resource
    private ZKManager zkManager;

    @Override
    public List<String> getAppsUnderNamespace(String namespace) {
        try {
            return zkManager.getClient().getChildren().forPath("/" + namespace);
        } catch (Exception e) {
            throw new SchedulerException(e);
        }
    }
}
