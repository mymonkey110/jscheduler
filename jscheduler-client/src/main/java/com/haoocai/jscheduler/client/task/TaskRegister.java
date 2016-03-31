package com.haoocai.jscheduler.client.task;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * task register
 *
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public class TaskRegister {
    private final Map<String, Method> taskMethodMap = new ConcurrentHashMap<String, Method>();

    public static void register(Class<? extends Job> jobClazz) {

    }

    public static <T> void registerInstance(T instance) {

    }
}
