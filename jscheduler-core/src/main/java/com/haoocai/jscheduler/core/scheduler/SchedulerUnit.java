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
