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

package com.haoocai.jscheduler.core.shared;

/**
 * @author Michael Jiang on 16/5/16.
 */
public class ErrorCode {
    public final static int SUCCESS = 1000;

    public final static int PARAM_ERROR = 1001;
    public final static int ILLEGAL_REQUEST = 1002;
    public final static int SYS_ERROR = 1003;

    public final static int NAMESPACE_NOT_FOUND = 2001;
    public final static int NAMESPACE_ALREADY_EXIST = 2002;
    public final static int APP_NOT_FOUND = 2003;
    public final static int APP_ALREADY_EXIST = 2004;
    public final static int TASK_NOT_FOUND = 2005;
    public final static int TASK_ALREADY_EXIST = 2006;

    public final static int ZK_ERROR = 3001;
    public final static int NODE_NOT_EXIST = 3002;
    public final static int NODE_ALREADY_EXIST = 3003;
}
