package com.epam.smartcityhousingsystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWTUtils is a utility class for JWT based authentication
 * for other service classes.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Component
public class JWTUtils {

    private static final String SECRET_KEY = "SECRET";
    private static final long TOKEN_VALIDITY_IN_SECONDS = 60000;

    /**
     * extracts username from jwtToken
     *
     * @param jwtToken String representing jwt token
     * @return String username
     */
    public String getUserNameFromJWTToken(String jwtToken){
        return getClaimFromJWTToken(jwtToken, Claims::getSubject);
    }

    /**
     * extracts Claims from jwtToken
     *
     * @param jwtToken String representing jwt token
     * @return Claims
     */
    private Claims getAllClaimsFromJWTToken(String jwtToken) {
        return Jwts
                .parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     *
     * @param jwtToken String representing jwt token
     * @param claimResolver Function on which claims are applied
     */
    private <T> T getClaimFromJWTToken(String jwtToken, Function<Claims, T> claimResolver) {
        Claims claims = getAllClaimsFromJWTToken(jwtToken);
        return claimResolver.apply(claims);
    }

    /**
     * validates token: if token belongs to a user, and it has not expired,
     * returns true, otherwise false.
     *
     * @param jwtToken String representing jwt token
     * @param userDetails UserDetails
     * @return boolean
     */
    public boolean validateToke(String jwtToken, UserDetails userDetails) {
        String username = getUserNameFromJWTToken(jwtToken);
        return ( username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken) );
    }

    /**
     * checks if jwtToken is expired
     * @param jwtToken String representing jwt token
     * @return boolean if token is expired, returns true, otherwise false.
     */
    public boolean isTokenExpired(String jwtToken) {
        Date expirationDate = getExpirationDateFromToken(jwtToken);
        return expirationDate.before(new Date());
    }

    /**
     * extracts expiration date from token
     *
     * @param jwtToken String representing jwt token
     * @return Date
     */
    public Date getExpirationDateFromToken(String jwtToken) {
        return getClaimFromJWTToken(jwtToken, Claims::getExpiration);
    }

    /**
     * generates jwt token
     * @param userDetails UserDetails
     * @return String jwt token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_IN_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact()
                ;
    }
}
