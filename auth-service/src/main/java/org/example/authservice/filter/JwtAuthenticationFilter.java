package org.example.authservice.filter;

import org.example.authservice.exception.InvalidTokenRequestException;
import org.example.authservice.security.JwtTokenProvider;
import org.example.authservice.security.JwtTokenValidator;
import org.example.authservice.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${app.jwt.header}")
    private String header;

    @Value("${app.jwt.header.prefix}")
    private String headerPrefix;

    private final JwtTokenValidator jwtTokenValidator;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String jwt = getJwtFromHeader(request);

            if (jwt != null && jwtTokenValidator.validateToken(jwt)) {

                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                log.info("User id in - " + String.valueOf(userId));
                UserDetails userDetails = userDetailService.loadUserById(userId);
                log.info("User Details in - " + userDetails.toString());

                List<GrantedAuthority> ga = jwtTokenProvider.getAuthoritiesFromJwt(jwt);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, jwt, ga);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                log.info("Request dosent have jwt");
            }
        } catch (InvalidTokenRequestException ex) {
            log.error("Failed to set Security Context");
            throw ex;
        }
        filterChain.doFilter(request, response);

    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(header);

        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix)) {
            log.info("Extracted Token : " + bearerToken);
            return bearerToken.replace(headerPrefix, "");
        }
        return null;
    }

}
