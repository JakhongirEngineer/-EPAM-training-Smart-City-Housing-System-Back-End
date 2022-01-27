package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.security.Role;
import com.epam.smartcityhousingsystem.security.User;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jakhongir Rasulov on 11/22/21
 * @project smartcity-housing-system
 */

@ActiveProfiles({"test"})
@DataJpaTest
@Import(BCryptPasswordEncoder.class) // CommandLineRunner needs this bean.
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUpEach(){
        Role role = new Role();
        role.setName("RESIDENT");
        User user = User.builder()
                .email("email@gmail.com")
                .enabled(true)
                .firstName("first_name")
                .lastName("last_name")
                .password("password")
                .roles(new HashSet<>(Arrays.asList(role)))
                .signedUp(true)
                .tokenExpired(false)
                .build();
        userRepository.save(user);
        roleRepository.save(role);

    }

    @Test
    @DisplayName("tests when user email exists in the database")
    void testFindUserByEmailWhenEmailExists() {
        Optional<List<User>> userList = userRepository.findByEmail("email@gmail.com");
        assertEquals(1, userList.get().size());
    }


    @Test
    @DisplayName("tests when the user email does not exist in the database")
    void testFindUserByEmailWhenEmailDoesNotExist() {
        Optional<List<User>> userList = userRepository.findByEmail("notSaved@gmail.com");
        assertEquals(0, userList.get().size());
    }
}