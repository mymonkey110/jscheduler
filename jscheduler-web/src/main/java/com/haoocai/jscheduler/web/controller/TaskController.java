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

import com.haoocai.jscheduler.core.algorithm.PickStrategy;
import com.haoocai.jscheduler.core.exception.AbstractCheckedException;
import com.haoocai.jscheduler.core.task.Cron;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.TaskService;
import com.haoocai.jscheduler.web.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Michael Jiang on 16/4/1.
 */
@RestController
@RequestMapping("/task")
class TaskController {
    private final TaskService taskService;

    private static Logger LOG = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/list/{namespace}/{app}", method = RequestMethod.GET)
    public CommonResult listTask(@PathVariable String namespace,
                                 @PathVariable String app) {
        List<TaskDescriptor> taskDescriptorList = taskService.getAppTasks(namespace, app);
        return new CommonResult<>(taskDescriptorList);
    }

    @RequestMapping(value = "/create/{namespace}/{app}/{name}", method = RequestMethod.POST)
    public CommonResult createTask(@PathVariable String namespace,
                                   @PathVariable String app,
                                   @PathVariable String name,
                                   @RequestParam String cron) {
        try {
            taskService.create(new TaskID(namespace, app, name), new Cron(cron));
            return CommonResult.successRet();
        } catch (AbstractCheckedException e) {
            LOG.error("create task error,namespace:{} app:{} name:{},code:{},error:{}.", namespace, app, name, e.code(), e);
            return CommonResult.errorOf(e);
        }
    }

    @RequestMapping(value = "/get/{namespace}/{app}/{name}", method = RequestMethod.GET)
    public CommonResult getTask(@PathVariable String namespace,
                                @PathVariable String app,
                                @PathVariable String name) {
        TaskDescriptor taskDescriptor = taskService.getSpecTask(new TaskID(namespace, app, name));
        return new CommonResult<>(taskDescriptor);
    }

    @RequestMapping(value = "/delete/{namespace}/{app}/{name}", method = RequestMethod.DELETE)
    public CommonResult deleteTask(@PathVariable String namespace,
                                   @PathVariable String app,
                                   @PathVariable String name) {
        taskService.delete(new TaskID(namespace, app, name));
        return CommonResult.successRet();
    }

    @RequestMapping(value = "/update/{namespace}/{app}/{name}", method = RequestMethod.PUT)
    public CommonResult updateConfig(@PathVariable String namespace,
                                     @PathVariable String app,
                                     @PathVariable String name,
                                     @RequestParam String cron,
                                     @RequestParam PickStrategy pickStrategy) {
        TaskID taskID = new TaskID(namespace, app, name);
        if (pickStrategy == null || !Cron.isValid(cron)) {
            return CommonResult.errorOf(AbstractCheckedException.ErrorCode.PARAM_ERROR);
        }

        taskService.updateConfig(taskID, new Cron(cron), pickStrategy);
        return CommonResult.successRet();
    }
}
