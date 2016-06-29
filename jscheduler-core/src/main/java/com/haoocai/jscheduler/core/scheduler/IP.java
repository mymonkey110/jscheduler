package com.haoocai.jscheduler.core.scheduler;

import com.haoocai.jscheduler.core.shared.ValueObject;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * @author Michael Jiang on 6/29/16.
 */
class IP implements ValueObject<IP> {
    private static final long serialVersionUID = 9181381716191870964L;

    private final String ip;

    IP(String ip) {
        Validate.isTrue(InetAddressValidator.getInstance().isValid(ip), "ip address is illegal");
        this.ip = ip;
    }

    String ipStr() {
        return this.ip;
    }

    @Override
    public boolean isSame(IP other) {
        return other != null && this.ip.equals(other.ipStr());
    }

    @Override
    public String toString() {
        return "IP{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
