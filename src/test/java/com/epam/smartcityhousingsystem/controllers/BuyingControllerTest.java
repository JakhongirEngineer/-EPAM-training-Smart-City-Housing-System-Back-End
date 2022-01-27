package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.AdvertisementDTO;
import com.epam.smartcityhousingsystem.dtos.ComplateTransferRequest;
import com.epam.smartcityhousingsystem.dtos.MoneyTransferUrlDTO;
import com.epam.smartcityhousingsystem.dtos.PaymentRequestFromUser;
import com.epam.smartcityhousingsystem.services.BuyingService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class BuyingControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BuyingService buyingService;

    @Test
    @DisplayName("when user has RESIDENT role, endpoint returns list of Advertisements")
    @WithMockUser(username = "resident", password = "password", roles = "RESIDENT")
    void testGetAllAdvertisementsWithCorrectRole() throws Exception {

        List<AdvertisementDTO> ads = new ArrayList<>();
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


        ads.add(advertisementDTO);
        when(buyingService.getAllAdvertisements(1,10,"price")).thenReturn(ads);

        mockMvc.perform(get("/api/v1/buying/advertisements")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user has not RESIDENT role, client error occurs")
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testGetAllAdvertisementsWithIncorrectRole() throws Exception {

        List<AdvertisementDTO> ads = new ArrayList<>();
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


        ads.add(advertisementDTO);
        when(buyingService.getAllAdvertisements(1,10,"price")).thenReturn(ads);

        mockMvc.perform(get("/api/v1/buying/advertisements")).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user has RESIDENT role, endpoint returns advertisement")
    @WithMockUser(username = "resident", password = "resident", roles = "RESIDENT")
    void testGetAdvertisementWithCorrectRole() throws Exception {
        when(buyingService.getAdvertisementByUUID("wwweeerrr")).thenReturn(new AdvertisementDTO());

        mockMvc.perform(get("/api/v1/buying/advertisements/{advertisementUuid}","wwweeerrr"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("when user does not have RESIDENT role,client error occurs")
    @WithMockUser(username = "resident", password = "resident", roles = "ADMIN")
    void testGetAdvertisementWithIncorrectRole() throws Exception {
        when(buyingService.getAdvertisementByUUID("wwweeerrr")).thenReturn(new AdvertisementDTO());

        mockMvc.perform(get("/api/v1/buying/advertisements/{advertisementUuid}","wwweeerrr"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("when user has RESIDENT role, endpoint starts money transfer")
    @WithMockUser(username = "resident", password = "resident", roles = "RESIDENT")
    void testStartMoneyTransferWithCorrectRole() throws Exception {
        PaymentRequestFromUser paymentRequestFromUser = new PaymentRequestFromUser();
        paymentRequestFromUser.setDescription("buy house");
        paymentRequestFromUser.setResidentCardNumberSender(12345);
        when(buyingService.getMoneyTransferUrlDTO("uuid", paymentRequestFromUser)).thenReturn(new MoneyTransferUrlDTO());

        mockMvc.perform(
                post("/api/v1/buying/advertisements/{advertisementUuid}","uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequestFromUser))
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("when user does not have RESIDENT role, client error occurs")
    @WithMockUser(username = "resident", password = "resident", roles = "ADMIN")
    void testStartMoneyTransferWithIncorrectRole() throws Exception {
        PaymentRequestFromUser paymentRequestFromUser = new PaymentRequestFromUser();
        paymentRequestFromUser.setDescription("buy house");
        paymentRequestFromUser.setResidentCardNumberSender(12345);
        when(buyingService.getMoneyTransferUrlDTO("uuid", paymentRequestFromUser)).thenReturn(new MoneyTransferUrlDTO());

        mockMvc.perform(
                post("/api/v1/buying/advertisements/{advertisementUuid}","uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequestFromUser))
        ).andExpect(status().is4xxClientError());

    }
}