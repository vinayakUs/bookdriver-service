package org.example.authservice.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenReqeust
{
    @NotEmpty(message = "Empty token provided")
    private String refreshToken;

}
