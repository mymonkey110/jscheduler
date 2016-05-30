package com.haoocai.jscheduler.core.app.impl;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.app.AppExistException;
import com.haoocai.jscheduler.core.app.AppService;
import com.haoocai.jscheduler.core.app.NamespaceNotExistException;
import com.haoocai.jscheduler.core.zk.ZKManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * App Service implementation
 *
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
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace));

        return zkManager.getChildren("/" + namespace);
    }

    @Override
    public void create(String namespace, String app) throws NamespaceNotExistException, AppExistException {
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(app));

        if (!zkManager.checkNodeExist("/" + namespace)) {
            throw new NamespaceNotExistException();
        }

        if (zkManager.checkNodeExist("/" + namespace + "/" + app)) {
            throw new AppExistException();
        }

        zkManager.create("/" + namespace + "/" + app, new byte[0]);
    }

    @Override
    public void delete(String namespace, String app) {
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(app));

        zkManager.delete("/" + namespace + "/" + app);
    }
}
