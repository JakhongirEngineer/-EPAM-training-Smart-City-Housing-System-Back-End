package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov on 11/10/21
 * @project smartcity-housing-system
 */
public class GetFinalResponseException extends RuntimeException{
    public GetFinalResponseException() {
    }

    public GetFinalResponseException(String message) {
        super(message);
    }
}
