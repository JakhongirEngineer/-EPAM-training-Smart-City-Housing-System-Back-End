package com.epam.smartcityhousingsystem.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * MoneyTransferWaiting is a class that
 * illustrates MoneyTransferWaiting table.
 * It is used to retrieve money transfers
 * that have not completed yet.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MoneyTransferWaiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderCardNumber;
    private Long receiverCardNumber;
    private String advertisementUUID;
}
