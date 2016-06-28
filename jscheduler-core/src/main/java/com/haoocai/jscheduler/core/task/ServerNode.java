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
class ServerNode extends AbstractNode {
    private final static String ROOT = "/servers";

    private ServerNode(ZKAccessor zkAccessor, TaskID taskID) {
        super(zkAccessor, taskID);
    }

    @Override
    NodeIdentify identify() {
        return NodeIdentify.SERVER;
    }

    //initialize server node
    public void init() {
        zkAccessor.create(taskID.identify() + "/servers", new byte[0]);
    }

    List<SchedulerUnit> getTaskSchedulerUnits() {
        List<String> servers = zkAccessor.getChildren(taskID.identify() + ROOT);

        return Lists.newArrayList(Lists.transform(servers, new Function<String, SchedulerUnit>() {
            @Override
            public SchedulerUnit apply(String input) {
                return new SchedulerUnit(input);
            }
        }));
    }

    static ServerNode load(ZKAccessor zkAccessor, TaskID taskID) {
        return new ServerNode(zkAccessor, taskID);
    }
}
