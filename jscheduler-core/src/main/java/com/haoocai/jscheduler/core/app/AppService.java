package com.haoocai.jscheduler.core.app;

import java.util.List;

/**
 * App Service
 *
 * @author Michael Jiang on 16/5/24.
 */
public interface AppService {
    /**
     * get apps' name within namespace
     *
     * @param namespace namespace
     * @return apps' name
     */
    public List<String> getNamespaceApps(String namespace);
}
