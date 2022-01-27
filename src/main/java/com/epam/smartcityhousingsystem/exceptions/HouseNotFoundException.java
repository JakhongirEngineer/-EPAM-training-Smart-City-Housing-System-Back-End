package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class HouseNotFoundException extends RuntimeException{
    public HouseNotFoundException() {
    }

    public HouseNotFoundException(String message) {
        super(message);
    }
}
