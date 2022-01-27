package com.epam.smartcityhousingsystem.security.hmac;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthorizeRequestAspect is a custom aspect
 * that intercepts method calls before method is called
 * and after method returns a result.
 * It extracts hmac specific headers from the request, and
 * checks if request can be chained or not.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@Aspect
@Component
public class AuthorizeRequestAspect {

    private final HMACUtil hmacUtil;

    public AuthorizeRequestAspect(HMACUtil hmacUtil) {
        this.hmacUtil = hmacUtil;
    }

    @Around("@annotation(AuthorizeRequest)")
    public Object authorizeRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String keyId = request.getHeader("sm-keyId"); // RECREATION
        String timestamp = request.getHeader("sm-timestamp"); // 26565956
        String action = request.getHeader("sm-action"); // get_resident
        String signature = request.getHeader("sm-signature"); // sdasfsgsd465463d5fsfes==
        if(keyId==null || timestamp==null || action==null || signature==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Headers are null!");
        }
        boolean hasAccess = hmacUtil.hasAccess(keyId, timestamp, action, signature);
        if(!hasAccess) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized!");
        }
        return joinPoint.proceed();
    }
}
