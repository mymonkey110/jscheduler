package com.haoocai.jscheduler.web.controller;

import com.haoocai.jscheduler.web.CommonResult;
import com.haoocai.jscheduler.core.scheduler.SchedulerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Jiang on 16/5/12.
 */
@RestController("/connect")
public class ConnectorController {
    @Autowired
    private SchedulerManager zkSchedulerManager;

    @RequestMapping(value = "/new.do", method = RequestMethod.POST)
    public CommonResult newConnection(@RequestParam String connectStr, @RequestParam String namespace) {
        return null;
    }
}
