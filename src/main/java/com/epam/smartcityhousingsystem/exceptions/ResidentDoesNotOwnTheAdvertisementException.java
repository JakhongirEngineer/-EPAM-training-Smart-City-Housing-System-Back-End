package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class ResidentDoesNotOwnTheAdvertisementException extends RuntimeException{
    public ResidentDoesNotOwnTheAdvertisementException() {
    }

    public ResidentDoesNotOwnTheAdvertisementException(String message) {
        super(message);
    }
}
