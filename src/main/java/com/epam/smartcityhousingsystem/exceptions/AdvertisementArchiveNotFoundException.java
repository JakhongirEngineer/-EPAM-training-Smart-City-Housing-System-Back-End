package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

public class AdvertisementArchiveNotFoundException extends RuntimeException{
    public AdvertisementArchiveNotFoundException() {
    }

    public AdvertisementArchiveNotFoundException(String message) {
        super(message);
    }
}
