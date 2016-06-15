package com.haoocai.jscheduler.core.scheduler;

import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * @author Michael Jiang on 16/3/16.
 */
public class SchedulerUnit {
    private final String ip;
    private final int port;

    public SchedulerUnit(String ipAndPort) {
        String[] ipAndPortStr = ipAndPort.split(":");
        Validate.isTrue(InetAddressValidator.getInstance().isValid(ipAndPortStr[0]), "ip address is illegal");
        Validate.inclusiveBetween(1, 65535, Integer.parseInt(ipAndPortStr[0]));

        this.ip = ipAndPortStr[0];
        this.port = Integer.parseInt(ipAndPortStr[1]);
    }

    public SchedulerUnit(String ip, int port) {
        Validate.isTrue(InetAddressValidator.getInstance().isValid(ip), "ip address is illegal");
        Validate.inclusiveBetween(1, 65535, port);

        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String identify() {
        return ip + ":" + port;
    }

    @Override
    public String toString() {
        return "SchedulerUnit{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
