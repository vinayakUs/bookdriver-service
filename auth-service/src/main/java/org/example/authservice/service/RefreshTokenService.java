package org.example.authservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.exception.InvalidTokenRequestException;
import org.example.authservice.exception.TokenRefreshException;
import org.example.authservice.model.DeviceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class RefreshTokenService {

    @Value("${Jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${app.token.refresh.duration}")
    private Integer refreshTokenExpiresIn;

    /**
     * Creates and returns a new refresh token
     */
    public String refreshToken(String sub, DeviceInfo deviceInfo) {
        String deviceId = deviceInfo
                .getDeviceIdentifier();

        return  Jwts.builder()
                .subject(sub)
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

    /**
     * Checks the for sorts of things
     */

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).build().parse(authToken);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw new InvalidTokenRequestException("JWT", authToken, "Incorrect signature");

        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Malformed jwt token");

        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Token expired. Refresh required");

        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");

        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
        }
        return true;
    }

    /**
     * Returns Refresh token . for now dummy method but can be used to implement save of tokens in storage.
     */

    public Optional<String> getRefreshToken(String refreshToken) {
        return Optional.of(refreshToken);

    }
    /**
     * validate the device against the refresh token
     */
    public boolean validateDeviceForToken(String refreshToken, DeviceInfo deviceInfo) {
        String deviceId = deviceInfo.getDeviceIdentifier();
        String deviceIdClaim = getClaimsFromToken(refreshToken).get("deviceId", String.class);
        if(!deviceId.equals(deviceIdClaim)) {
            throw new TokenRefreshException(refreshToken,"Device claims doesnt match, Please login");
        }
        return true;
    }

    /**
     * Returns claims
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).build().parseSignedClaims(token).getPayload();
    }

}
