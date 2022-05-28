package com.code4j.exception;

/**
 * @author liu_wp
 * @date 2020/11/17
 * @see
 */
public class Code4jException extends RuntimeException {
    public Code4jException(final String message) {
        super(message);
    }

    public Code4jException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
