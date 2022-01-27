package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.SignUpRequest;
import com.epam.smartcityhousingsystem.dtos.SignUpResponse;
import com.epam.smartcityhousingsystem.exceptions.ConfirmationPasswordDoesNotMatchException;
import com.epam.smartcityhousingsystem.exceptions.ResidentNotFoundException;
import com.epam.smartcityhousingsystem.exceptions.RoleNotFoundException;
import com.epam.smartcityhousingsystem.exceptions.UserHasAlreadySignedUpException;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.repositories.RoleRepository;
import com.epam.smartcityhousingsystem.repositories.UserRepository;
import com.epam.smartcityhousingsystem.security.Resident;
import com.epam.smartcityhousingsystem.security.Role;
import com.epam.smartcityhousingsystem.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */


@RequiredArgsConstructor
@Service
public class HousingSystemAuthenticationService {
    private final ResidentRepository residentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     *
     * @param signUpRequest SignUpRequest object
     * @return SignUpResponse
     *
     * <code>ResidentNotFoundException</code> exception is thrown if
     * resident is not found with a given email in signUpRequest.
     *
     * <code>UserHasAlreadySignedUpException</code> exception is thrown if
     * user has already signed up with a given email in signUpRequest.
     *
     * <code>ConfirmationPasswordDoesNotMatchException</code> exception is thrown if
     * user password and confirmation password does not match.
     *
     * <code>RoleNotFoundException</code> exception is thrown if there is not found
     * RESIDENT role in the database.
     */
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        Resident resident = residentRepository
                            .findByEmail(signUpRequest.getEmail())
                            .orElseThrow(() -> new ResidentNotFoundException("resident not found with the provided email"));
        if (resident.isSignedUp()) {
            throw new UserHasAlreadySignedUpException("Resident was already signed up");
        }
        if (!signUpRequest.isConfirmationPasswordTheSameAsPassword()){
            throw new ConfirmationPasswordDoesNotMatchException("password with confirmation password don't match");
        }
        resident.setSignedUp(true);
        resident.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        residentRepository.save(resident);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("RESIDENT").orElseThrow(() -> new RoleNotFoundException("RESIDENT role not found")));
        User registeredUser = User
                .builder()
                .password(resident.getPassword())
                .signedUp(true)
                .email(resident.getEmail())
                .enabled(resident.isEnabled())
                .firstName(resident.getFirstName())
                .lastName(resident.getLastName())
                .tokenExpired(false)
                .roles(roles)
                .build();
        userRepository.save(registeredUser);

        return new SignUpResponse("You are successfully signed up. You need to remember your card number.",resident.getResidentCode());
    }
}
