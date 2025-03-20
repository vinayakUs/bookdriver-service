package org.example.authservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "Password reset Request" , description = "Password reset request Payload")
@Getter
@Setter
@ToString
public class PasswordResetRequestDto {

    private String email;
    private String password;
    private String confirmPassword;
    private String token;

    
}
