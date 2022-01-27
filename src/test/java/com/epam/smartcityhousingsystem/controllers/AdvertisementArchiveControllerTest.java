package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.entities.AdvertisementArchive;
import com.epam.smartcityhousingsystem.services.AdvertisementArchiveService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AdvertisementArchiveControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdvertisementArchiveService advertisementArchiveService;

    @Test
    @WithMockUser(username = "admin",password = "admin", roles = "ADMIN")
    @DisplayName("when user role is ADMIN, return advertisement archives")
    void testGetAllAdvertisementArchivesWithCorrectRole() throws Exception {

        List<AdvertisementArchive> archives = new ArrayList<>();
        AdvertisementArchive archive1 = AdvertisementArchive
                                            .builder()
                                            .residentCode(1234)
                                            .moneyTransferUUID("sdfklajhfwef")
                                            .title("home on sale")
                                            .price(30000d)
                                            .phone("129066555")
                                            .description("amazing home")
                                            .uuid("odsijgorqef")
                                            .build();
        archives.add(archive1);

        when(advertisementArchiveService.getAllAdvertisementArchives(1,10,"price"))
                .thenReturn(archives);
        mockMvc.perform(get("/api/v1/advertisementArchives")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",password = "admin", roles = "USER")
    @DisplayName("when user role is ADMIN, return advertisement archives")
    void testGetAllAdvertisementArchivesWithIncorrectRole() throws Exception {

        List<AdvertisementArchive> archives = new ArrayList<>();
        AdvertisementArchive archive1 = AdvertisementArchive
                .builder()
                .residentCode(1234)
                .moneyTransferUUID("sdfklajhfwef")
                .title("home on sale")
                .price(30000d)
                .phone("129066555")
                .description("amazing home")
                .uuid("odsijgorqef")
                .build();
        archives.add(archive1);

        when(advertisementArchiveService.getAllAdvertisementArchives(1,10,"price"))
                .thenReturn(archives);

        mockMvc.perform(get("/api/v1/advertisementArchives")).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user role is ADMIN, get advertisement archive")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testGetAdvertisementArchiveWithCorrectRole() throws Exception {
        when(advertisementArchiveService.getAdvertisementArchive("dkfskvfasc")).thenReturn(new AdvertisementArchive());
        mockMvc.perform(get("/api/v1/advertisementArchives/dkfskvfasc")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user role is not ADMIN, client error occurs")
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    void testGetAdvertisementArchiveWithIncorrectRole() throws Exception {
        when(advertisementArchiveService.getAdvertisementArchive("dkfskvfasc")).thenReturn(new AdvertisementArchive());
        mockMvc.perform(get("/api/v1/advertisementArchives/dkfskvfasc")).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user role is ADMIN, delete advertisement archive")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testDeleteAdvertisementArchiveWithCorrectRole() throws Exception {
        when(advertisementArchiveService.deleteAdvertisementArchive("sakfdakf")).thenReturn(true);
        mockMvc.perform(delete("/api/v1/advertisementArchives/sakfdakf")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user role is not ADMIN, client error occurs")
    @WithMockUser(username = "admin", password = "admin", roles = "USER")
    void testDeleteAdvertisementArchiveWithIncorrectRole() throws Exception {
        when(advertisementArchiveService.deleteAdvertisementArchive("afaklja")).thenReturn(true);
        mockMvc.perform(delete("/api/v1/advertisementArchives/afaklja")).andExpect(status().is4xxClientError());
    }

}