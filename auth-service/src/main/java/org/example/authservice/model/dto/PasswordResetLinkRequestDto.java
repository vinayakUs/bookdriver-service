package org.example.authservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetLinkRequestDto {

    @NotBlank
    @Email(message = "Invalid Email Format")
    private String email;

    public PasswordResetLinkRequestDto(String email){
        this.email=email;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    
    
}
