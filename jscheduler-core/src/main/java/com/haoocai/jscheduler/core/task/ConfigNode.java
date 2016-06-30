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

import com.haoocai.jscheduler.core.algorithm.PickStrategy;
import com.haoocai.jscheduler.core.zk.ZKAccessor;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.haoocai.jscheduler.core.shared.Constants.UTF8_CHARSET;

/**
 * Task Configuration Node
 *
 * @author Michael Jiang on 16/6/15.
 */
class ConfigNode extends AbstractNode {
    private Cron cron;
    private PickStrategy pickStrategy;

    private final static String ROOT = NodeIdentify.CONFIG.getRoot();
    private final static String CRON_EXPRESSION = ROOT + "/cron";
    private final static String PICK_STRATEGY = ROOT + "/strategy";

    private ConfigNode(ZKAccessor zkAccessor, TaskID taskID) {
        super(zkAccessor, taskID);
    }

    ConfigNode(ZKAccessor zkAccessor,TaskID taskID,Cron cron,PickStrategy pickStrategy) {
        super(zkAccessor,taskID);
        this.cron = checkNotNull(cron);
        this.pickStrategy = checkNotNull(pickStrategy);
    }

    @Override
    NodeIdentify identify() {
        return NodeIdentify.CONFIG;
    }

    //initialize config node
    @Override
    void init() {
        zkAccessor.create(taskID.identify() + ROOT, new byte[0]);
        zkAccessor.create(taskID.identify() + CRON_EXPRESSION, cron.cron().getBytes(UTF8_CHARSET));
        zkAccessor.create(taskID.identify() + PICK_STRATEGY, pickStrategy.toString().getBytes(UTF8_CHARSET));
    }

    static ConfigNode load(ZKAccessor zkAccessor, TaskID taskID) {
        String cronExpr = new String(zkAccessor.getData(taskID.identify() + CRON_EXPRESSION), UTF8_CHARSET);
        PickStrategy pickStrategy = PickStrategy.valueOf(new String(zkAccessor.getData(taskID.identify() + PICK_STRATEGY), UTF8_CHARSET));
        ConfigNode configNode = new ConfigNode(zkAccessor, taskID);
        configNode.setCron(new Cron(cronExpr));
        configNode.setPickStrategy(pickStrategy);
        return configNode;
    }

    public void setCron(Cron cron) {
        this.cron = cron;
    }

    void changeCron(Cron cron) {
        this.cron = checkNotNull(cron);
        zkAccessor.setData(taskID.identify() + CRON_EXPRESSION, cron.cron().getBytes(UTF8_CHARSET));
    }

    private void setPickStrategy(PickStrategy pickStrategy) {
        this.pickStrategy = pickStrategy;

        zkAccessor.setData(taskID.identify() + PICK_STRATEGY, pickStrategy.toString().getBytes(UTF8_CHARSET));
    }

    public Cron getCron() {
        return this.cron;
    }

    Date calcNextRunTime() {
        return cron.calcNextRunTime();
    }

    PickStrategy getPickStrategy() {
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
