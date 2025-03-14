package org.example.authservice.security;

import org.example.authservice.model.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    public static String AUTHORITIES_CLAIM = "authorities";

    @Value("${Jwt.jwtExpirationInMs}")
    private Integer jwtExpirationInMs;

    @Value("${Jwt.jwtSecret}")
    private String jwtSecret;

    /**
     * Private helper method to extract user authorities.
     */
    private String getUserAuthorities(CustomUserDetails customUserDetails) {
        return customUserDetails.getAuthorities().stream().map(role -> role.getAuthority())
                .collect(Collectors.joining(","));
    }

    /**
     * Generates a token from a principal object
     */
    public String generateToken(CustomUserDetails customUserDetails) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        String authorities = getUserAuthorities(customUserDetails);
        return Jwts.builder()
                .subject(Long.toString(customUserDetails.getId()))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiryDate))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                // .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .claim(AUTHORITIES_CLAIM, authorities)
                .compact();
    }

    /**
     * Return the jwt expiration for the client so that they can execute
     * the refresh token logic appropriately
     */
    public long getExpiryDuration() {
        return jwtExpirationInMs;
    }

    /*
     * Return Userid encapsulated in token
     */
    public Long getUserIdFromJWT(String token) {
        var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        System.out.println(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject());
        return Long.parseLong(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject());
    }

    /*
     * Returns Claims as List<GrantedAuthority> encapsulated within the token.
     */

    public List<GrantedAuthority> getAuthoritiesFromJwt(String jwt) {
        String roles = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))).build()
                .parseSignedClaims(jwt).getPayload().get(AUTHORITIES_CLAIM, String.class);
        return Arrays.stream(roles.toString().split(",")).map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

}
