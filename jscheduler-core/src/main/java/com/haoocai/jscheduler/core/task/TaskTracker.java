package com.haoocai.jscheduler.core.task;

/**
 * Task Tracker
 * <p>
 * Every single task should have its own task tracker, they are one-to-one relationship.
 * Task tracker is responsible for calculation the task next run time point, and choose
 * one scheduler unit for the task.
 * </p>
 *
 * @author Michael Jiang on 16/4/5.
 */
public interface TaskTracker {

    void track();
}
