package com.haoocai.jscheduler.core.scheduler;

import java.util.List;

/**
 * @author Michael Jiang on 16/5/12.
 */
public interface SchedulerManager {
    List<String> getAppsUnderNamespace(String namespace);
}
