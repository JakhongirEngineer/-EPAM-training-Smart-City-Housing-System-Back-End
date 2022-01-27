package com.epam.smartcityhousingsystem.entities;

import com.epam.smartcityhousingsystem.entities.enums.Condition;
import com.epam.smartcityhousingsystem.entities.enums.Heating;
import com.epam.smartcityhousingsystem.entities.enums.HouseType;
import com.epam.smartcityhousingsystem.entities.enums.MaterialType;
import com.epam.smartcityhousingsystem.security.Resident;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * House Entity represents House table in the database.
 * It has many to one relationship with Resident Entity, and it is the owning side.
 * It has one to one relationship with Advertisement Entity, and it is the owning side.
 * It has one to one relationship with Address Entity, and it is the owning side.
 * All properties are required.
 * @code property is used to uniquely identify the house.
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
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull(message = "code cannot be null")
    private long code; // houseCode from City Administration;
    @NotNull(message = "numberOfRooms cannot be null")
    private int numberOfRooms;
    @NotNull(message = "materialType cannot be null")
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;
    @NotNull(message = "houseType cannot be null")
    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    @NotNull(message = "totalArea cannot be null")
    private double totalArea;
    @NotNull(message = "ceilingHeight cannot be null")
    private double ceilingHeight;
    @NotNull(message = "furnished cannot be null")
    private boolean furnished;
    @NotNull(message = "condition cannot be null")
    @Enumerated(EnumType.STRING)
    private Condition condition;
    @NotNull(message = "heating cannot be null")
    @Enumerated(EnumType.STRING)
    private Heating heating;

    @JsonManagedReference
    @ManyToOne
    private Resident resident;


    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "house", orphanRemoval = true)
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}
