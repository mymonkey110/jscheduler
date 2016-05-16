package com.haoocai.jscheduler.core.task;

/**
 * @author Michael Jiang on 16/5/11.
 */
public class TaskID {
    private String namespace;
    private String app;
    private String name;

    TaskID(TaskDescriptor taskDescriptor) {
        this(taskDescriptor.getNamespace(), taskDescriptor.getApp(), taskDescriptor.getName());
    }

    private TaskID(String namespace, String app, String name) {
        this.namespace = namespace;
        this.app = app;
        this.name = name;
    }

    public String identify() {
        return "/" + namespace + "/" + app + "/" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskID)) return false;

        TaskID taskID = (TaskID) o;

        return namespace.equals(taskID.namespace) && app.equals(taskID.app) && name.equals(taskID.name);

    }

    public String getNamespace() {
        return namespace;
    }

    public String getApp() {
        return app;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + app.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaskID{" +
                "namespace='" + namespace + '\'' +
                ", app='" + app + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
