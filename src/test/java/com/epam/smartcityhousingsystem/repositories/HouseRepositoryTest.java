package com.epam.smartcityhousingsystem.repositories;

import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.entities.enums.Condition;
import com.epam.smartcityhousingsystem.entities.enums.Heating;
import com.epam.smartcityhousingsystem.entities.enums.HouseType;
import com.epam.smartcityhousingsystem.entities.enums.MaterialType;
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
 * @author Jakhongir Rasulov on 11/25/21
 * @project smartcity-housing-system
 */

@ActiveProfiles({"test"})
@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class HouseRepositoryTest {
    @Autowired
    HouseRepository houseRepository;
    @BeforeEach
    void setUp() {
        House house = new House();
        house.setFurnished(true);
        house.setHouseType(HouseType.APARTMENT);
        house.setCondition(Condition.AVERAGE);
        house.setHeating(Heating.GAS);
        house.setMaterialType(MaterialType.BRICK);
        house.setNumberOfRooms(2);
        house.setCeilingHeight(2);
        house.setCode(12345);
        house.setTotalArea(40);
        house.setId(1);

        houseRepository.save(house);
    }

    @Test
    @DisplayName("tests retrieving a house with the given correct house code")
    void testFindHouseWithCorrectCode() {
        Optional<House> houseOptional = houseRepository.findByCode(12345);
        assertAll("tests retrieved object",
                ()->{
                    assertTrue(houseOptional.isPresent());
                },
                ()->{
                    assertEquals(12345,houseOptional.get().getCode());
                }
        );
    }

    @Test
    @DisplayName("tests retrieving a house with the given incorrect house code")
    void testFindHouseWithIncorrectCode() {
        Optional<House> houseOptional = houseRepository.findByCode(7676767);
        assertTrue(houseOptional.isEmpty());
    }
}