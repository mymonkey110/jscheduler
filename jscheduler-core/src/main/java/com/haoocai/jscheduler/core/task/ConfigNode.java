package com.haoocai.jscheduler.core.task;

import com.google.common.base.Preconditions;
import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.trigger.PickStrategy;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import static com.haoocai.jscheduler.core.Constants.UTF8_CHARSET;

/**
 * Task Configuration
 *
 * @author Michael Jiang on 16/6/15.
 */
class ConfigNode {
    private final ZKAccessor zkAccessor;
    private final TaskID taskID;
    private String cronExpression;
    private PickStrategy pickStrategy;

    private static String ROOT = "/config";
    private static String CRON_EXPRESSION = ROOT + "/cron";
    private static String PICK_STRATEGY = ROOT + "/strategy";

    ConfigNode(ZKAccessor zkAccessor, TaskID taskID, String cronExpression) {
        this(zkAccessor, taskID, cronExpression, PickStrategy.RANDOM);
    }

    ConfigNode(ZKAccessor zkAccessor, TaskID taskID, String cronExpression, PickStrategy pickStrategy) {
        this.zkAccessor = zkAccessor;
        this.taskID = taskID;
        this.cronExpression = cronExpression;
        this.pickStrategy = pickStrategy;
    }

    //initialize config node
    public void init() {
        zkAccessor.create(taskID.identify() + ROOT, new byte[0]);
        zkAccessor.create(taskID.identify() + CRON_EXPRESSION, cronExpression.getBytes(UTF8_CHARSET));
        zkAccessor.create(taskID.identify() + PICK_STRATEGY, cronExpression.getBytes(UTF8_CHARSET));
    }

    public static ConfigNode load(ZKAccessor zkAccessor, TaskID taskID) {
        String cronExpr = new String(zkAccessor.getData(taskID.identify() + CRON_EXPRESSION), UTF8_CHARSET);
        PickStrategy pickStrategy = PickStrategy.valueOf(new String(zkAccessor.getData(taskID.identify() + PICK_STRATEGY), UTF8_CHARSET));
        return new ConfigNode(zkAccessor, taskID, cronExpr, pickStrategy);
    }

    public void setCronExpr(String cronExpression) {
        Preconditions.checkState(CronExpression.isValidExpression(cronExpression));

        this.cronExpression = cronExpression;
        zkAccessor.setData(taskID.identify() + CRON_EXPRESSION, cronExpression.getBytes(UTF8_CHARSET));
    }

    public void setPickStrategy(PickStrategy pickStrategy) {
        this.pickStrategy = pickStrategy;

        zkAccessor.setData(taskID.identify() + PICK_STRATEGY, pickStrategy.toString().getBytes(UTF8_CHARSET));
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public PickStrategy getPickStrategy() {
        return pickStrategy;
    }

    @Override
    public String toString() {
        return "ConfigNode{" +
                "taskID=" + taskID +
                ", cronExpression='" + cronExpression + '\'' +
                ", pickStrategy=" + pickStrategy +
                '}';
    }
}
