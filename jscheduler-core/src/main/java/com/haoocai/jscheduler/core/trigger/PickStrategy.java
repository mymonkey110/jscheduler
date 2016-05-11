package com.haoocai.jscheduler.core.trigger;

/**
 * @author Michael Jiang on 16/4/1.
 */
public enum PickStrategy {
    RANDOM("randomPicker"),

    ROUND_ROBIN("rrPicker");

    private String identify;

    PickStrategy(String identify) {
        this.identify = identify;
    }

    public String getIdentify() {
        return identify;
    }
}
