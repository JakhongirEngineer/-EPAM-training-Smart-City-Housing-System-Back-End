package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.AdvertisementDTO;
import com.epam.smartcityhousingsystem.services.AdvertisementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AdvertisementControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AdvertisementService advertisementService;

    @Test
    @DisplayName("creates advertisement with RESIDENT role, and expects CREATED status")
    @WithMockUser(username = "resident", password = "resident", roles = "RESIDENT")
    void testCreateAdvertisementWithCorrectRole() throws Exception {
        AdvertisementDTO advertisementDTO = AdvertisementDTO
                .builder()
                .photoUrls(Set.of("kdfkajskf"))
                .houseCode(454656)
                .residentCode(1234)
                .description("amazing house")
                .price(30000d)
                .phone("359029752")
                .uuid("asklfdal")
                .title("amaizng house")
                .build();

        when(advertisementService.createAdvertisement(1234,advertisementDTO)).thenReturn(true);

        mockMvc.perform(
                post("/api/v1/advertisements/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advertisementDTO))
        ).andExpect(status().is2xxSuccessful());

    }

    @Test
    @DisplayName("when user role is not RESIDENT, client error occurs")
    @WithMockUser(username = "resident", password = "resident", roles = "ADMIN")
    void testCreateAdvertisementWithIncorrectRole() throws Exception {
        AdvertisementDTO advertisementDTO = AdvertisementDTO
                .builder()
                .photoUrls(Set.of("kdfkajskf"))
                .houseCode(454656)
                .residentCode(1234)
                .description("amazing house")
                .price(30000d)
                .phone("359029752")
                .uuid("asklfdal")
                .title("amaizng house")
                .build();

        when(advertisementService.createAdvertisement(1234,advertisementDTO)).thenReturn(true);

        mockMvc.perform(
                post("/api/v1/advertisements/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advertisementDTO))
        ).andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("when user role is RESIDENT, endpoint returns advertisements belonging to the logged in user")
    @WithMockUser(username = "resident", password = "resident", roles = "RESIDENT")
    void testGetAdvertisementsWithCorrectRole() throws Exception {
        Set<AdvertisementDTO> ads = new HashSet<>();
        ads.add(new AdvertisementDTO());
        when(advertisementService.getAdvertisementDTOsByResidentCode(1234)).thenReturn(ads);
        mockMvc.perform(get("/api/v1/advertisements/1234")).andExpect(status().isOk());

    }

    @Test
    @DisplayName("when user role is not RESIDENT, client error occurs")
    @WithMockUser(username = "resident", password = "resident", roles = "ADMIN")
    void testGetAdvertisementsWithIncorrectRole() throws Exception {
        Set<AdvertisementDTO> ads = new HashSet<>();
        ads.add(new AdvertisementDTO());
        when(advertisementService.getAdvertisementDTOsByResidentCode(1234)).thenReturn(ads);
        mockMvc.perform(get("/api/v1/advertisements/1234")).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user role is RESIDENT, endpoint returns a specific advertisement")
    @WithMockUser(username = "resident", password = "password", roles = "RESIDENT")
    void testGetAdvertisementWithCorrectRole() throws Exception {
        when(advertisementService.getAdvertisementDTOByUUID(123,"qwerty")).thenReturn(new AdvertisementDTO());
        mockMvc.perform(get("/api/v1/advertisements/{residentCode}/{uuid}", 123,"qwerty")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user role is not RESIDENT, client error occurs")
    @WithMockUser(username = "resident", password = "password", roles = "ADMIN")
    void testGetAdvertisementWithIncorrectRole() throws Exception {
        when(advertisementService.getAdvertisementDTOByUUID(123,"qwerty")).thenReturn(new AdvertisementDTO());
        mockMvc.perform(get("/api/v1/advertisements/{residentCode}/{uuid}", 123,"qwerty")).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user role is RESIDENT, endpoint updates an advertisement")
    @WithMockUser(username = "resident", password = "password", roles = "RESIDENT")
    void testUpdateAdvertisementWithCorrectRole() throws Exception {
        AdvertisementDTO dtoForUpdating = AdvertisementDTO
                .builder()
                .photoUrls(Set.of("kdfkajskf"))
                .houseCode(454656)
                .residentCode(1234)
                .description("amazing house")
                .price(35000d)
                .phone("35905672")
                .uuid("qwerty")
                .title("great house")
                .build();

        AdvertisementDTO dtoForReturning = AdvertisementDTO
                .builder()
                .photoUrls(Set.of("kdfkajskf"))
                .houseCode(454656)
                .residentCode(1234)
                .description("amazing house")
                .price(30000d)
                .phone("359029752")
                .uuid("qwerty")
                .title("amaizng house")
                .build();

        when(advertisementService.updateAdvertisement(1234,"qwerty",dtoForUpdating))
                .thenReturn(dtoForReturning);

        mockMvc.perform(put("/api/v1/advertisements/{residentCode}/{uuid}", 1234, "qwerty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoForUpdating))).andExpect(status().isOk());
    }


    @Test
    @DisplayName("when user role is not RESIDENT,client error occurs")
    @WithMockUser(username = "resident", password = "password", roles = "ADMIN")
    void testUpdateAdvertisementWithIncorrectRole() throws Exception {
        AdvertisementDTO dtoForUpdating = AdvertisementDTO
                .builder()
                .photoUrls(Set.of("kdfkajskf"))
                .houseCode(454656)
                .residentCode(1234)
                .description("amazing house")
                .price(35000d)
                .phone("35905672")
                .uuid("qwerty")
                .title("great house")
                .build();

        AdvertisementDTO dtoForReturning = AdvertisementDTO
                .builder()
                .photoUrls(Set.of("kdfkajskf"))
                .houseCode(454656)
                .residentCode(1234)
                .description("amazing house")
                .price(30000d)
                .phone("359029752")
                .uuid("qwerty")
                .title("amaizng house")
                .build();

        when(advertisementService.updateAdvertisement(1234,"qwerty",dtoForUpdating))
                .thenReturn(dtoForReturning);

        mockMvc.perform(put("/api/v1/advertisements/{residentCode}/{uuid}", 1234, "qwerty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoForUpdating))).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user has RESIDENT role, advertisement can be deleted")
    @WithMockUser(username = "resident",password = "resident", roles = "RESIDENT")
    void testDeleteAdvertisementWithCorrectRole() throws Exception {
        when(advertisementService.deleteAdvertisement(1234,"qwerty")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/advertisements/{residentCode}/{uuid}",1234,"qwerty")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user has not RESIDENT role, advertisement cannot be deleted")
    @WithMockUser(username = "resident",password = "resident", roles = "ADMIN")
    void testDeleteAdvertisementWithIncorrectRole() throws Exception {
        when(advertisementService.deleteAdvertisement(1234,"qwerty")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/advertisements/{residentCode}/{uuid}",1234,"qwerty")).andExpect(status().is4xxClientError());
    }


}