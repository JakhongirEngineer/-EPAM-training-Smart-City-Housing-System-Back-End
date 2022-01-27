package com.epam.smartcityhousingsystem.security;

import com.epam.smartcityhousingsystem.dtos.SignInRequest;
import com.epam.smartcityhousingsystem.dtos.SignInResponse;
import com.epam.smartcityhousingsystem.exceptions.UserNotFoundException;
import com.epam.smartcityhousingsystem.repositories.ResidentRepository;
import com.epam.smartcityhousingsystem.repositories.UserRepository;
import com.epam.smartcityhousingsystem.utils.JWTUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

/**
 *<code>JWTService</code> is a service class that
 * is used to authorize requests based on JWT tokens.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Service
@Primary
public class JWTService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final ResidentRepository residentRepository;

    public JWTService(@Lazy UserRepository userRepository,
                      @Lazy AuthenticationManager authenticationManager,
                      @Lazy JWTUtils jwtUtils,
                      @Lazy ResidentRepository residentRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.residentRepository = residentRepository;
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();
        User user =  userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("user not found with email: " + email))
                    .get(0);
        final UserDetails userDetails = loadUserByUsername(email);
        String jwtToken = jwtUtils.generateToken(userDetails);
        authenticate(email, password);

        long residentCode = 0;
        Set<Role> roles = user.getRoles();
        for (Role role : roles){
            if (role.getName().equals("RESIDENT")){
                Optional<Resident> residentOptional = residentRepository.findByEmail(email);
                if (residentOptional.isPresent()) {
                    residentCode = residentOptional.get().getResidentCode();
                }
            }
        }


        return new SignInResponse(user,jwtToken, residentCode);
    }

    private void authenticate(String username, String password) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("user does not exist with email: " + email))
                .get(0);
        return new UserDetailsImpl(user);
    }
}
