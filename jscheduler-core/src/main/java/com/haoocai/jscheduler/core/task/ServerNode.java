package com.haoocai.jscheduler.core.task;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.haoocai.jscheduler.core.scheduler.SchedulerUnit;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.List;

/**
 * Server Node
 *
 * @author Michael Jiang on 16/6/15.
 */
public class ServerNode {
    private final ZKAccessor zkAccessor;
    private final TaskID taskID;
    private final static String ROOT = "/servers";

    public ServerNode(ZKAccessor zkAccessor, TaskID taskID) {
        this.zkAccessor = zkAccessor;
        this.taskID = taskID;
    }

    //initialize server node
    public void init() {
        zkAccessor.create(taskID.identify() + "/servers", new byte[0]);
    }

    public List<SchedulerUnit> getTaskSchedulerUnits() {
        List<String> servers = zkAccessor.getChildren(taskID.identify() + ROOT);

        return Lists.newArrayList(Lists.transform(servers, new Function<String, SchedulerUnit>() {
            @Override
            public SchedulerUnit apply(String input) {
                return new SchedulerUnit(input);
            }
        }));
    }

    public static ServerNode load(ZKAccessor zkAccessor, TaskID taskID) {
        return new ServerNode(zkAccessor, taskID);
    }
}
