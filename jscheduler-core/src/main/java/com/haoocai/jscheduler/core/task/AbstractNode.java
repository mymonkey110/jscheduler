package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.zk.ZKAccessor;

import static com.haoocai.jscheduler.core.task.AbstractNode.Persist.FALSE;
import static com.haoocai.jscheduler.core.task.AbstractNode.Persist.TRUE;

/**
 * Abstraction Node
 *
 * @author Michael Jiang on 6/28/16.
 */
abstract class AbstractNode {
    protected ZKAccessor zkAccessor;

    protected TaskID taskID;

    abstract NodeIdentify identify();

    abstract void init();

    AbstractNode(ZKAccessor zkAccessor, TaskID taskID) {
        this.zkAccessor = zkAccessor;
        this.taskID = taskID;
    }

    enum Persist {
        TRUE,
        FALSE
    }

    enum NodeIdentify {
        CONFIG("/config", TRUE),
        SERVER("/servers", TRUE),
        STATUS("/status", FALSE);

        private String root;
        private Persist persist;

        NodeIdentify(String root, Persist persist) {
            this.root = root;
            this.persist = persist;
        }

        public String getRoot() {
            return root;
        }

        public Persist getPersist() {
            return persist;
        }
    }

    public boolean isPersist() {
        return identify().getPersist() == Persist.TRUE;
    }

}
