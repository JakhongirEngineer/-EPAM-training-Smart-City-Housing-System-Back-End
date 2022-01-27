package com.epam.smartcityhousingsystem.exceptions.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomExceptionResponse is a custom response
 * that is used by GlobalExceptionHandler.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomExceptionResponse {
    private String message;
}
