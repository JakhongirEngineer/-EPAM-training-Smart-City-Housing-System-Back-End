package com.epam.smartcityhousingsystem.controllers;
import com.epam.smartcityhousingsystem.dtos.AdvertisementDTO;
import com.epam.smartcityhousingsystem.services.AdvertisementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * AdvertisementController can only
 * be accessed with RESIDENT role.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@AllArgsConstructor
@RequestMapping("/api/v1/advertisements")
@RestController
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @ApiOperation(value = "create advertisement",
            notes = "provides residentCode and advertisementDto to create an advertisement",
            response = Boolean.class
    )
    @PostMapping({"/{residentCode}"})
    public ResponseEntity<Boolean> createAdvertisement(@ApiParam(value = "advertisementDto", required = true) @Valid @RequestBody AdvertisementDTO advertisementDto,
                                                       @ApiParam(value = "residentCode", required = true) @PathVariable long residentCode){
        boolean result = advertisementService.createAdvertisement(residentCode ,advertisementDto);
        if (result) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "get advertisements",
            notes = "provides residentCode to get all advertisements a resident owns",
            response = AdvertisementDTO.class, responseContainer = "Set"
    )

    @GetMapping({  "/{residentCode}" })
    public ResponseEntity<Set<AdvertisementDTO>> getAdvertisements( @ApiParam(value = "residentCode", required = true) @PathVariable(name = "residentCode") long residentCode) {

        Set<AdvertisementDTO> ads = advertisementService.getAdvertisementDTOsByResidentCode(residentCode);
        if(ads.size() > 0) {
            return new ResponseEntity<>( ads, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation(value = "get advertisement",
            notes = "provides residentCode and advertisement UUID to get an advertisement a resident owns",
            response = AdvertisementDTO.class
    )
    @GetMapping("/{residentCode}/{uuid}")
    public ResponseEntity<AdvertisementDTO> getAdvertisement(@ApiParam(value = "residentCode", required = true) @PathVariable long residentCode,
                                                             @ApiParam(value = "uuid", required = true) @PathVariable String uuid){
        AdvertisementDTO ad = advertisementService.getAdvertisementDTOByUUID(residentCode, uuid);
        if (ad == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ad, HttpStatus.OK);
    }

    @ApiOperation(value = "update advertisement",
            notes = "provides residentCode and advertisement UUID to update an advertisement a resident owns",
            response = AdvertisementDTO.class
    )
    @PutMapping("/{residentCode}/{uuid}")
    public ResponseEntity<AdvertisementDTO> updateAdvertisement(@ApiParam(value = "advertisementDto", required = true) @Valid @RequestBody AdvertisementDTO advertisementDto,
                                                                @ApiParam(value = "residentCode", required = true) @PathVariable long residentCode,
                                                                @ApiParam(value = "uuid", required = true) @PathVariable String uuid) {
        AdvertisementDTO updatedAdvertisementDTO = advertisementService.updateAdvertisement( residentCode, uuid, advertisementDto);
        return new ResponseEntity<>(updatedAdvertisementDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "delete advertisement",
            notes = "provides residentCode and advertisement UUID to delete an advertisement a resident owns",
            response = Boolean.class
    )
    @DeleteMapping("/{residentCode}/{uuid}")
    public ResponseEntity<Boolean> deleteAdvertisement(@ApiParam(value = "residentCode", required = true) @PathVariable long residentCode,
                                                       @ApiParam(value = "uuid", required = true) @PathVariable String uuid) {
        boolean result = advertisementService.deleteAdvertisement(residentCode, uuid);
        if (!result) {
            return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
