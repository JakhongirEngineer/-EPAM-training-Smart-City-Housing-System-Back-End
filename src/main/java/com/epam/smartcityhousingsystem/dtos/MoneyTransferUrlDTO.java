package com.epam.smartcityhousingsystem.dtos;

import lombok.*;

/**
 * MoneyTransferUrlDTO is the response object
 * from Citizen account module, and it is returned
 * when money transfer begins as a first step.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class MoneyTransferUrlDTO {
    private boolean success;
    private String message;
    private String object;
}
