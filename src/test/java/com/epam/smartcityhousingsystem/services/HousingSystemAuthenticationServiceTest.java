package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.SignUpRequest;
import com.epam.smartcityhousingsystem.exceptions.ConfirmationPasswordDoesNotMatchException;
import com.epam.smartcityhousingsystem.exceptions.UserHasAlreadySignedUpException;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.repositories.RoleRepository;
import com.epam.smartcityhousingsystem.repositories.UserRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = HousingSystemAuthenticationService.class)
class HousingSystemAuthenticationServiceTest {

    @MockBean
    ResidentRepository residentRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    RoleRepository roleRepository;
    @Autowired
    HousingSystemAuthenticationService housingSystemAuthenticationService;

    @BeforeEach
    void initEach(){
        housingSystemAuthenticationService = new HousingSystemAuthenticationService(
                                                    residentRepository,
                                                    userRepository,
                                                    passwordEncoder,
                                                    roleRepository);
    }

    @Test
    @DisplayName("if a user has already signed up, exception is thrown")
    void testUserAlreadySignedUp(){
        Resident resident = new Resident();
        resident.setSignedUp(true);
        resident.setEmail("email@gmail.com");
        resident.setPassword("password");

        when(residentRepository.findByEmail(resident.getEmail())).thenReturn(Optional.of(resident));

        assertThrows(UserHasAlreadySignedUpException.class,
                () -> housingSystemAuthenticationService.signUp(new SignUpRequest(resident.getEmail(),resident.getPassword(),resident.getPassword())));
    }

    @Test
    @DisplayName("if password does not match with confirmation password, exception is thrown")
    void testConfirmationPasswordDoesNotMatch(){
        Resident resident = new Resident();
        resident.setSignedUp(false);
        resident.setEmail("email@gmail.com");
        resident.setPassword("password");

        when(residentRepository.findByEmail(resident.getEmail())).thenReturn(Optional.of(resident));

        assertThrows(ConfirmationPasswordDoesNotMatchException.class,
                () -> housingSystemAuthenticationService.signUp(new SignUpRequest(resident.getEmail(),resident.getPassword(),"something else")));
    }


}