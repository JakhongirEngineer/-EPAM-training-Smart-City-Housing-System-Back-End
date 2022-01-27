package com.epam.smartcityhousingsystem.dtos;

import lombok.*;

/**
 * After money transformation, City Management module
 * needs to confirm ownership change.
 * HousingSystemConfirmationRequest is the request object
 * to City Management module.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class HousingSystemConfirmationRequest {
    private long ownerCardNumber;
    private long buyerCardNumber;
    private long homeCode;
}
