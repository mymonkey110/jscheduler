package com.haoocai.jscheduler.client.util;

/**
 * @author Michael Jiang on 16/3/31.
 */
public class Validate {
    public static <T> T checkNotNull(T o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return o;
    }

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
