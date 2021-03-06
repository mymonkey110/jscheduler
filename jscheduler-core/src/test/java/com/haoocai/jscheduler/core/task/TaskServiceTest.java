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

package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.AbstractBaseTest;
import com.haoocai.jscheduler.core.exception.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.zk.ZKAccessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Michael Jiang on 16/5/27.
 */
public class TaskServiceTest extends AbstractBaseTest {
    @Mock
    private ZKAccessor zkAccessor;

    @InjectMocks
    private ZKTaskService taskManager;

    private final static String T_NAMESPACE = "test";
    private final static String T_APP = "t_app";
    private final static String T_TASK = "t_task";
    private final static String T_ID = "/" + T_NAMESPACE + "/" + T_APP + "/" + T_TASK;
    private final static String cronExpression = "0 1 * * * ?";
    private final static TaskID taskID = new TaskID(T_NAMESPACE, T_APP, T_TASK);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createWithNamespaceNotExist() throws Exception {
        when(zkAccessor.checkNodeExist(anyString())).thenReturn(false);

        try {
            taskManager.create(taskID, new Cron(cronExpression));
            fail("should report T_NAMESPACE not exist!");
        } catch (NamespaceNotExistException ignored) {

        }

        verify(zkAccessor).checkNodeExist(eq("/" + T_NAMESPACE));
        verify(zkAccessor, never()).checkNodeExist(eq("/" + T_NAMESPACE + "/" + T_APP));
    }


    @Test
    public void createWithAppNotExist() throws Exception {
        when(zkAccessor.checkNodeExist("/" + T_NAMESPACE)).thenReturn(true);
        when(zkAccessor.checkNodeExist("/" + T_NAMESPACE + "/" + T_APP)).thenReturn(false);

        try {
            taskManager.create(taskID, new Cron(cronExpression));
            fail("should report APP not exist!");
        } catch (AppNotFoundException ignored) {

        }

        verify(zkAccessor).checkNodeExist(eq("/" + T_NAMESPACE));
        verify(zkAccessor).checkNodeExist("/" + T_NAMESPACE + "/" + T_APP);
    }

    @Test
    public void create() throws Exception {
        when(zkAccessor.checkNodeExist("/" + T_NAMESPACE)).thenReturn(true);
        when(zkAccessor.checkNodeExist("/" + T_NAMESPACE + "/" + T_APP)).thenReturn(true);
        when(zkAccessor.checkNodeExist(T_ID)).thenReturn(false);
        doNothing().when(zkAccessor).create(T_ID + "/config/cronExpression", cronExpression.getBytes());

        taskManager.create(taskID, new Cron(cronExpression));

        verify(zkAccessor).checkNodeExist(eq("/" + T_NAMESPACE));
        verify(zkAccessor).checkNodeExist("/" + T_NAMESPACE + "/" + T_APP);
        verify(zkAccessor).checkNodeExist(T_ID);
        verify(zkAccessor).create(T_ID + "/config/cronExpression", cronExpression.getBytes());
    }

    @Test
    public void delete() throws Exception {
        TaskID taskID = new TaskID(T_NAMESPACE, T_APP, T_TASK);
        doNothing().when(zkAccessor).delete(T_ID);

        taskManager.delete(taskID);

        verify(zkAccessor).delete(T_ID);
    }

    @Test
    public void getAppTasks() throws Exception {

    }

    @Test
    public void getSpecTaskDescriptor() throws Exception {

    }

}