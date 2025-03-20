package org.example.authservice.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public  class TimeParserUtil {
    
    public static ZonedDateTime getIstTime(){
        return Instant.now().atZone(ZoneId.of("Asia/Kolkata"));
    }
}
