package com.haoocai.jscheduler.web.controller;

import com.haoocai.jscheduler.core.ErrorCode;
import com.haoocai.jscheduler.core.exception.AbstractCheckedException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.task.TaskDescriptor;
import com.haoocai.jscheduler.core.app.AppNotFoundException;
import com.haoocai.jscheduler.core.task.TaskExistException;
import com.haoocai.jscheduler.core.task.TaskID;
import com.haoocai.jscheduler.core.task.TaskManager;
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
    private final TaskManager taskManager;

    private static Logger LOG = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    public TaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @RequestMapping(value = "/list/{namespace}/{app}", method = RequestMethod.GET)
    public CommonResult listTask(@PathVariable String namespace,
                                 @PathVariable String app) {
        List<TaskDescriptor> taskDescriptorList = taskManager.getAppTasks(namespace, app);
        return new CommonResult<>(taskDescriptorList);
    }

    @RequestMapping(value = "/create/{namespace}/{app}/{name}", method = RequestMethod.POST)
    public CommonResult createTask(@PathVariable String namespace,
                                   @PathVariable String app,
                                   @PathVariable String name,
                                   @RequestParam String cron) {
        try {
            taskManager.create(namespace, app, name, cron);
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
        TaskDescriptor taskDescriptor = taskManager.getSpecTaskDescriptor(new TaskID(namespace, app, name));
        return new CommonResult<>(taskDescriptor);
    }

    @RequestMapping(value = "/delete/{namespace}/{app}/{name}", method = RequestMethod.DELETE)
    public CommonResult deleteTask(@PathVariable String namespace,
                                   @PathVariable String app,
                                   @PathVariable String name) {
        taskManager.delete(new TaskID(namespace, app, name));
        return CommonResult.successRet();
    }
}
