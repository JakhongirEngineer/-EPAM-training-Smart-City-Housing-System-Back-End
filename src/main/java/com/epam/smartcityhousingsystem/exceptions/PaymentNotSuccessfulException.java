package com.epam.smartcityhousingsystem.exceptions;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class PaymentNotSuccessfulException extends RuntimeException {
    public PaymentNotSuccessfulException() {
    }

    public PaymentNotSuccessfulException(String message) {
        super(message);
    }
}
