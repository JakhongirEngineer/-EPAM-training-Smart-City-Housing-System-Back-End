package com.epam.smartcityhousingsystem.security.hmac;


import com.epam.smartcityhousingsystem.repositories.HMACSecretKeyRepository;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Date;

/**
 * HMACUtil is a utility class for HMAC based authorization.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Component
public class HMACUtil implements HMACUtilService{


    public static void main(String[] args) {
        long now = new Date().getTime() + 86400;
        System.out.println(now);
        String s = HMACUtil.calculateHASH2("HOUSING_SYSTEM", String.valueOf(now), "transfer_money", "housingSystemKey");
        System.out.println(s);
    }

    private final HMACSecretKeyRepository hmacSecretKeyRepository;

    public HMACUtil(HMACSecretKeyRepository hmacSecretKeyRepository) {
        this.hmacSecretKeyRepository = hmacSecretKeyRepository;
    }

    /**
     * Checking request
     * @param keyId from request header
     * @param timestamp from request header
     * @param action from request header
     * @param signature from request header
     * @return result of checking request (true - has access, false - has not access)
     */
    public boolean hasAccess(String keyId, String timestamp, String action, String signature) {
        long now = new Date().getTime(); // keyId = HOTEL, timp: 2222532252, action: get_resident, signature: gfdfg455464==
        String secretKey = hmacSecretKeyRepository.takeHMACSecretKeyByComponentName(keyId);
        if(now>Long.parseLong(timestamp) && secretKey==null) {
            return false;
        }
        String testSignature = calculateHASH(keyId, timestamp, action, secretKey);
        return testSignature.equals(signature);
    }

    /**
     * To calculate HASH signature
     */
    public String calculateHASH(String keyId, String timestamp, String action, String secretKey) {
        String data = "keyId="+keyId+";timestamp="+timestamp+";action="+action;  //keyId=HOTEL;timestamp=12345465;action=get_residents
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return new String(Base64.getEncoder().encode(rawHmac));
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException();
        }
    }


    public static String calculateHASH2(String keyId, String timestamp, String action, String secretKey) {
        String data = "keyId="+keyId+";timestamp="+timestamp+";action="+action;  //keyId=HOTEL;timestamp=12345465;action=get_citizen_info
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return new String(Base64.getEncoder().encode(rawHmac));
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException();
        }
    }
}

