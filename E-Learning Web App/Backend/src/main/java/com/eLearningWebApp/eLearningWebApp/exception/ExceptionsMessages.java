package com.eLearningWebApp.eLearningWebApp.exception;

/**
 * Custom exception class that extends RuntimeException.
 * This is used to throw custom error messages throughout the application.
 */
public class ExceptionsMessages extends RuntimeException {

    /**
     * Constructor that accepts a custom error message.
     * @param message The custom error message to be passed to the exception.
     */
    public ExceptionsMessages(String message) {
        super(message);  // Calls the constructor of RuntimeException with the provided message.
    }
}
