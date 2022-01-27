package com.epam.smartcityhousingsystem.configuration.constants;

/**
 *  Constants for security configuration
 *
 * @author Jakhongir Rasulov on 11/1/21
 * @project smartcity-housing-system
 */
public class SecurityConstants {
    public static final String ADMIN = "ADMIN";
    public static final String RESIDENT = "RESIDENT";
    // public urls can be accessed without logging in the application
    public static final String[] PUBLIC_URLS = {
            "/api/v1/authentication/signUp",
            "/api/v1/authentication/signIn",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/api/v1/buying/transfer-result",
            "/api/v1/buying/accountservice/transfermoney"  // for resident account module
    };


    /**
     * URL patters that can be accessed by RESIDENT role
     */
    public static final String[] RESIDENT_URLS = {
            "/api/v1/advertisements/**",
            "/api/v1/buying/**",
            "/api/v1/residents/**"
    };

    /**
     * URL patters that can be accessed by ADMIN role
     */
    public static final String[] ADMIN_URLS = {
            "/api/v1/admin/**",
            "/api/v1/advertisementArchives/**"
    };
}
