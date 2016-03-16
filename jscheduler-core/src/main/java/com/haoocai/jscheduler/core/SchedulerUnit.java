package com.haoocai.jscheduler.core;

import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public class SchedulerUnit {
    private String ip;
    private int port;

    public SchedulerUnit(String ip, int port) {
        Validate.isTrue(InetAddressValidator.getInstance().isValid(ip), "ip address is illegal");
        Validate.inclusiveBetween(0, 65535L, port);

        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "SchedulerUnit{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
