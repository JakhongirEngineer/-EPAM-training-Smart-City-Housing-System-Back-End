package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jakhongir Rasulov on 11/24/21
 * @project smartcity-housing-system
 */

@ActiveProfiles({"test"})
@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class ResidentRepositoryTest {
    @Autowired
    ResidentRepository residentRepository;
    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(2);
        role.setName("RESIDENT");
        roleRepository.save(role);

        Resident resident1 = getResident("resident1@gmail.com", 12345, role);
        Resident resident2 = getResident("resident2@gmail.com", 54321, role);
        residentRepository.save(resident1);
        residentRepository.save(resident2);
    }

    @Test
    @DisplayName("tests if a resident exists with the given resident code")
    void testResidentExistsWithResidentCode() {
        Optional<Resident> residentOptional = residentRepository.findByResidentCode(12345);

        assertAll("checks resident is queried correctly",
                () ->{
                    assertTrue(residentOptional.isPresent());
                },
                ()->{
                    assertEquals("resident1@gmail.com", residentOptional.get().getEmail());
                }
        );
    }

    @Test
    @DisplayName("tests a resident cannot be retrieved with an arbitrarily wrong resident code")
    void testResidentIsNotRetrievedWithWrongResidentCode() {
        Optional<Resident> residentOptional = residentRepository.findByResidentCode(964846);
        assertTrue(residentOptional.isEmpty());
    }

    @Test
    @DisplayName("tests if a resident exists with a given email")
    void testResidentExistsWithEmail() {
        Optional<Resident> residentOptional = residentRepository.findByEmail("resident2@gmail.com");

        assertAll("checks resident is queried correctly",
                () ->{
                    assertTrue(residentOptional.isPresent());
                },
                ()->{
                    assertEquals("resident2@gmail.com", residentOptional.get().getEmail());
                },
                () -> {
                    assertEquals(54321, residentOptional.get().getResidentCode());
                }
        );
    }

    @Test
    @DisplayName("tests a resident cannot be retrieved with an arbitrarily wrong email")
    void testResidentIsNotRetrievedWithWrongEmail() {
        Optional<Resident> residentOptional = residentRepository.findByEmail("not_such_email@gmail.com");
        assertTrue(residentOptional.isEmpty());
    }



    private Resident getResident(String email, long residentCode, Role role){
        Resident resident = new Resident();
        resident.setResidentCode(residentCode);
        resident.setEmail(email);
        resident.setPassword("password");
        resident.setEnabled(true);
        resident.setSignedUp(true);
        resident.setRoles(new HashSet<>(Arrays.asList(role)));
        resident.setLastName("resident");
        resident.setFirstName("resident");

        return resident;
    }
}