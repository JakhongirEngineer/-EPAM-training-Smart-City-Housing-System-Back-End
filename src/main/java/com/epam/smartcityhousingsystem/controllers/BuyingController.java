package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.AdvertisementDTO;
import com.epam.smartcityhousingsystem.dtos.ComplateTransferRequest;
import com.epam.smartcityhousingsystem.dtos.MoneyTransferUrlDTO;
import com.epam.smartcityhousingsystem.dtos.PaymentRequestFromUser;
import com.epam.smartcityhousingsystem.security.hmac.AuthorizeRequest;
import com.epam.smartcityhousingsystem.services.BuyingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * AdvertisementController can only
 * be accessed with RESIDENT role.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@RequestMapping(value = "/api/v1/buying")
@AllArgsConstructor
@RestController
public class BuyingController {

    private final BuyingService buyingService;

    @ApiOperation(value = "get all advertisements",
            notes = "get all advertisements using pagination",
            response = AdvertisementDTO.class, responseContainer = "List")

    @GetMapping("/advertisements")
    public ResponseEntity<List<AdvertisementDTO>> getAllAdvertisements(
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "price") String sortBy ) {

        List<AdvertisementDTO> ads = buyingService.getAllAdvertisements(pageNumber,pageSize, sortBy);
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }
    @ApiOperation(value = "get advertisement",
            notes = "a resident who wants to buy a house sees one advertisement",
            response = AdvertisementDTO.class
    )
    @GetMapping("/advertisements/{advertisementUuid}")
    public ResponseEntity<AdvertisementDTO> getAdvertisement(@ApiParam(value = "advertisementUuid", required = true) @PathVariable String advertisementUuid) {
        AdvertisementDTO advertisementDto = buyingService.getAdvertisementByUUID(advertisementUuid);
        return new ResponseEntity<>(advertisementDto, HttpStatus.OK);
    }

    @ApiOperation(value = "start money transfer",
            notes = "resident starts money transfer",
            response = String.class
    )

    @PostMapping("/advertisements/{advertisementUuid}")
    public ResponseEntity<MoneyTransferUrlDTO> startMoneyTransfer(@ApiParam(value = "advertisementUuid", required = true) @PathVariable String advertisementUuid,
                                                                  @ApiParam(value = "paymentRequestFromUser", required = true) @Valid @RequestBody PaymentRequestFromUser paymentRequestFromUser) {
        MoneyTransferUrlDTO moneyTransferUrlDTO = buyingService.getMoneyTransferUrlDTO(advertisementUuid, paymentRequestFromUser);
        return new ResponseEntity<>(moneyTransferUrlDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "complete money transfer",
            notes = "resident account module sends response about transfer result ",
            response = String.class
    )
    @AuthorizeRequest
    @PostMapping("/accountservice/transfermoney")
    public ResponseEntity<HashMap<String,Boolean>> completeMoneyTransfer(@ApiParam(value = "complateTransferRequest", required = true) @RequestBody ComplateTransferRequest complateTransferRequest){
        buyingService.completeMoneyTransfer(complateTransferRequest);
        HashMap<String,Boolean> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transfer-result")
    public String transferResult(){
        return "You successfully made money transfer";
    }
}
