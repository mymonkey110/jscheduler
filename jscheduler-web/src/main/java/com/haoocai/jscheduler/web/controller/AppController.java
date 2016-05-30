package com.haoocai.jscheduler.web.controller;

import com.haoocai.jscheduler.core.app.AppExistException;
import com.haoocai.jscheduler.core.app.AppService;
import com.haoocai.jscheduler.core.app.NamespaceNotExistException;
import com.haoocai.jscheduler.web.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * App Controller
 *
 * @author Michael Jiang on 16/5/30.
 */
@RestController
@RequestMapping(value = "/app")
public class AppController {
    @Autowired
    private AppService appService;


    @RequestMapping(value = "/list/{namespace}", method = RequestMethod.GET)
    public CommonResult getNamespaceApps(@PathVariable String namespace) {
        List<String> apps = appService.getNamespaceApps(namespace);
        return new CommonResult<>(apps);
    }

    @RequestMapping(value = "/create/{namespace}/{app}", method = RequestMethod.POST)
    public CommonResult create(@PathVariable String namespace, @PathVariable String app) {
        try {
            appService.create(namespace, app);
        } catch (NamespaceNotExistException e) {
            return CommonResult.errorOf(e);
        } catch (AppExistException e) {
            return CommonResult.errorOf(e);
        }
        return CommonResult.successRet();
    }

    @RequestMapping(value = "/delete/{namespace}/{app}", method = RequestMethod.DELETE)
    public CommonResult delete(@PathVariable String namespace, @PathVariable String app) {
        appService.delete(namespace, app);
        return CommonResult.successRet();
    }
}
