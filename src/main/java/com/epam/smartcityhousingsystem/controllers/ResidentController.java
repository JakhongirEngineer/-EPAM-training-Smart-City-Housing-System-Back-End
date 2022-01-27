package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.HouseDTO;
import com.epam.smartcityhousingsystem.dtos.ResidentDTO;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.services.ResidentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author Jakhongir Rasulov on 11/1/21
 * @project smart-city-housing-system
 */

@CrossOrigin
@RequestMapping("/api/v1/residents")
@RequiredArgsConstructor
@RestController
public class ResidentController {

    private final ResidentService residentService;

    @ApiOperation(
            value = "find resident by resident code",
            notes = "with path variable residentCode, endpoint finds a resident",
            response = Resident.class
    )
    @GetMapping("/{residentCode}")
    public ResidentDTO findByResidentCode(@ApiParam(value = "residentCode", required = true) @PathVariable long residentCode){
        return residentService.findResidentDTOByResidentCode(residentCode);
    }

    @ApiOperation(
            value = "find houses of resident",
            notes = "with path variable residentCode, endpoint finds houses that belong to a resident represented by residentCode",
            response = House.class,
            responseContainer = "Set"
    )

    @GetMapping("/{residentCode}/houses")
    public Set<HouseDTO> findHousesOfResident(@ApiParam(value = "residentCode", required = true) @PathVariable long residentCode){
        return residentService.findHouseDTOsOfResident(residentCode);
    }
}
