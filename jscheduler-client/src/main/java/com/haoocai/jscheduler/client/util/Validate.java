package com.haoocai.jscheduler.client.util;

/**
 * @author mymonkey110@gmail.com on 16/3/31.
 */
public class Validate {
    public static void checkArguments(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArguments(boolean condition, String errorMsg) {
        if (!condition) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
