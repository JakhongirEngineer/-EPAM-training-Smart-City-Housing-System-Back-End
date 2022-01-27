package com.epam.smartcityhousingsystem.dtos;


import lombok.*;

/**
 * After money transformation, City Management module
 * needs to confirm ownership change.
 * HousingSystemConfirmationResponse is the response object
 * from City Management module.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class HousingSystemConfirmationResponse {
    private String message;
    private boolean success;
}
