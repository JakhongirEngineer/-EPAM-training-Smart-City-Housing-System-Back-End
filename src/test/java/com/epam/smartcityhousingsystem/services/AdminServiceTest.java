package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.HouseDTO;
import com.epam.smartcityhousingsystem.dtos.ResidentDTO;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.exceptions.HouseNotFoundException;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.utils.HousingSystemUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class AdminServiceTest {

    @MockBean
    private ResidentRepository residentRepository;
    @MockBean
    private HousingSystemUtils housingSystemUtils;

    @Autowired
    AdminService adminService;

    @BeforeEach
    public void initEach(){
        adminService = new AdminService(residentRepository,housingSystemUtils);
    }

    @Test
    @DisplayName("when there is no resident returned by a repository, returns an empty arraylist")
    void testGetAllResidentsAsDTOsWithoutContent(){

        Page<Resident> residentPage = new PageImpl<>(new ArrayList<>());

        when(residentRepository.findAll(PageRequest.of(1,10, Sort.by("firstName"))))
                .thenReturn(residentPage);

        List<ResidentDTO> emptyList = adminService.getAllResidentsAsDTOs(1, 10, "firstName");
        assertTrue(emptyList.isEmpty());
    }

    @Test
    @DisplayName("when there is no resident returned by a repository, returns an empty arraylist")
    void testGetAllResidentsAsDTOsWithContent(){
        List<Resident> residents = new ArrayList<>();
        residents.add(new Resident());
        Page<Resident> residentPage = new PageImpl<>(residents);

        when(residentRepository.findAll(PageRequest.of(1,10, Sort.by("firstName"))))
                .thenReturn(residentPage);

        List<ResidentDTO> hasContent = adminService.getAllResidentsAsDTOs(1, 10, "firstName");
        assertEquals(1,hasContent.size());
    }


    @ParameterizedTest
    @MethodSource("getHouseParams")
    @DisplayName("when resident does not obtain a house, getHouse throws an exception")
    void testGetHouseWhenResidentHasThatHouse(long residentCode, long houseCode){
        Resident resident = new Resident();
        House house = new House();
        house.setCode(houseCode);
        resident.setHouses(Set.of(house));

        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setCode(houseCode);

        when(housingSystemUtils.extractResident(residentCode)).thenReturn(resident);
        when(housingSystemUtils.mapHouseToHouseDTO(house)).thenReturn(houseDTO);

        HouseDTO returnedHouse = adminService.getHouse(residentCode, houseCode);
        assertEquals(houseCode, returnedHouse.getCode());
    }


    @ParameterizedTest
    @MethodSource("getHouseParams")
    @DisplayName("when resident does not obtain a house, getHouse throws an exception")
    void testGetHouseWhenResidentDoesNotHaveThatHouse(long residentCode, long houseCode){
        Resident resident = new Resident();
        when(housingSystemUtils.extractResident(residentCode)).thenReturn(resident);

        assertThrows(HouseNotFoundException.class, () -> {
            adminService.getHouse(residentCode, houseCode);
        });
    }

    static Stream<Arguments> getHouseParams(){
        return Stream.of(
                Arguments.of(111,1111),
                Arguments.of(222,2222),
                Arguments.of(333, 3333)
        );
    }


}