package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.AbstractBaseTest;
import com.haoocai.jscheduler.core.app.AppNotFoundException;
import com.haoocai.jscheduler.core.exception.NamespaceNotExistException;
import com.haoocai.jscheduler.core.task.impl.ZKTaskManager;
import com.haoocai.jscheduler.core.zk.ZKManager;
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
public class TaskManagerTest extends AbstractBaseTest {
    @Mock
    private ZKManager zkManager;

    @InjectMocks
    private ZKTaskManager taskManager;

    private final static String T_NAMESPACE = "test";
    private final static String T_APP = "t_app";
    private final static String T_TASK = "t_task";
    private final static String T_ID = "/" + T_NAMESPACE + "/" + T_APP + "/" + T_TASK;
    private final static String cronExpression = "0 1 * * * ?";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createWithNamespaceNotExist() throws Exception {
        when(zkManager.checkNodeExist(anyString())).thenReturn(false);

        try {
            taskManager.create(T_NAMESPACE, T_APP, T_TASK, cronExpression);
            fail("should report T_NAMESPACE not exist!");
        } catch (NamespaceNotExistException ignored) {

        }

        verify(zkManager).checkNodeExist(eq("/" + T_NAMESPACE));
        verify(zkManager, never()).checkNodeExist(eq("/" + T_NAMESPACE + "/" + T_APP));
    }


    @Test
    public void createWithAppNotExist() throws Exception {
        when(zkManager.checkNodeExist("/" + T_NAMESPACE)).thenReturn(true);
        when(zkManager.checkNodeExist("/" + T_NAMESPACE + "/" + T_APP)).thenReturn(false);

        try {
            taskManager.create(T_NAMESPACE, T_APP, T_TASK, cronExpression);
            fail("should report APP not exist!");
        } catch (AppNotFoundException ignored) {

        }

        verify(zkManager).checkNodeExist(eq("/" + T_NAMESPACE));
        verify(zkManager).checkNodeExist("/" + T_NAMESPACE + "/" + T_APP);
    }

    @Test
    public void create() throws Exception {
        when(zkManager.checkNodeExist("/" + T_NAMESPACE)).thenReturn(true);
        when(zkManager.checkNodeExist("/" + T_NAMESPACE + "/" + T_APP)).thenReturn(true);
        when(zkManager.checkNodeExist(T_ID)).thenReturn(false);
        doNothing().when(zkManager).create(T_ID + "/config/cronExpression", cronExpression.getBytes());

        taskManager.create(T_NAMESPACE, T_APP, T_TASK, cronExpression);

        verify(zkManager).checkNodeExist(eq("/" + T_NAMESPACE));
        verify(zkManager).checkNodeExist("/" + T_NAMESPACE + "/" + T_APP);
        verify(zkManager).checkNodeExist(T_ID);
        verify(zkManager).create(T_ID + "/config/cronExpression", cronExpression.getBytes());
    }

    @Test
    public void delete() throws Exception {
        TaskID taskID = new TaskID(T_NAMESPACE, T_APP, T_TASK);
        doNothing().when(zkManager).delete(T_ID);

        taskManager.delete(taskID);

        verify(zkManager).delete(T_ID);
    }

    @Test
    public void getAppTasks() throws Exception {

    }

    @Test
    public void getSpecTaskDescriptor() throws Exception {

    }

}