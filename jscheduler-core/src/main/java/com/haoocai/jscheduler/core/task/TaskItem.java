package com.haoocai.jscheduler.core.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mymonkey110@gmail.com on 16/3/16.
 */
public class TaskItem implements Serializable {
    private static final long serialVersionUID = 5224933468107682449L;

    private String name;

    private String cronExpression;

    private Map extraParams = new HashMap();

    public TaskItem(String name, String cronExpression) {

    }
}
