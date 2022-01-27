package com.epam.smartcityhousingsystem.security.hmac;

/**
 * Interface {@code HMACUtilService} for creating and checking signature
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

public interface HMACUtilService {

    /**
     * To create hashed signature
     * @param keyId Unique key between the two components
     * @param timestamp Long value of time, it must be 5 minutes ahead from now
     * @param action Short description of action(get_resident_info)
     * @param secretKey Secret key between the two components
     * @return Hash value
     */
    String calculateHASH(String keyId, String timestamp, String action, String secretKey);

    /**
     * To check signature
     * @param keyId Taken from request header
     * @param timestamp Taken from request header
     * @param action Taken from request header
     * @param signature Taken from request header
     * @return true - if this request can access, false - if this request can't access
     */
    boolean hasAccess(String keyId, String timestamp, String action, String signature);
}
