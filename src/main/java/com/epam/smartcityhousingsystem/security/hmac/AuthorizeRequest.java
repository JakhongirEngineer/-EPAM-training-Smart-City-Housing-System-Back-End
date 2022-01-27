package com.epam.smartcityhousingsystem.security.hmac;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthorizeRequest is a custom annotation
 * that is used on methods in controller classes
 * to make sure that endpoints hit by other modules
 * are secured.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeRequest {
}
