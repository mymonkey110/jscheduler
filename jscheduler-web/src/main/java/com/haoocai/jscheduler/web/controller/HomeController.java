package com.haoocai.jscheduler.web.controller;

import com.haoocai.jscheduler.core.ErrorCode;
import com.haoocai.jscheduler.core.app.AppService;
import com.haoocai.jscheduler.web.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Michael Jiang on 16/5/24.
 */
@Controller
public class HomeController {

    private final AppService appService;

    @Autowired
    public HomeController(AppService appService) {
        this.appService = appService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dashboard() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/getApp/{namespace}", method = RequestMethod.GET)
    public CommonResult getNamespaceApps(@PathVariable String namespace) {
        List<String> apps = appService.getNamespaceApps(namespace);
        return new CommonResult<>(ErrorCode.SUCCESS, apps);
    }
}
