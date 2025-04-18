package org.example.gateway.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.gateway.exception.JwtValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${app.jwt.key}")
    private String jwtKey;

    private SecretKey getPublicKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }

    public Claims validateToken(String token) {

        try {
            return Jwts.parser().verifyWith(getPublicKey()).build().parseSignedClaims(token).getPayload();
        }  catch (ExpiredJwtException ex) {
            throw new JwtValidationException("GW : Token has expired", ex);
        } catch (UnsupportedJwtException ex) {
            throw new JwtValidationException("GW : Token is unsupported", ex);
        } catch (MalformedJwtException ex) {
            throw new JwtValidationException("GW : Token is malformed", ex);
        } catch (SecurityException | io.jsonwebtoken.security.SignatureException ex) {
            throw new JwtValidationException("GW : Token signature is invalid", ex);
        } catch (IllegalArgumentException ex) {
            throw new JwtValidationException("GW : Token is empty or null", ex);
        }
    }
}
