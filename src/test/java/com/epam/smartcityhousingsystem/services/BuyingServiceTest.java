package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.*;
import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.entities.MoneyTransferWaiting;
import com.epam.smartcityhousingsystem.entities.Photo;
import com.epam.smartcityhousingsystem.entities.enums.AdvertisementState;
import com.epam.smartcityhousingsystem.exceptions.*;
import com.epam.smartcityhousingsystem.repositories.*;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.utils.HousingSystemUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class BuyingServiceTest {

    @MockBean
    AdvertisementRepository advertisementRepository;
    @MockBean
    HousingSystemUtils housingSystemUtils;
    @MockBean
    ResidentRepository residentRepository;
    @MockBean
    AdvertisementArchiveRepository advertisementArchiveRepository;
    @MockBean
    PhotoRepository photoRepository;
    @MockBean
    MoneyTransferWaitingRepository moneyTransferWaitingRepository;
    @MockBean
    HTTPRequestWithHMAC requestWithHMAC;

    @Autowired
    BuyingService buyingService;

    @BeforeEach
    void initEach(){
        buyingService = new BuyingService(
                advertisementRepository,
                housingSystemUtils,
                residentRepository,
                advertisementArchiveRepository,
                photoRepository,
                moneyTransferWaitingRepository,
                requestWithHMAC
        );
    }
    @Test
    @DisplayName("when there is no advertisement, throws exception")
    void testGetAllAdvertisementsThrowsExceptionWhenNoAd(){
        Page<Advertisement> advertisementPage = new PageImpl<>(new ArrayList<>());
        when(advertisementRepository.findAll(PageRequest.of(1,10, Sort.by("price"))))
                .thenReturn(advertisementPage);
        assertThrows(AdvertisementNotFoundException.class,
                () -> buyingService.getAllAdvertisements(1,10,"price")
        );
    }

    @Test
    @DisplayName("when there are advertisements, returns them")
    void testGetAllAdvertisementsWithContent(){
        List<Advertisement> ads = new ArrayList<>();
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertisementState(AdvertisementState.ON_SALE);
        ads.add(advertisement);
        Page<Advertisement> advertisementPage = new PageImpl<>(ads);

        when(advertisementRepository.findAll(PageRequest.of(1,10, Sort.by("price"))))
                .thenReturn(advertisementPage);

        when(housingSystemUtils.mapAdvertisementToAdvertisementDto(any())).thenReturn(new AdvertisementDTO());
        List<AdvertisementDTO> advertisementDTOS = buyingService.getAllAdvertisements(1, 10, "price");
        assertFalse(advertisementDTOS.isEmpty());
    }

    @Test
    @DisplayName("with incorrect uuid to get advertisement, exception is thrown")
    void testGetAdvertisementWithIncorrectUUID(){
        when(advertisementRepository.findByUuid("qwerty")).thenReturn(Optional.empty());
        assertThrows(AdvertisementNotFoundException.class,
                () -> buyingService.getAdvertisementByUUID("qwerty")
        );
    }

    @Test
    @DisplayName("with correct uuid to get advertisement,advertisement is returned")
    void testGetAdvertisementWithCorrectUUID(){
        Advertisement advertisement = new Advertisement();
        advertisement.setUuid("qwerty");
        when(advertisementRepository.findByUuid("qwerty")).thenReturn(Optional.of(advertisement));
        when(housingSystemUtils.mapAdvertisementToAdvertisementDto(advertisement)).thenReturn(new AdvertisementDTO());

        AdvertisementDTO advertisementDTO = buyingService.getAdvertisementByUUID("qwerty");
        assertNotNull(advertisementDTO);
    }

    @Test
    @DisplayName("when advertisement not on sale, exception is thrown")
    void testWhenAdvertisementNoOnSaleThrowsException(){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertisementState(AdvertisementState.BOOKED);
        when(advertisementRepository.findByUuid("qwerty"))
                .thenReturn(Optional.of(advertisement));
        assertThrows(AdvertisementNotOnSaleException.class,
                () -> buyingService.getMoneyTransferUrlDTO("qwerty",new PaymentRequestFromUser())
        );
    }

    @Test
    @DisplayName("when money transfer url is not returned, exception is thrown")
    void testWhenMoneyTransferUrlIsNotReturned(){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertisementState(AdvertisementState.ON_SALE);
        advertisement.setResident(new Resident());
        advertisement.setHouse(new House());
        advertisement.setPhone("1142525");
        advertisement.setPrice(12000);
        advertisement.setTitle("title");
        advertisement.setUuid("qwerty");
        Set<Photo> photos = new HashSet<>();
        advertisement.setPhotos(photos);

        when(advertisementRepository.findByUuid("qwerty"))
                .thenReturn(Optional.of(advertisement));
        when(requestWithHMAC.postForMoneyTransfer(any(),any(),any() )).thenReturn(null);
        PaymentRequestFromUser paymentRequestFromUser = new PaymentRequestFromUser();
        paymentRequestFromUser.setDescription("description");
        paymentRequestFromUser.setResidentCardNumberSender(1234);

        assertThrows(PaymentNotSuccessfulException.class,
                () -> buyingService.getMoneyTransferUrlDTO("qwerty",paymentRequestFromUser)
        );
    }

    @Test
    @DisplayName("when money transfer url is returned but status is not success, exception is thrown")
    void testWhenMoneyTransferUrlIsReturnedWithNotSuccessful(){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertisementState(AdvertisementState.ON_SALE);
        advertisement.setResident(new Resident());
        advertisement.setHouse(new House());
        advertisement.setPhone("1142525");
        advertisement.setPrice(12000);
        advertisement.setTitle("title");
        advertisement.setUuid("qwerty");
        Set<Photo> photos = new HashSet<>();
        advertisement.setPhotos(photos);

        when(advertisementRepository.findByUuid("qwerty"))
                .thenReturn(Optional.of(advertisement));

        MoneyTransferUrlDTO moneyTransferUrlDTO = new MoneyTransferUrlDTO(false,"not successful", "/nowhere");
        when(requestWithHMAC.postForMoneyTransfer(any(),any(),any() )).thenReturn(moneyTransferUrlDTO);
        PaymentRequestFromUser paymentRequestFromUser = new PaymentRequestFromUser();
        paymentRequestFromUser.setDescription("description");
        paymentRequestFromUser.setResidentCardNumberSender(1234);

        assertThrows(PaymentNotSuccessfulException.class,
                () -> buyingService.getMoneyTransferUrlDTO("qwerty",paymentRequestFromUser)
        );
    }

    @Test
    @DisplayName("when money transfer url is returned but status is not success, exception is thrown")
    void testWhenMoneyTransferUrlIsSuccessful(){
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertisementState(AdvertisementState.ON_SALE);
        advertisement.setResident(new Resident());
        advertisement.setHouse(new House());
        advertisement.setPhone("1142525");
        advertisement.setPrice(12000);
        advertisement.setTitle("title");
        advertisement.setUuid("qwerty");
        Set<Photo> photos = new HashSet<>();
        advertisement.setPhotos(photos);

        when(advertisementRepository.findByUuid("qwerty"))
                .thenReturn(Optional.of(advertisement));

        MoneyTransferUrlDTO moneyTransferUrlDTO = new MoneyTransferUrlDTO(true,"successful", "/here");
        when(requestWithHMAC.postForMoneyTransfer(any(),any(),any() )).thenReturn(moneyTransferUrlDTO);
        PaymentRequestFromUser paymentRequestFromUser = new PaymentRequestFromUser();
        paymentRequestFromUser.setDescription("description");
        paymentRequestFromUser.setResidentCardNumberSender(1234);

        MoneyTransferUrlDTO result = buyingService.getMoneyTransferUrlDTO("qwerty", paymentRequestFromUser);
        assertNotNull(result);
    }

    @Test
    @DisplayName("when there is no transfer, exception is thrown")
    void testNoMoneyTransferFound(){
        when(moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(123L,345L))
                .thenReturn(Optional.empty());
        assertThrows(MoneyTransferWaitingNotFoundException.class,
                () -> buyingService.completeMoneyTransfer(new ComplateTransferRequest())
        );
    }

    @Test
    @DisplayName("when transfer is not successful, exception is thrown")
    void testTransferIsNotSuccessful(){
        MoneyTransferWaiting moneyTransferWaiting = new MoneyTransferWaiting();
        moneyTransferWaiting.setSenderCardNumber(123L);
        moneyTransferWaiting.setAdvertisementUUID("qwerty");
        moneyTransferWaiting.setReceiverCardNumber(345L);
        moneyTransferWaiting.setId(1L);

        when(moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(123L,345L))
                .thenReturn(Optional.of(List.of(moneyTransferWaiting) ));

        when(advertisementRepository.findByUuid("qwerty")).thenReturn(Optional.of(new Advertisement()));

        ComplateTransferRequest request = new ComplateTransferRequest();
        request.setSenderCardNumber(123L);
        request.setReceiverCardNumber(345L);
        request.setSuccess(false);

        assertThrows(PaymentNotSuccessfulException.class,
                () -> buyingService.completeMoneyTransfer(request)
        );
    }

    @Test
    @DisplayName("when confirmation is not successful, exception is thrown")
    void testConfirmationIsNotSuccessful(){
        MoneyTransferWaiting moneyTransferWaiting = new MoneyTransferWaiting();
        moneyTransferWaiting.setSenderCardNumber(123L);
        moneyTransferWaiting.setAdvertisementUUID("qwerty");
        moneyTransferWaiting.setReceiverCardNumber(345L);
        moneyTransferWaiting.setId(1L);

        when(moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(123L,345L))
                .thenReturn(Optional.of(List.of(moneyTransferWaiting)));

        Advertisement advertisement = new Advertisement();
        House house = new House();
        house.setCode(1245L);
        advertisement.setHouse(house);

        when(advertisementRepository.findByUuid("qwerty")).thenReturn(Optional.of(advertisement));

        ComplateTransferRequest request = new ComplateTransferRequest();
        request.setSenderCardNumber(123L);
        request.setReceiverCardNumber(345L);
        request.setSuccess(true);

        Resident buyer = new Resident();
        buyer.setResidentCode(123L);
        Resident seller = new Resident();
        seller.setResidentCode(345L);
        HousingSystemConfirmationRequest housingSystemConfirmationRequest = new HousingSystemConfirmationRequest();
        housingSystemConfirmationRequest.setOwnerCardNumber(345L);
        housingSystemConfirmationRequest.setBuyerCardNumber(123L);
        housingSystemConfirmationRequest.setHomeCode(advertisement.getHouse().getCode());

        when(residentRepository.findByResidentCode(buyer.getResidentCode())).thenReturn(Optional.of(buyer));
        when(residentRepository.findByResidentCode(seller.getResidentCode())).thenReturn(Optional.of(seller));
        when(requestWithHMAC.postForConfirmationFromCityAdministration("action", "path",housingSystemConfirmationRequest ))
                .thenReturn(null);


        assertThrows(HousingSystemConfirmationNotSuccessful.class,
                () -> buyingService.completeMoneyTransfer(request)
        );

    }
}