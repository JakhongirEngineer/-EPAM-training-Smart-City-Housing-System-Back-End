package com.epam.smartcityhousingsystem.dtos;

import lombok.*;

import java.util.UUID;

/**
 * ComplateTransferRequest is a response sent by Citizen account module.
 * It describes the result of a money transfer to buy house.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ComplateTransferRequest {
    private String message;
    private Long senderCardNumber;
    private Long receiverCardNumber;
    private UUID transactionId;
    private Integer amount;
    private Boolean success;
}
