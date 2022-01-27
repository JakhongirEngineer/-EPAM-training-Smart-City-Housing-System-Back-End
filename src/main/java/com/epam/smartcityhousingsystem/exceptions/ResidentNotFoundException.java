package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class ResidentNotFoundException extends RuntimeException{
    public ResidentNotFoundException() {
    }

    public ResidentNotFoundException(String message) {
        super(message);
    }
}
