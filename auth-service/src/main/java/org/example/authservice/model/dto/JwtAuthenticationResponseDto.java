package org.example.authservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponseDto {
    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long accessExpiryDuration;

    private Long refreshExpiryDuration;


    public JwtAuthenticationResponseDto(String accessToken, String refreshToken, Long accessExpiryDuration, Long refreshExpiryDuration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpiryDuration = accessExpiryDuration;
        this.refreshExpiryDuration = refreshExpiryDuration;

        tokenType = "Bearer ";
    }

}
