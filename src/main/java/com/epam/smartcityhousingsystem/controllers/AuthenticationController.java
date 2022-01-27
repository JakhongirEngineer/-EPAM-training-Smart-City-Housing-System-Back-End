package com.epam.smartcityhousingsystem.controllers;

import com.epam.smartcityhousingsystem.dtos.SignInRequest;
import com.epam.smartcityhousingsystem.dtos.SignInResponse;
import com.epam.smartcityhousingsystem.dtos.SignUpResponse;
import com.epam.smartcityhousingsystem.dtos.SignUpRequest;
import com.epam.smartcityhousingsystem.security.JWTService;
import com.epam.smartcityhousingsystem.services.HousingSystemAuthenticationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * AuthenticationController endpoints are
 * used to sign in and to sign up the application
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@CrossOrigin
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final HousingSystemAuthenticationService authenticationService;
    private final JWTService jwtService;

    @ApiOperation(value = "sign up", response = SignUpResponse.class)
    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> signUp(@ApiParam(value = "signUpRequest", required = true) @Valid @RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "sign in", response = SignInResponse.class)
    @PostMapping("/signIn")
    public ResponseEntity<SignInResponse> signIn(@ApiParam(value = "signInRequest", required = true) @Valid @RequestBody SignInRequest signInRequest) {
        SignInResponse signInResponse = jwtService.signIn(signInRequest);
        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }

}
