package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov on 11/10/21
 * @project smartcity-housing-system
 */
public class PostForConfirmationFromCityAdministrationException extends RuntimeException{
    public PostForConfirmationFromCityAdministrationException() {
    }

    public PostForConfirmationFromCityAdministrationException(String message) {
        super(message);
    }
}
