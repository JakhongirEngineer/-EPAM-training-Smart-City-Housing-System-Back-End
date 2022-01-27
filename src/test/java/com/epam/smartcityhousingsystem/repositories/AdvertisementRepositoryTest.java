package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.enums.AdvertisementState;
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
 * @author Jakhongir Rasulov on 11/26/21
 * @project smartcity-housing-system
 */

@ActiveProfiles({"test"})
@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class AdvertisementRepositoryTest {
    @Autowired
    AdvertisementRepository advertisementRepository;

    @BeforeEach
    public void init(){
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setAdvertisementState(AdvertisementState.ON_SALE);
        advertisement.setDescription("description");
        advertisement.setPrice(1234);
        advertisement.setTitle("title");
        advertisement.setPhone("9999999");

        advertisementRepository.save(advertisement);
    }

    @Test
    @DisplayName("tests advertisement deletion with a correct uuid")
    void testDeleteAdvertisementWithCorrectUUID() {
        Advertisement advertisement = advertisementRepository.findAll().get(0);
        Integer deletedAmount = advertisementRepository.deleteByUuid(advertisement.getUuid());
        assertEquals(1,deletedAmount);
    }

    @Test
    @DisplayName("tests advertisement deletion with an incorrect uuid")
    void testDeleteAdvertisementWithIncorrectUUID() {
        Integer deletedAmount = advertisementRepository.deleteByUuid("incorrect_uuid");
        assertEquals(0,deletedAmount);
    }

    @Test
    @DisplayName("tests advertisement finding with a correct uuid")
    void testFindAdvertisementWithCorrectUUID() {
        Advertisement advertisement = advertisementRepository.findAll().get(0);
        Optional<Advertisement> advertisementOptional = advertisementRepository.findByUuid(advertisement.getUuid());

        assertAll("tests retrieved advertisement object",
                ()->{
                    assertTrue(advertisementOptional.isPresent());
                },
                ()->{
                    assertEquals("title",advertisementOptional.get().getTitle());
                }
                );
    }

    @Test
    @DisplayName("tests advertisement finding with a correct uuid")
    void testFindAdvertisementWithIncorrectUUID() {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findByUuid("incorrect_uuid");
        assertTrue(advertisementOptional.isEmpty());
    }
}