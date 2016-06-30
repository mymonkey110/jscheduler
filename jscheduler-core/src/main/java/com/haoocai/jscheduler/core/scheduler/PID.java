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

package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.client.util.Validate;
import com.haoocai.jscheduler.core.shared.ValueObject;

/**
 * @author Michael Jiang on 6/29/16.
 */
class PID implements ValueObject<PID> {
    private static final long serialVersionUID = -6253931176198767274L;

    private final int pid;

    PID(int pid) {
        Validate.checkArguments(pid > 0, "pid must be greater than 0");
        this.pid = pid;
    }

    int getPid() {
        return pid;
    }

    @Override
    public boolean isSame(PID other) {
        return other != null && this.pid == other.getPid();
    }

    @Override
    public String toString() {
        return "PID{" +
                "pid=" + pid +
                '}';
    }
}
