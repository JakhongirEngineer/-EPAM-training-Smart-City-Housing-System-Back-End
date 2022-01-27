package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov on 11/10/21
 * @project smartcity-housing-system
 */
public class PostForMoneyTransferException extends RuntimeException{
    public PostForMoneyTransferException() {
    }

    public PostForMoneyTransferException(String message) {
        super(message);
    }
}
