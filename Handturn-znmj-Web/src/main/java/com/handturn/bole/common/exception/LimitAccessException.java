package com.handturn.bole.common.exception;

/**
 * 限流异常
 *
 * @author Eric
 */
public class LimitAccessException extends Exception {

    private static final long serialVersionUID = -3608667856397125671L;

    public LimitAccessException(String message) {
        super(message);
    }
}