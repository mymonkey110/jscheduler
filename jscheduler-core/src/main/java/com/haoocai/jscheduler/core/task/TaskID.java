package com.haoocai.jscheduler.core.task;

import com.haoocai.jscheduler.core.shared.ValueObject;

/**
 * @author Michael Jiang on 16/5/11.
 */
public class TaskID implements ValueObject<TaskID> {
    private static final long serialVersionUID = -8736193223436228464L;

    private String namespace;
    private String app;
    private String name;

    public TaskID(TaskDescriptor taskDescriptor) {
        this(taskDescriptor.getNamespace(), taskDescriptor.getApp(), taskDescriptor.getName());
    }

    public TaskID(String namespace, String app, String name) {
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

    @Override
    public boolean isSame(TaskID other) {
        return other != null && this.namespace.equals(other.namespace) && this.app.equals(other.app) && this.name.equals(other.name);
    }
}
