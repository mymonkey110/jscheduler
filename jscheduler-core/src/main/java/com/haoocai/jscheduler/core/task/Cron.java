package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.CronExpression;
import com.haoocai.jscheduler.core.shared.ValueObject;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Michael Jiang on 6/27/16.
 */
public class Cron implements ValueObject<Cron> {
    private static final long serialVersionUID = -7826568233626564535L;

    private String cron;

    public Cron(String cron) {
        checkArgument(CronExpression.isValidExpression(cron));

        this.cron = cron;
    }

    public String cron() {
        return this.cron;
    }

    @Override
    public boolean isSame(Cron other) {
        return false;
    }

    @Override
    public String toString() {
        return "Cron{" +
                "cron='" + cron + '\'' +
                '}';
    }
}
