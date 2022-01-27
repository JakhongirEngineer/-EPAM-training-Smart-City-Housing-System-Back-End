package com.epam.smartcityhousingsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;



/**
 * Address Entity represents address table in the database.
 * It has one to one relationship with House Entity.
 * All of its properties are required to be not null.
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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull(message = "homeNumber cannot be null")
    private String homeNumber;
    @NotNull(message = "district cannot be null")
    private String district;
    @NotNull(message = "street cannot be null")
    private String street;

    @JsonBackReference
    @OneToOne(mappedBy = "address")
    private House house;
}
