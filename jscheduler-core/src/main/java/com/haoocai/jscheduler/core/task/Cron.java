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

import com.haoocai.jscheduler.core.util.CronExpression;
import com.haoocai.jscheduler.core.shared.ValueObject;

import java.text.ParseException;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Cron Expression Value Object
 *
 * @author Michael Jiang on 6/27/16.
 */
public final class Cron implements ValueObject<Cron> {
    private static final long serialVersionUID = -7826568233626564535L;

    private String cron;
    private CronExpression cexp;

    public Cron(String cron) {
        checkArgument(CronExpression.isValidExpression(cron));

        this.cron = cron;
        try {
            this.cexp = new CronExpression(cron);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String cron() {
        return this.cron;
    }

    /**
     * calculate the task next run time point
     *
     * @return next run time point
     */
    Date calcNextRunTime() {
        return cexp.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
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
