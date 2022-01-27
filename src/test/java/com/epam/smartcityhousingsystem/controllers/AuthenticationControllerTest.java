package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.SignInRequest;
import com.epam.smartcityhousingsystem.dtos.SignInResponse;
import com.epam.smartcityhousingsystem.dtos.SignUpRequest;
import com.epam.smartcityhousingsystem.dtos.SignUpResponse;
import com.epam.smartcityhousingsystem.security.JWTService;
import com.epam.smartcityhousingsystem.security.User;
import com.epam.smartcityhousingsystem.services.HousingSystemAuthenticationService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HousingSystemAuthenticationService authenticationService;
    @MockBean
    private JWTService jwtService;



    @Test
    @DisplayName("when user provides proper request body, sign up succeeds")
    void testSignUpWithProperRequestBody() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("email@gmail.com","password","password");
        SignUpResponse signUpResponse = new SignUpResponse("you have successfully signed up",1234);
        when(authenticationService.signUp(signUpRequest)).thenReturn(signUpResponse);
        mockMvc.perform(
                post("/api/v1/authentication/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("when user provides proper request body, sign in succeeds")
    @WithMockUser(username = "user",password = "password", roles = {"RESIDENT", "ADMIN"})
    void testSignInWithProperRequestBody() throws Exception {
        SignInRequest signInRequest = new SignInRequest("email@gmail.com","password");
        SignInResponse signInResponse = new SignInResponse(new User(),"alsjfkhakhl.iadsclkhakhkldahuweiw.askhcjahv",111);


        when(jwtService.signIn(signInRequest)).thenReturn(signInResponse);
        mockMvc.perform(
                post("/api/v1/authentication/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest))
        ).andExpect(status().isOk());
    }

}