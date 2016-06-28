package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.haoocai.jscheduler.core.shared.Constants.UTF8_CHARSET;

/**
 * Task Configuration
 *
 * @author Michael Jiang on 16/6/15.
 */
class ConfigNode {
    private final ZKAccessor zkAccessor;
    private final TaskID taskID;
    private Cron cron;
    private PickStrategy pickStrategy;

    private static String ROOT = "/config";
    private static String CRON_EXPRESSION = ROOT + "/cron";
    private static String PICK_STRATEGY = ROOT + "/strategy";

    ConfigNode(ZKAccessor zkAccessor, TaskID taskID, Cron cron) {
        this(zkAccessor, taskID, cron, PickStrategy.RANDOM);
    }

    ConfigNode(ZKAccessor zkAccessor, TaskID taskID, Cron cron, PickStrategy pickStrategy) {
        this.zkAccessor = zkAccessor;
        this.taskID = taskID;
        this.cron = cron;
        this.pickStrategy = pickStrategy;
    }

    //initialize config node
    void init() {
        zkAccessor.create(taskID.identify() + ROOT, new byte[0]);
        zkAccessor.create(taskID.identify() + CRON_EXPRESSION, cron.cron().getBytes(UTF8_CHARSET));
        zkAccessor.create(taskID.identify() + PICK_STRATEGY, pickStrategy.toString().getBytes(UTF8_CHARSET));
    }

    public static ConfigNode load(ZKAccessor zkAccessor, TaskID taskID) {
        String cronExpr = new String(zkAccessor.getData(taskID.identify() + CRON_EXPRESSION), UTF8_CHARSET);
        PickStrategy pickStrategy = PickStrategy.valueOf(new String(zkAccessor.getData(taskID.identify() + PICK_STRATEGY), UTF8_CHARSET));
        return new ConfigNode(zkAccessor, taskID, new Cron(cronExpr), pickStrategy);
    }

    public void changeCron(Cron cron) {
        this.cron = checkNotNull(cron);
        zkAccessor.setData(taskID.identify() + CRON_EXPRESSION, cron.cron().getBytes(UTF8_CHARSET));
    }

    public void setPickStrategy(PickStrategy pickStrategy) {
        this.pickStrategy = pickStrategy;

        zkAccessor.setData(taskID.identify() + PICK_STRATEGY, pickStrategy.toString().getBytes(UTF8_CHARSET));
    }

    public Cron getCron() {
        return this.cron;
    }

    public Date calcNextRunTime() {
        return cron.calcNextRunTime();
    }

    public PickStrategy getPickStrategy() {
        return pickStrategy;
    }

    @Override
    public String toString() {
        return "ConfigNode{" +
                "taskID=" + taskID +
                ", cron=" + cron +
                ", pickStrategy=" + pickStrategy +
                '}';
    }
}
