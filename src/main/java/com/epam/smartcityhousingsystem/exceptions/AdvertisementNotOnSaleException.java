package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class AdvertisementNotOnSaleException extends RuntimeException{
    public AdvertisementNotOnSaleException() {
    }

    public AdvertisementNotOnSaleException(String message) {
        super(message);
    }
}
