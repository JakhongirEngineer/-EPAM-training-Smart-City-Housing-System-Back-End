package com.epam.smartcityhousingsystem.services;


import com.epam.smartcityhousingsystem.entities.AdvertisementArchive;
import com.epam.smartcityhousingsystem.exceptions.AdvertisementArchiveNotFoundException;
import com.epam.smartcityhousingsystem.repositories.AdvertisementArchiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class AdvertisementArchiveServiceTest {

    @MockBean
    AdvertisementArchiveRepository advertisementArchiveRepository;
    @Autowired
    AdvertisementArchiveService advertisementArchiveService;

    @BeforeEach
    void initEach(){
        advertisementArchiveService = new AdvertisementArchiveService(advertisementArchiveRepository);
    }

    @Test
    @DisplayName("checks there is one archive returned")
    void testGetAllAdvertisementArchivesHasContent(){
        List<AdvertisementArchive> archives = new ArrayList<>();
        archives.add(new AdvertisementArchive());
        Page<AdvertisementArchive> advertisementArchivePage = new PageImpl<>(archives);

        when(advertisementArchiveRepository.findAll(PageRequest.of(1,10, Sort.by("price"))))
                .thenReturn(advertisementArchivePage);

        List<AdvertisementArchive> returnedList = advertisementArchiveService.getAllAdvertisementArchives(1, 10, "price");
        assertEquals(1, returnedList.size());
    }

    @Test
    @DisplayName("checks there is one archive returned")
    void testGetAllAdvertisementArchivesDoesNotHaveContent(){
        Page<AdvertisementArchive> advertisementArchivePage = new PageImpl<>(new ArrayList<>()); // empty array list means no content
        when(advertisementArchiveRepository.findAll(PageRequest.of(1,10, Sort.by("price"))))
                .thenReturn(advertisementArchivePage);

        assertThrows(AdvertisementArchiveNotFoundException.class, () -> advertisementArchiveService.getAllAdvertisementArchives(1, 10, "price"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwerty1", "qwerty2", "qwerty3", "qwerty4"})
    @DisplayName("when advertisement archive exists, it is returned")
    void testGetAdvertisementArchiveWhenItExists(String uuid){
        AdvertisementArchive archive = new AdvertisementArchive();
        archive.setUuid(uuid);

        when(advertisementArchiveRepository.findByUuid(uuid))
                .thenReturn(Optional.of(archive));

        AdvertisementArchive advertisementArchive = advertisementArchiveService.getAdvertisementArchive(uuid);
        assertEquals(advertisementArchive.getUuid(), uuid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"qwerty1", "qwerty2", "qwerty3", "qwerty4"})
    @DisplayName("when advertisement archive does not exist, error occurs")
    void testGetAdvertisementArchiveWhenItDoesNotExist(String uuid){
        AdvertisementArchive archive = new AdvertisementArchive();
        archive.setUuid(uuid);

        when(advertisementArchiveRepository.findByUuid(uuid))
                .thenReturn(Optional.empty());

        assertThrows(AdvertisementArchiveNotFoundException.class, () -> advertisementArchiveService.getAdvertisementArchive(uuid));
    }


    @Test
    @DisplayName("deletes advertisement archive, when it exists")
    void testDeleteAdvertisementArchive(){
        when(advertisementArchiveRepository.deleteByUuid("uuid"))
                .thenReturn(1);
        boolean deleteResult = advertisementArchiveService.deleteAdvertisementArchive("uuid");
        assertTrue(deleteResult);
    }

    @Test
    @DisplayName("does not delete advertisement archive, when it does not exist")
    void testDeleteAdvertisementArchiveWhenItDoesNotExist(){
        when(advertisementArchiveRepository.deleteByUuid("uuid"))
                .thenReturn(0);
        boolean deleteResult = advertisementArchiveService.deleteAdvertisementArchive("uuid");
        assertFalse(deleteResult);
    }

}