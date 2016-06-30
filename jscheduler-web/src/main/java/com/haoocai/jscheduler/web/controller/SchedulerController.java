/*
 * Copyright 2016  Michael Jiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haoocai.jscheduler.web.controller;

import com.haoocai.jscheduler.core.scheduler.SchedulerService;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.web.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Jiang on 16/6/14.
 */
@RestController
@RequestMapping("/schedule")
public class SchedulerController {
    @Autowired
    private SchedulerService schedulerService;

    /*@RequestMapping(value = "/info/{namespace}/{app}/{task}", method = RequestMethod.GET)
    public CommonResult monitorTask(@PathVariable String namespace,
                                    @PathVariable String app,
                                    @PathVariable String task) {

    }*/

    @RequestMapping(value = "/start/{namespace}/{app}/{task}", method = RequestMethod.GET)
    public CommonResult startTask(@PathVariable String namespace,
                                  @PathVariable String app,
                                  @PathVariable String task) {
        TaskID taskID = new TaskID(namespace, app, task);
        schedulerService.startTask(taskID);

        return CommonResult.successRet();
    }

    @RequestMapping(value = "/stop/{namespace}/{app}/{task}", method = RequestMethod.GET)
    public CommonResult stopTask(@PathVariable String namespace,
                                 @PathVariable String app,
                                 @PathVariable String task) {
        TaskID taskID = new TaskID(namespace, app, task);
        schedulerService.stopTask(taskID);

        return CommonResult.successRet();
    }


}
