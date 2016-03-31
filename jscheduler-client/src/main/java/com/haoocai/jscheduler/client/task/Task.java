package com.haoocai.jscheduler.client.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Job Description annotation
 *
 * @author mymonkey110@gmail.com on 16/3/31.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Task {
    /**
     * Job name
     */
    String name() default "";

    /**
     * Job execution expire time.
     */
    long expireTime() default 1000L;
}
