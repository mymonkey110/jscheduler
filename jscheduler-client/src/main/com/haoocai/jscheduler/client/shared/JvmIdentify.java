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
    public final static int pid;
    public final static String ip;
    public final static String hostname;

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
                if (inetAddress.isAnyLocalAddress()) {
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
