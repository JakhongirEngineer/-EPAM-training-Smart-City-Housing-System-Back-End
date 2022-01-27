package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.AdvertisementDTO;
import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.exceptions.ResidentDoesNotOwnTheAdvertisementException;
import com.epam.smartcityhousingsystem.exceptions.ResidentDoesNotOwnTheHouseException;
import com.epam.smartcityhousingsystem.repositories.AdvertisementRepository;
import com.epam.smartcityhousingsystem.repositories.PhotoRepository;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.utils.HousingSystemUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class AdvertisementServiceTest {

    @MockBean
    private  AdvertisementRepository advertisementRepository;
    @MockBean
    private  PhotoRepository photoRepository;
    @MockBean
    private  ResidentRepository residentRepository;
    @MockBean
    private  HousingSystemUtils housingSystemUtils;

    @Autowired
    AdvertisementService advertisementService;

    @BeforeEach
    void initEach(){
        advertisementService = new AdvertisementService(advertisementRepository,photoRepository,residentRepository,housingSystemUtils);
    }

    @Test
    @DisplayName("when resident creates an advertisement for house that he does not own, exception is thrown")
    void testResidentDoesNotOwnHouseCreatingAdvertisement(){
        Resident resident = new Resident();
        House house = new House();

        when(housingSystemUtils.extractResident(1234)).thenReturn(resident);
        when(housingSystemUtils.extractHouse(644)).thenReturn(house);
        when(housingSystemUtils.checkResidentOwnsHouse(resident,house)).thenReturn(false);

         assertThrows(ResidentDoesNotOwnTheHouseException.class,
                 () -> advertisementService.createAdvertisement(1234,new AdvertisementDTO())
         );
    }

   @Test
   @DisplayName("when resident has one advertisement, returned set size is one")
   void testGetAdvertisementDTOsByResidentCode(){
        Resident resident = new Resident();
        Set<Advertisement> ads = new HashSet<>();
        ads.add(new Advertisement());
        resident.setAdvertisements(ads);

        when(housingSystemUtils.extractResident(1234)).thenReturn(resident);
        Set<AdvertisementDTO> advertisementDTOsByResidentCode = advertisementService.getAdvertisementDTOsByResidentCode(1234);
        assertEquals(1, advertisementDTOsByResidentCode.size());
    }

    @Test
    @DisplayName("when resident does not own the ad,he cannot update, error is thrown")
    void testAdUpdateWhenResidentDoesNotOwnAd(){
        Resident resident = new Resident();
        when(housingSystemUtils.extractResident(123)).thenReturn(resident);
        Advertisement advertisement = new Advertisement();
        when(housingSystemUtils.extractAdvertisement("qwerty")).thenReturn(advertisement);
        when(housingSystemUtils.checkResidentOwnsAdvertisement(resident,advertisement)).thenReturn(false);

        assertThrows(ResidentDoesNotOwnTheAdvertisementException.class,
                () -> advertisementService.updateAdvertisement(123,"qwerty", new AdvertisementDTO())
        );

    }

    @Test
    @DisplayName("when resident owns the add, he deletes it")
    void testDeleteSuccessful(){
        Resident resident = new Resident();
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setUuid("qwerty");

        when(housingSystemUtils.extractResident(1234)).thenReturn(resident);
        when(housingSystemUtils.extractAdvertisement("qwerty")).thenReturn(advertisement);

        when(housingSystemUtils.checkResidentOwnsAdvertisement(resident,advertisement)).thenReturn(true);

        when(photoRepository.deleteByAdvertisementId(advertisement.getId())).thenReturn(1);
        when(advertisementRepository.deleteByUuid(advertisement.getUuid())).thenReturn(1);

        assertTrue(advertisementService.deleteAdvertisement(1234,"qwerty"));
    }


    @Test
    @DisplayName("when resident does not own the add, he cannot delete it, exception is thrown")
    void testResidentDoesNotOwnAdvertisement(){
        Resident resident = new Resident();
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setUuid("qwerty");

        when(housingSystemUtils.extractResident(1234)).thenReturn(resident);
        when(housingSystemUtils.extractAdvertisement("qwerty")).thenReturn(advertisement);

        when(housingSystemUtils.checkResidentOwnsAdvertisement(resident,advertisement)).thenReturn(false);

        when(photoRepository.deleteByAdvertisementId(advertisement.getId())).thenReturn(0);
        when(advertisementRepository.deleteByUuid(advertisement.getUuid())).thenReturn(0);

        assertThrows(ResidentDoesNotOwnTheAdvertisementException.class,
                () -> advertisementService.deleteAdvertisement(1234,"qwerty")
        );

    }


    @Test
    @DisplayName("when resident owns advertisement, ad dto is returned")
    void testGetAdvertisementDTOByUUID(){
        Resident resident = new Resident();
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setUuid("qwerty");

        when(housingSystemUtils.extractResident(1234)).thenReturn(resident);
        when(housingSystemUtils.extractAdvertisement("qwerty")).thenReturn(advertisement);

        when(housingSystemUtils.checkResidentOwnsAdvertisement(resident,advertisement)).thenReturn(true);

        when(housingSystemUtils.mapAdvertisementToAdvertisementDto(advertisement)).thenReturn(new AdvertisementDTO());

        AdvertisementDTO returnedAdDTO = advertisementService.getAdvertisementDTOByUUID(1234, "qwerty");

        assertNotNull(returnedAdDTO);
    }

    @Test
    @DisplayName("when resident does not own advertisement, exception is thrown")
    void testGetAdDTOThrowsException(){
        Resident resident = new Resident();
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1);
        advertisement.setUuid("qwerty");

        when(housingSystemUtils.extractResident(1234)).thenReturn(resident);
        when(housingSystemUtils.extractAdvertisement("qwerty")).thenReturn(advertisement);

        when(housingSystemUtils.checkResidentOwnsAdvertisement(resident,advertisement)).thenReturn(false);

        when(housingSystemUtils.mapAdvertisementToAdvertisementDto(advertisement)).thenReturn(new AdvertisementDTO());

        assertThrows(ResidentDoesNotOwnTheAdvertisementException.class,
                () -> advertisementService.getAdvertisementDTOByUUID(1234, "qwerty")
        );
    }

}