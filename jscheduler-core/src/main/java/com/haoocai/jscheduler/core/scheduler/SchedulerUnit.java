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

import com.haoocai.jscheduler.core.shared.ValueObject;

/**
 * @author Michael Jiang on 16/3/16.
 */
public class SchedulerUnit implements ValueObject<SchedulerUnit> {
    private static final long serialVersionUID = 8131416066677376958L;
    private final IP ip;
    private final PID pid;

    public SchedulerUnit(String pidAndIP) {
        String[] pidAndIPStr = pidAndIP.split("@");

        this.pid = new PID(Integer.parseInt(pidAndIPStr[0]));
        this.ip = new IP(pidAndIPStr[1]);
    }

    public IP getIp() {
        return ip;
    }

    public PID getPid() {
        return pid;
    }

    public String identify() {
        return pid.getPid() + "@" + ip.ipStr();
    }

    @Override
    public boolean isSame(SchedulerUnit other) {
        return other != null && this.ip.isSame(other.getIp()) && this.pid.isSame(other.getPid());
    }

    @Override
    public String toString() {
        return "SchedulerUnit{" +
                "ip=" + ip.ipStr() +
                ", pid=" + pid.getPid() +
                '}';
    }
}
