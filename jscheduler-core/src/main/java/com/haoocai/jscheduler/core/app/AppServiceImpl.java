package com.haoocai.jscheduler.core.app;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.exception.AppExistException;
import com.haoocai.jscheduler.core.app.AppService;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
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
    private final ZKAccessor zkAccessor;

    @Autowired
    public AppServiceImpl(ZKAccessor zkAccessor) {
        this.zkAccessor = zkAccessor;
    }

    @Override
    public List<String> getNamespaceApps(String namespace) {
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace));

        return zkAccessor.getChildren("/" + namespace);
    }

    @Override
    public void create(String namespace, String app) throws NamespaceNotExistException, AppExistException {
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(app));

        if (!zkAccessor.checkNodeExist("/" + namespace)) {
            throw new NamespaceNotExistException();
        }

        if (zkAccessor.checkNodeExist("/" + namespace + "/" + app)) {
            throw new AppExistException();
        }

        zkAccessor.create("/" + namespace + "/" + app, new byte[0]);
    }

    @Override
    public void delete(String namespace, String app) {
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace) && StringUtils.isNotBlank(app));

        zkAccessor.delete("/" + namespace + "/" + app);
    }
}
