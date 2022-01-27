package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.entities.AdvertisementArchive;
import com.epam.smartcityhousingsystem.services.AdvertisementArchiveService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdvertisementArchiveController endpoints can only
 * be accessed with ADMIN role
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */
@RequestMapping("/api/v1/advertisementArchives")
@RequiredArgsConstructor
@RestController
public class AdvertisementArchiveController {
    private final AdvertisementArchiveService advertisementArchiveService;

    @ApiOperation(value = "get all advertisement archives", response = AdvertisementArchive.class, responseContainer = "List")
    @GetMapping
    public ResponseEntity<List<AdvertisementArchive>> getAllAdvertisementArchives(
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "price") String sortBy ){

        List<AdvertisementArchive> archives = advertisementArchiveService.getAllAdvertisementArchives(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(archives, HttpStatus.OK);
    }
    @ApiOperation(value = "get advertisement archive",
            notes = "gets one advertisement archive by its UUID",
            response = AdvertisementArchive.class
    )
    @GetMapping("/{advertisementArchiveUUID}")
    public ResponseEntity<AdvertisementArchive> getAdvertisementArchive( @ApiParam(value = "advertisementArchiveUUID", required = true)
                                                                         @PathVariable String advertisementArchiveUUID){
        AdvertisementArchive archive = advertisementArchiveService.getAdvertisementArchive(advertisementArchiveUUID);
        return new ResponseEntity<>(archive, HttpStatus.OK);
    }
    @ApiOperation(value = "delete advertisement archive",
            notes = "deletes one advertisement archive by its UUID",
            response = Boolean.class
    )
    @DeleteMapping("/{advertisementArchiveUUID}")
    public ResponseEntity<Boolean> deleteAdvertisementArchive( @ApiParam(value = "advertisementArchiveUUID", required = true)
                                                               @PathVariable String advertisementArchiveUUID) {
        Boolean result = advertisementArchiveService.deleteAdvertisementArchive(advertisementArchiveUUID);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
