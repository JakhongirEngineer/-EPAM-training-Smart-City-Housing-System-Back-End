package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class ConfirmationPasswordDoesNotMatchException extends RuntimeException{
    public ConfirmationPasswordDoesNotMatchException() {
    }

    public ConfirmationPasswordDoesNotMatchException(String message) {
        super(message);
    }
}
