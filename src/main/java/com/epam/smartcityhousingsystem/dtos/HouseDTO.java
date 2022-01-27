package com.epam.smartcityhousingsystem.dtos;


import com.epam.smartcityhousingsystem.entities.Address;
import com.epam.smartcityhousingsystem.entities.enums.Condition;
import com.epam.smartcityhousingsystem.entities.enums.Heating;
import com.epam.smartcityhousingsystem.entities.enums.HouseType;
import com.epam.smartcityhousingsystem.entities.enums.MaterialType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 * @author Jakhongir Rasulov
 * @project smart-city-housing-system
 */

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HouseDTO {

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

    private long residentCode;

    private Address address;
}
