package com.epam.smartcityhousingsystem.exceptions;

import javax.naming.AuthenticationException;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String explanation) {
        super(explanation);
    }

    public CustomAuthenticationException() {
    }
}
