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

package com.haoocai.jscheduler.client.shared;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author Michael Jiang on 6/30/16.
 */
public class JvmIdentify {
    private final static int pid;
    private final static String ip;
    private final static String hostname;

    static {
        String processID = ManagementFactory.getRuntimeMXBean().getName();
        String[] pidAndMachineName = processID.split("@");
        pid = Integer.parseInt(pidAndMachineName[0]);
        hostname = pidAndMachineName[1];
        try {
            ip = getLocalIP();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("hostname:" + hostname);
            InetAddress localAddr = InetAddress.getLocalHost();
            System.out.println("Local IP:" + localAddr.getHostAddress());
            System.out.println("hostname:" + localAddr.getHostName());
            System.out.println("ID:"+id());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static String getLocalIP() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        String publicIpAddr = null;
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (networkInterface.isLoopback() || networkInterface.isPointToPoint() || !networkInterface.isUp()) {
                continue;
            }
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress inetAddress = addresses.nextElement();
                if (inetAddress.isSiteLocalAddress()) {
                    return inetAddress.getHostAddress();
                }
                if (isPublic(inetAddress)) {
                    publicIpAddr = inetAddress.getHostAddress();
                }
            }
        }
        if (publicIpAddr != null) {
            return publicIpAddr;
        }
        throw new RuntimeException("can't get local ip address");
    }

    private static boolean isPublic(final InetAddress ipAddress) {
        return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress();
    }

    public static String id() {
        return pid + "@" + ip;
    }


}
