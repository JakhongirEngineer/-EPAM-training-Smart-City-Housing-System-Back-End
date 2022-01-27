package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jakhongir Rasulov on 11/24/21
 * @project smartcity-housing-system
 */
@ActiveProfiles({"test"})
@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("RESIDENT");
        role.setId(2);
        roleRepository.save(role);
    }

    @Test
    @DisplayName("test when role exists in the database")
    void testRoleExists() {
        Optional<Role> roleOptional = roleRepository.findByName("RESIDENT");
        assertTrue(roleOptional.isPresent());
    }

    @Test
    @DisplayName("test when role does not exist in the database")
    void testRoleDoesNotExist() {
        Optional<Role> roleOptional = roleRepository.findByName("NOT_SUCH_ROLE");
        assertTrue(roleOptional.isEmpty());
    }

}