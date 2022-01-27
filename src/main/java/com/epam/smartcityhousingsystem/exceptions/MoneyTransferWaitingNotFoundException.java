package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class MoneyTransferWaitingNotFoundException extends RuntimeException{
    public MoneyTransferWaitingNotFoundException(String message) {
        super(message);
    }
}
