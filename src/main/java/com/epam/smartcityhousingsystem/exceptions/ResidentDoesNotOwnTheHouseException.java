package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class ResidentDoesNotOwnTheHouseException extends RuntimeException{
    public ResidentDoesNotOwnTheHouseException() {
    }

    public ResidentDoesNotOwnTheHouseException(String message) {
        super(message);
    }
}
