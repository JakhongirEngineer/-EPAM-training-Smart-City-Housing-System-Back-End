package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class AdvertisementNotFoundException extends RuntimeException{
    public AdvertisementNotFoundException() {
    }

    public AdvertisementNotFoundException(String message) {
        super(message);
    }
}
