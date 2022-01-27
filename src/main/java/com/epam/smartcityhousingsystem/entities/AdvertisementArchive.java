package com.epam.smartcityhousingsystem.entities;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;


/**
 * AdvertisementArchive Entity represents advertisement_archive table in the database.
 * It is used for analytical purposes, such as analyzing home sales in a city.
 * All properties are required to be not null.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */


@ApiModel(description = "Class that represents advertisement archive")

@Builder
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AdvertisementArchive {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;
    @NotNull(message = "title cannot be null")
    private String title;
    @NotNull(message = "price cannot be null")
    private double price;
    @NotNull(message = "description cannot be null")
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotNull(message = "phone cannot be null")
    private String phone;
    @NotNull(message = "residentId cannot be null")
    private long residentCode;
    @Column(unique = true)
    @NotNull(message = "moneyTransferUUID cannot be null")
    private String moneyTransferUUID;

    @PrePersist
    private void init(){
        this.uuid = UUID.randomUUID().toString();
    }
}