package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.HouseDTO;
import com.epam.smartcityhousingsystem.dtos.ResidentDTO;
import com.epam.smartcityhousingsystem.exceptions.ResidentNotFoundException;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.utils.HousingSystemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jakhongir Rasulov on 11/1/21
 * @project smartcity-housing-system
 */

@RequiredArgsConstructor
@Service
public class ResidentService {
    private final ResidentRepository residentRepository;
    private final HousingSystemUtils housingSystemUtils;

    /**
     *
     * @param residentCode is used to uniquely identify a resident
     * @return Resident
     */
    public ResidentDTO findResidentDTOByResidentCode(long residentCode){
        Resident resident = findResidentByResidentCode(residentCode);
        return housingSystemUtils.mapResidentToResidentDTO(resident);
    }


    /**
     *
     * @param residentCode residentCode represents a resident uniquely
     * @return Set of HouseDTO s
     */
    public Set<HouseDTO> findHouseDTOsOfResident(long residentCode) {
        return findResidentByResidentCode(residentCode)
                .getHouses()
                .stream()
                .map(house -> housingSystemUtils.mapHouseToHouseDTO(house))
                .collect(Collectors.toSet());

    }

    /**
     * finds resident by his/her resident code
     * @param residentCode represents a resident uniquely
     * @return Resident
     */
    private Resident findResidentByResidentCode(long residentCode) {
        return residentRepository
                .findByResidentCode(residentCode)
                .orElseThrow(() -> new ResidentNotFoundException("resident not found"));
    }
}
