package com.epam.smartcityhousingsystem.services;

import com.epam.smartcityhousingsystem.dtos.HousingSystemConfirmationRequest;
import com.epam.smartcityhousingsystem.dtos.HousingSystemConfirmationResponse;
import com.epam.smartcityhousingsystem.dtos.MoneyTransferUrlDTO;
import com.epam.smartcityhousingsystem.dtos.PaymentRequestToPaymentService;
import com.epam.smartcityhousingsystem.exceptions.GetFinalResponseException;
import com.epam.smartcityhousingsystem.exceptions.PostForConfirmationFromCityAdministrationException;
import com.epam.smartcityhousingsystem.exceptions.PostForMoneyTransferException;
import com.epam.smartcityhousingsystem.security.hmac.HMACUtil;
import com.epam.smartcityhousingsystem.utils.apiEntities.FinalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Date;

/**
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */


@RequiredArgsConstructor
@Service
public class HTTPRequestWithHMAC {
    @Value("${hmac.keyId:HOUSING_SYSTEM}")
    private String HMAC_KEY_ID;
    @Value("${hmac.secretKey:housingSystemKey}")
    private String HMAC_SECRET_KEY;

    private final HMACUtil hmacUtil;

    /**
     * provides post request capability for MoneyTransferUrlDTO with HMAC
     * @param action specifies action header
     * @param path specifies path header
     * @param paymentRequest PaymentRequestToPaymentService object
     * @return MoneyTransferUrlDTO
     */
    public MoneyTransferUrlDTO postForMoneyTransfer(String action, String path, PaymentRequestToPaymentService paymentRequest) {
        HttpHeaders headers = generateHeaders(action);
        HttpEntity<Object> httpEntity = new HttpEntity<>(paymentRequest,headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
         return restTemplate.exchange(
                    URI.create(path),
                    HttpMethod.POST,
                    httpEntity,
                 MoneyTransferUrlDTO.class).getBody();
        } catch (HttpClientErrorException e) {
            throw new PostForMoneyTransferException("Money transfer does not start properly {postForMoneyTransfer}");
        }
    }

    /**
     * provides post request capability for HousingSystemConfirmationResponse with HMAC
     * @param action specifies action header
     * @param path specifies path header
     * @param request HousingSystemConfirmationRequest
     * @return HousingSystemConfirmationResponse
     */
    public HousingSystemConfirmationResponse postForConfirmationFromCityAdministration(String action, String path, HousingSystemConfirmationRequest request) {
        HttpHeaders headers = generateHeaders(action);
        HttpEntity<Object> httpEntity = new HttpEntity<>(request,headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(
                    URI.create(path),
                    HttpMethod.POST,
                    httpEntity,
                    HousingSystemConfirmationResponse.class).getBody();
        } catch (HttpClientErrorException e) {
            throw new PostForConfirmationFromCityAdministrationException("exception while getting confirmation from City Administration");
        }
    }

    /**
     * provides put request capability for String with HMAC
     * @param action specifies action header
     * @param path specifies path header
     */
    public void put(String action, String path) {
        HttpHeaders headers = generateHeaders(action);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(
                    URI.create(path),
                    HttpMethod.PUT,
                    httpEntity,
                    String.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * provides get request capability for FinalResponse with HMAC
     * @param action specifies action header
     * @param path specifies path header
     * @return FinalResponse
     */
    public FinalResponse getFinalResponse(String action, String path) {
        HttpHeaders headers = generateHeaders(action);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
             return restTemplate.exchange(URI.create(path), HttpMethod.GET, httpEntity, FinalResponse.class).getBody();
        } catch (Exception e){
            throw new GetFinalResponseException("exception while getting a final response from City Administration module");
        }
    }

    /**
     * generates HttpHeaders for HMAC
     * @param action specifies action header content
     * @return HttpHeaders
     */
    private HttpHeaders generateHeaders(String action) {
        final String timestamp = String.valueOf(new Date().getTime() + 300000);
        final String signature = hmacUtil.calculateHASH(HMAC_KEY_ID, timestamp, action, HMAC_SECRET_KEY);
        HttpHeaders headers = new HttpHeaders();
        headers.set("sm-keyId", HMAC_KEY_ID);
        headers.set("sm-timestamp", timestamp);
        headers.set("sm-action", action);
        headers.set("sm-signature", signature);
        return headers;
    }
}
