package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.HouseDTO;
import com.epam.smartcityhousingsystem.dtos.ResidentDTO;
import com.epam.smartcityhousingsystem.services.AdminService;
import com.epam.smartcityhousingsystem.utils.DataManagerWithCityAdministration;
import com.epam.smartcityhousingsystem.utils.apiEntities.FinalResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminController endpoints can only be accessed
 * by ADMIN role
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final DataManagerWithCityAdministration dataManager;
    private  final AdminService adminService;
    private FinalResponse finalResponse;

    @ApiOperation(value = "gets ownerships from city administration",
            notes = "based on that, the server generates data",
            response = FinalResponse.class
    )
    @GetMapping("/cityAdministration")
    public ResponseEntity<FinalResponse> getFinalResponse(){
        if (finalResponse == null) {
            finalResponse = dataManager.getFinalResponse();
            dataManager.populateDB(finalResponse.getResult());
        }
        return new ResponseEntity<>(finalResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "get all residents",
            notes = "retrieves all resident information using pagination",
            response = ResidentDTO.class, responseContainer = "List"
    )

    @GetMapping("/residents")
    public ResponseEntity<List<ResidentDTO>> getAllResidents(
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "firstName") String sortBy) {

        List<ResidentDTO> residentDTOs = adminService.getAllResidentsAsDTOs(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(residentDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "get house", notes = "get house information of a particular resident", response = HouseDTO.class)
    @GetMapping("/residents/{residentCode}/{houseCode}")
    public ResponseEntity<HouseDTO> getHouse(@ApiParam(value = "residentCode", required = true) @PathVariable long residentCode,
                                             @ApiParam(value = "houseCode", required = true) @PathVariable long houseCode) {
        HouseDTO houseDTO = adminService.getHouse(residentCode, houseCode);
        return new ResponseEntity<>(houseDTO, HttpStatus.OK);
    }
}
