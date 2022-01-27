package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.HouseDTO;
import com.epam.smartcityhousingsystem.dtos.ResidentDTO;
import com.epam.smartcityhousingsystem.entities.Address;
import com.epam.smartcityhousingsystem.entities.enums.Condition;
import com.epam.smartcityhousingsystem.entities.enums.Heating;
import com.epam.smartcityhousingsystem.entities.enums.HouseType;
import com.epam.smartcityhousingsystem.entities.enums.MaterialType;
import com.epam.smartcityhousingsystem.services.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminService adminService;

    @Test
    @DisplayName("when user role is ADMIN, status will be FOUND")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testGetFinalResponseWithCorrectRole() throws Exception {
        mockMvc.perform(get("/api/v1/admin/cityAdministration")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user role is not ADMIN, status code will be 4XX")
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    void testGetFinalResponseWithIncorrectRole() throws Exception {
        mockMvc.perform(get("/api/v1/admin/cityAdministration")).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user role is ADMIN, endpoint returns residents")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testGetAllResidentsWithCorrectRole() throws Exception {
        List<ResidentDTO> residents = new ArrayList<>();
        ResidentDTO residentDTO1 = ResidentDTO.builder()
                                    .advertisementUUIDs(Set.of("dhakvhlareg"))
                                    .residentCode(12345)
                                    .houseCodes(Set.of(1276L))
                                    .firstName("resident1")
                                    .lastName("resident1LastName")
                                    .email("email1@gmail.com")
                                    .build();
        ResidentDTO residentDTO2 = ResidentDTO.builder()
                .advertisementUUIDs(Set.of("leksdfjoac"))
                .residentCode(14455)
                .houseCodes(Set.of(3276L))
                .firstName("resident2")
                .lastName("resident2LastName")
                .email("email2@gmail.com")
                .build();

        residents.add(residentDTO1);
        residents.add(residentDTO2);

        when(adminService.getAllResidentsAsDTOs(1,10,"firstName")).thenReturn(residents);

        mockMvc.perform(get("/api/v1/admin/residents")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user role is not ADMIN, status code will be 4XX, and does not return residents")
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    void testGetAllResidentsWithIncorrectRole() throws Exception {
        List<ResidentDTO> residents = new ArrayList<>();
        ResidentDTO residentDTO1 = ResidentDTO.builder()
                .advertisementUUIDs(Set.of("dhakvhlareg"))
                .residentCode(12345)
                .houseCodes(Set.of(1276L))
                .firstName("resident1")
                .lastName("resident1LastName")
                .email("email1@gmail.com")
                .build();
        ResidentDTO residentDTO2 = ResidentDTO.builder()
                .advertisementUUIDs(Set.of("leksdfjoac"))
                .residentCode(14455)
                .houseCodes(Set.of(3276L))
                .firstName("resident2")
                .lastName("resident2LastName")
                .email("email2@gmail.com")
                .build();

        residents.add(residentDTO1);
        residents.add(residentDTO2);

        when(adminService.getAllResidentsAsDTOs(1,10,"firstName")).thenReturn(residents);
        mockMvc.perform(get("/api/v1/admin/residents")).andExpect(status().is4xxClientError());
    }



    @Test
    @DisplayName("when user role is ADMIN, endpoint returns HouseDTO")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testGetHouseWithCorrectRole() throws Exception {
        HouseDTO houseDTO = HouseDTO.builder()
                .totalArea(120)
                .numberOfRooms(4)
                .materialType(MaterialType.BRICK)
                .heating(Heating.GAS)
                .code(4567)
                .residentCode(1234)
                .furnished(true)
                .condition(Condition.AVERAGE)
                .houseType(HouseType.APARTMENT)
                .ceilingHeight(3)
                .address(new Address())
                .build();

        when(adminService.getHouse(12345,6453)).thenReturn(houseDTO);
        mockMvc.perform(get("/api/v1/admin/residents/{residentCode}/{houseCode}", 12345,6453)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user role is not ADMIN, status will be 4XX")
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    void testGetHouseWithIncorrectRole() throws Exception {
        HouseDTO houseDTO = HouseDTO.builder()
                .totalArea(120)
                .numberOfRooms(4)
                .materialType(MaterialType.BRICK)
                .heating(Heating.GAS)
                .code(4567)
                .residentCode(1234)
                .furnished(true)
                .condition(Condition.AVERAGE)
                .houseType(HouseType.APARTMENT)
                .ceilingHeight(3)
                .address(new Address())
                .build();

        when(adminService.getHouse(12345,6453)).thenReturn(houseDTO);
        mockMvc.perform(get("/api/v1/admin/residents/{residentCode}/{houseCode}",1234,4567)).andExpect(status().is4xxClientError());
    }
}