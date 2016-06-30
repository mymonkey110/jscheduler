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

import com.haoocai.jscheduler.core.exception.AppExistException;
import com.haoocai.jscheduler.core.app.AppService;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
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
