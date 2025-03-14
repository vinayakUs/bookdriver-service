package org.example.authservice.utils;

import java.util.UUID;

public class Util {
    
    public static String generateRandomUuid(){
        return UUID.randomUUID().toString();
    }
}
