package com.haoocai.jscheduler.core.app;

import com.haoocai.jscheduler.core.exception.AppExistException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;

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
    List<String> getNamespaceApps(String namespace);

    /**
     * create app within namespace
     *
     * @param namespace namespace
     * @param app       app
     */
    void create(String namespace, String app) throws NamespaceNotExistException, AppExistException;


    /**
     * delete app
     *
     * @param namespace namespace
     * @param app       app
     */
    void delete(String namespace, String app);
}
