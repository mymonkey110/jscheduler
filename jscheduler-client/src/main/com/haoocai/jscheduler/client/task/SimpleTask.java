package com.haoocai.jscheduler.client.task;

/**
 * @author Michael Jiang on 6/30/16.
 */
abstract class SimpleTask {
    private String namespace;
    private String app;

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setApp(String app) {
        this.app = app;
    }

    String taskPath() {
        return "/" + namespace + "/" + app + "/" + name();
    }

    abstract String name();

    abstract void run(SchedulerContext context);
}
