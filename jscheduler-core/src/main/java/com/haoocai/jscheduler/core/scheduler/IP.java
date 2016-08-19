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
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * @author Michael Jiang on 6/29/16.
 */
class IP implements ValueObject<IP> {
    private static final long serialVersionUID = 9181381716191870964L;

    private final String ip;

    IP(String ip) {
        //Validate.isTrue(InetAddressValidator.getInstance().isValid(ip), "ip address is illegal");
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
