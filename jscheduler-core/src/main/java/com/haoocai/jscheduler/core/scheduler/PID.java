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
