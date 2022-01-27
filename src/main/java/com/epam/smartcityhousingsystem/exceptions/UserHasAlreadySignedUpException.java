package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class UserHasAlreadySignedUpException extends RuntimeException{
    public UserHasAlreadySignedUpException() {
    }

    public UserHasAlreadySignedUpException(String message) {
        super(message);
    }
}
