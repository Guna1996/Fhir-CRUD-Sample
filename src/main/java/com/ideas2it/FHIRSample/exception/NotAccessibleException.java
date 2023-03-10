/**
 * <p>
 * This is the base package for all the Exception classes.
 * </p>
 * Copyright 2022 - Ideas2it
 */
package com.ideas2it.FHIRSample.exception;

/**
 * <p>
 * This is an custom exception class and it Extends
 * RuntimeException class
 * </p>
 *
 * @author Gunaseelan k
 * @since 2022-10-10
 */
public class NotAccessibleException extends RuntimeException {

    /**
     * <p>
     * This method is used to throw customized error message
     * </p>
     *
     * @param message {@link String} is error message
     */
    public NotAccessibleException(String message) {
        super(message);
    }
}

