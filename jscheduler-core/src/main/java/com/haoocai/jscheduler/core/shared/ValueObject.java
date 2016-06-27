package com.haoocai.jscheduler.core.shared;

import java.io.Serializable;

/**
 * @author Michael Jiang on 6/27/16.
 */
public interface ValueObject<T> extends Serializable {
    boolean isSame(T other);
}
