package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.*;
import com.epam.smartcityhousingsystem.entities.Advertisement;
import com.epam.smartcityhousingsystem.entities.AdvertisementArchive;
import com.epam.smartcityhousingsystem.entities.House;
import com.epam.smartcityhousingsystem.entities.MoneyTransferWaiting;
import com.epam.smartcityhousingsystem.entities.enums.AdvertisementState;
import com.epam.smartcityhousingsystem.exceptions.*;
import com.epam.smartcityhousingsystem.repositories.*;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.utils.HousingSystemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */


@RequiredArgsConstructor
@Service
public class BuyingService {
    @Value("${services.buying.confirmation.url}")
    private  String CONFIRMATION_URL;
    @Value("${services.buying.transfer.url}")
    private  String TRANSFER_URL;
    @Value("${services.buying.transfer.redirect.url}")
    private  String REDIRECT_URL_FOR_TRANSFER;

    private final AdvertisementRepository advertisementRepository;
    private final HousingSystemUtils housingSystemUtils;
    private final ResidentRepository residentRepository;
    private final AdvertisementArchiveRepository advertisementArchiveRepository;
    private final PhotoRepository photoRepository;
    private final MoneyTransferWaitingRepository moneyTransferWaitingRepository;
    private final HTTPRequestWithHMAC requestWithHMAC;


    /**
     *
     * @param pageNumber represents page number for pagination
     * @param pageSize represents page size for pagination
     * @param sortBy represents sorting column for pagination
     * @return List of AdvertisementDTO s
     * If there is not advertisement, <code>AdvertisementNotFoundException</code> exception
     * is thrown
     */
    public List<AdvertisementDTO> getAllAdvertisements(int pageNumber, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Advertisement> advertisementPage = advertisementRepository.findAll(pageable);
        if (advertisementPage.hasContent()){
            return advertisementPage
                    .getContent()
                    .stream()
                    .filter(advertisement -> advertisement.getAdvertisementState().equals(AdvertisementState.ON_SALE))
                    .map(housingSystemUtils::mapAdvertisementToAdvertisementDto)
                    .collect(Collectors.toList());
        } else {
            throw new AdvertisementNotFoundException("there is not advertisement");
        }
    }

    /**
     *
     * @param advertisementUuid is used to uniquely identify an advertisement
     * @return AdvertisementDTO
     * if an advertisement is not found, <code>AdvertisementNotFoundException</code> exception
     * is thrown
     */
    public AdvertisementDTO getAdvertisementByUUID(String advertisementUuid) {
        Advertisement advertisement = advertisementRepository
                                        .findByUuid(advertisementUuid)
                                        .orElseThrow(()->new AdvertisementNotFoundException("Advertisement not found"));
        return housingSystemUtils.mapAdvertisementToAdvertisementDto(advertisement);
    }

    /**
     *
     * @param advertisementUuid is used to uniquely identify an advertisement
     * @param paymentRequestFromUser is PaymentRequestFromUser object
     * @return MoneyTransferUrlDTO
     * <code>AdvertisementNotFoundException</code> exception is thrown if there is not any
     * advertisement with the given advertisementUuid
     * <code>AdvertisementNotOnSaleException</code> exception is thrown if
     * advertisement is not on sale state.
     * <code>PaymentNotSuccessfulException</code> exception is thrown if
     * money transfer is not successful
     */
    @Transactional
    public MoneyTransferUrlDTO getMoneyTransferUrlDTO(String advertisementUuid, PaymentRequestFromUser paymentRequestFromUser) {
        Advertisement advertisement = advertisementRepository
                                        .findByUuid(advertisementUuid)
                                        .orElseThrow(() -> new AdvertisementNotFoundException("Advertisement not found"));

        if (!advertisement.getAdvertisementState().equals(AdvertisementState.ON_SALE)) {
            throw new AdvertisementNotOnSaleException("This advertisement is not currently available on sale.");
        }
        if (!advertisement.getAdvertisementState().equals(AdvertisementState.BOOKED)){
            advertisement.setAdvertisementState(AdvertisementState.BOOKED);
        }

        PaymentRequestToPaymentService paymentRequestToPaymentService =
                                    createPaymentRequestToPaymentService(advertisement,paymentRequestFromUser);

        MoneyTransferUrlDTO moneyTransferUrlDTO = requestWithHMAC.postForMoneyTransfer("transfer_money_to_buy_house",TRANSFER_URL, paymentRequestToPaymentService);

        if (moneyTransferUrlDTO == null || !moneyTransferUrlDTO.isSuccess()) {
            advertisement.setAdvertisementState(AdvertisementState.ON_SALE);
            advertisementRepository.save(advertisement);

            throw new PaymentNotSuccessfulException("payment was not successful");
        }

        MoneyTransferWaiting moneyTransferWaiting = new MoneyTransferWaiting();
        moneyTransferWaiting.setAdvertisementUUID(advertisementUuid);
        moneyTransferWaiting.setReceiverCardNumber(paymentRequestToPaymentService.getReceiverCardNumber());
        moneyTransferWaiting.setSenderCardNumber(paymentRequestToPaymentService.getSenderCardNumber());
        moneyTransferWaitingRepository.save(moneyTransferWaiting);

        return moneyTransferUrlDTO;
    }

    /**
     *
     * @param complateTransferRequest is ComplateTransferRequest object that is
     *                                coming from Citizen account module
     *                                to complete money transfer
     * @return boolean to represent if money transfer is successful or not
     * <code>MoneyTransferWaitingNotFoundException</code> exception is thrown
     * if there is not MoneyTransferWaiting related to ComplateTransferRequest
     * in the database.
     *
     * <code>AdvertisementNotFoundException</code> exception is thrown if
     * there is no advertisement related to ComplateTransferRequest in the database.
     *
     * <code>PaymentNotSuccessfulException</code> exception is thrown if
     * money transfer is not successful.
     *
     * <code>HousingSystemConfirmationNotSuccessful</code> exception is thrown if
     * City Administration module does not confirm ownership change
     */
    public boolean completeMoneyTransfer(ComplateTransferRequest complateTransferRequest){

        MoneyTransferWaiting moneyTransferWaiting =
                moneyTransferWaitingRepository.findBySenderCardNumberAndReceiverCardNumber(
                complateTransferRequest.getSenderCardNumber(),
                complateTransferRequest.getReceiverCardNumber()
        ).orElseThrow(() -> new MoneyTransferWaitingNotFoundException("Money transfer waiting not found")).get(0);


        Advertisement advertisement = advertisementRepository
                .findByUuid(moneyTransferWaiting.getAdvertisementUUID())
                .orElseThrow(() -> new AdvertisementNotFoundException("Advertisement not found"));


        if (!complateTransferRequest.getSuccess()){
            //back to on sale state
            advertisement.setAdvertisementState(AdvertisementState.ON_SALE);
            advertisementRepository.save(advertisement);
            throw new PaymentNotSuccessfulException("payment not successful");
        }

        Resident seller = residentRepository
               .findByResidentCode(complateTransferRequest.getReceiverCardNumber())
               .orElseThrow(() -> new ResidentNotFoundException("Resident(seller) not found"));
        Resident buyer = residentRepository
               .findByResidentCode(complateTransferRequest.getSenderCardNumber())
               .orElseThrow(() -> new ResidentNotFoundException("Resident(buyer) not found"));

        House house = advertisement.getHouse();

        var confirmationRequest = new HousingSystemConfirmationRequest();
        confirmationRequest.setHomeCode(house.getCode());
        confirmationRequest.setBuyerCardNumber(buyer.getResidentCode());
        confirmationRequest.setOwnerCardNumber(seller.getResidentCode());

        HousingSystemConfirmationResponse housingSystemConfirmationResponse =
                requestWithHMAC.postForConfirmationFromCityAdministration("confirm_ownership_change", CONFIRMATION_URL,confirmationRequest);

        if ( housingSystemConfirmationResponse != null && housingSystemConfirmationResponse.isSuccess()) {
            //because transfer is successful.
            // delete house from seller
            seller.removeHouse(house);
            residentRepository.save(seller);
            // add house to buyer
            buyer.addHouse(house);
            residentRepository.save(buyer);
            // add advertisement to archive
            AdvertisementArchive advertisementArchive = createAdvertisementArchive(advertisement, complateTransferRequest.getTransactionId().toString());
            advertisementArchiveRepository.save(advertisementArchive);
            // delete advertisement from the advertisement table
            photoRepository.deleteByAdvertisementId(advertisement.getId());
            advertisementRepository.deleteByUuid(moneyTransferWaiting.getAdvertisementUUID());

            moneyTransferWaitingRepository.delete(moneyTransferWaiting);

        } else {
            throw  new HousingSystemConfirmationNotSuccessful("confirmation is not successful");
        }

        return true;
    }

    /**
     *
     * @param advertisement Advertisement object
     * @param moneyTransferUUID taken from Citizen account module during
     *                          the first step of money transfer
     * @return AdvertisementArchive
     */
    private AdvertisementArchive createAdvertisementArchive(Advertisement advertisement, String moneyTransferUUID) {
        return AdvertisementArchive
                .builder()
                .description(advertisement.getDescription())
                .phone(advertisement.getPhone())
                .price(advertisement.getPrice())
                .title(advertisement.getTitle())
                .residentCode(advertisement.getResident().getResidentCode())
                .moneyTransferUUID(moneyTransferUUID)
                .build();
    }

    /**
     *
     * @param advertisement Advertisement object
     * @param paymentRequestFromUser PaymentRequestFromUser object
     * @return PaymentRequestToPaymentService
     */
    private PaymentRequestToPaymentService createPaymentRequestToPaymentService(Advertisement advertisement,PaymentRequestFromUser paymentRequestFromUser){
        final String baseUrl =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(); // gets base url

        return PaymentRequestToPaymentService
                .builder()
                .serviceName("HOUSING_SYSTEM")
                .redirectUrl(baseUrl + REDIRECT_URL_FOR_TRANSFER)
                .amount(advertisement.getPrice())
                .receiverCardNumber(advertisement.getResident().getResidentCode())
                .senderCardNumber(paymentRequestFromUser.getResidentCardNumberSender())
                .description(paymentRequestFromUser.getDescription())
                .build();
    }
}
