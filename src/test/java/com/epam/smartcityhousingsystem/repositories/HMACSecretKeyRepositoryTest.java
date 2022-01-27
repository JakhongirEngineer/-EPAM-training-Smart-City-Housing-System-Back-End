package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.HMACSecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jakhongir Rasulov on 11/25/21
 * @project smartcity-housing-system
 */
@ActiveProfiles({"test"})
@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class HMACSecretKeyRepositoryTest {

    @Autowired
    HMACSecretKeyRepository hmacSecretKeyRepository;
    @BeforeEach
    void setUp() {
        HMACSecretKey hmacSecretKey = new HMACSecretKey();
        hmacSecretKey.setId(1);
        hmacSecretKey.setSecretKey("secret");
        hmacSecretKey.setComponentName("component");
        hmacSecretKeyRepository.save(hmacSecretKey);
    }

    @Test
    @DisplayName("tests when a correct component name is given, secret key needs to be retrieved")
    void testRetrievingHMACSecretKeySuccess() {
        String componentSecretKey = hmacSecretKeyRepository.takeHMACSecretKeyByComponentName("component");
        assertEquals("secret", componentSecretKey);
    }

    @Test
    @DisplayName("tests when an incorrect component name is given, secret key must not be retrieved")
    void testRetrievingHMACSecretKeyFailure() {
        String incorrectSecretKey = hmacSecretKeyRepository.takeHMACSecretKeyByComponentName("incorrect_component");
        assertNull(incorrectSecretKey);
    }
}