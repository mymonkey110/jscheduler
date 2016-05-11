package com.haoocai.jscheduler.core.task;

/**
 * @author Michael Jiang on 16/5/11.
 */
public class TaskID {
    private String namespace;
    private String app;
    private String path;

    public TaskID(TaskDescriptor taskDescriptor) {
        this(taskDescriptor.getNamespace(), taskDescriptor.getApp(), taskDescriptor.getName());
    }

    private TaskID(String namespace, String app, String path) {
        this.namespace = namespace;
        this.app = app;
        this.path = path;
    }

    public String identify() {
        return "/" + namespace + "/" + app + "/" + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskID)) return false;

        TaskID taskID = (TaskID) o;

        return namespace.equals(taskID.namespace) && app.equals(taskID.app) && path.equals(taskID.path);

    }

    @Override
    public int hashCode() {
        int result = namespace.hashCode();
        result = 31 * result + app.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaskID{" +
                "namespace='" + namespace + '\'' +
                ", app='" + app + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
