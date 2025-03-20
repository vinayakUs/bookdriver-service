package org.example.authservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.example.authservice.model.DeviceInfo;

import java.util.UUID;

public class Utils {

    public static String getClientIpAddress(HttpServletRequest httpServletRequest) {
        String clientIp = httpServletRequest.getHeader("X-Forwarded-For");
        return clientIp == null||clientIp.isEmpty() ?httpServletRequest.getRemoteAddr() : clientIp.split(",")[0];
//        return clientIp;
    }

    public static String generateRandomUuid(){
        return UUID.randomUUID().toString();
    }
}
