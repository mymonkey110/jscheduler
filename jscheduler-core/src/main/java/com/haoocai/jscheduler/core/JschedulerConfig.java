package com.haoocai.jscheduler.core;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public class JschedulerConfig {
    private String rootPath;
    private String zkAddress;
    private String zkUserName;
    private String zkPassword;
    private long timeout;

    public JschedulerConfig(String rootPath, String zkAddress, String zkUserName, String zkPassword, long timeout) {
        this.rootPath = rootPath;
        this.zkAddress = zkAddress;
        this.zkUserName = zkUserName;
        this.zkPassword = zkPassword;
        this.timeout = timeout;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public String getZkUserName() {
        return zkUserName;
    }

    public String getZkPassword() {
        return zkPassword;
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "JschedulerConfig{" +
                "rootPath='" + rootPath + '\'' +
                ", zkAddress='" + zkAddress + '\'' +
                ", zkUserName='" + zkUserName + '\'' +
                ", zkPassword='" + zkPassword + '\'' +
                ", timeout=" + timeout +
                '}';
    }
}
