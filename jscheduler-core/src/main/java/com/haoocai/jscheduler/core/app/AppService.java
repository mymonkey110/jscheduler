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
