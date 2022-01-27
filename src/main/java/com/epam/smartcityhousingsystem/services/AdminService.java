package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.HouseDTO;
import com.epam.smartcityhousingsystem.dtos.ResidentDTO;
import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.exceptions.HouseNotFoundException;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.utils.HousingSystemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ResidentRepository residentRepository;
    private final HousingSystemUtils housingSystemUtils;

    /**
     * @param pageNumber represents page number for pagination
     * @param pageSize represents page size for pagination
     * @param sortBy represents sorting parameter for pagination
     * @return List of ResidentDTOs
     */
    public List<ResidentDTO> getAllResidentsAsDTOs(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<Resident> residentPage = residentRepository.findAll(pageable);
        if (residentPage.hasContent()){
            return residentPage
                    .getContent()
                    .stream()
                    .map(this::mapResidentToResidentDTO)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param resident is Resident object that is mapped to ResidentDTO
     * @return ResidentDTO
     */
    private ResidentDTO mapResidentToResidentDTO(Resident resident) {
        return ResidentDTO
                .builder()
                .residentCode(resident.getResidentCode())
                .email(resident.getEmail())
                .firstName(resident.getFirstName())
                .lastName(resident.getLastName())
                .houseCodes(
                        resident.getHouses()
                        .stream()
                        .map(House::getCode)
                        .collect(Collectors.toSet())
                )
                .advertisementUUIDs(
                        resident.getAdvertisements()
                        .stream()
                        .map(Advertisement::getUuid)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    /**
     *
     * @param residentCode belongs to a resident that can be used
     *                     to uniquely identify a resident
     * @param houseCode is used to uniquely identify a house
     * @return HouseDTO
     */
    public HouseDTO getHouse(long residentCode, long houseCode) {
        Resident resident = housingSystemUtils.extractResident(residentCode);
        var houses = resident
                .getHouses()
                .stream()
                .filter(house -> house.getCode() == houseCode)
                .map(housingSystemUtils::mapHouseToHouseDTO)
                .collect(Collectors.toList());
        if (houses.isEmpty()){
            throw new HouseNotFoundException("house not found");
        }
        return houses.get(0);
    }
}
