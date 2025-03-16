package org.example.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.authservice.model.DeviceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class RefreshTokenService {

    @Value("${Jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${app.token.refresh.duration}")
    private Integer refreshTokenExpiresIn;

    /**
     * Creates and returns a new refresh token
     */
    public String refreshToken(String username, DeviceInfo deviceInfo) {
        String deviceId = deviceInfo
                .getDeviceIdentifier();

        return  Jwts.builder()
                .subject(username)
                .claim("deviceId", deviceId)
                .expiration(Date.from(Instant.now().plusMillis(refreshTokenExpiresIn)))
                .issuedAt(Date.from(Instant.now()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                .compact();

    }
    /**
     * Return the jwt expiration for the client so that they can execute
     * the refresh token logic appropriately
     */
    public long getExpiryDuration() {
        return refreshTokenExpiresIn;
    }


}
