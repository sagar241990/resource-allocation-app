package com.sag.res_allocation_app.resource_allocation_app.util;

/**
 * Custom exception for file parsing errors
 */
public class FileParsingException extends Exception {

    public FileParsingException(String message) {
        super(message);
    }

    public FileParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
