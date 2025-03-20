package org.example.authservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Login Request", description = "The login request payload")
public class LoginRequestDto {

//    @NotNull(message = "Login Username can be null but not blank")
//    @Schema(name = "Registered username", allowableValues = "NonEmpty String")
//    private String username;

    @NotNull(message = "Login Email can be null but not blank")
    @Schema(name = "User registered email", allowableValues = "NonEmpty String")
    private String email;

    @NotNull(message = "Login password cannot be blank")
    @Schema(name = "Valid user password", allowableValues = "NonEmpty String")
    private String password;

    // @Valid
    // @NotNull(message = "Device info cannot be null")
    // @Schema(name = "Device info", required = true, type = "object",
    // allowableValues = "A valid " +
    // "deviceInfo object")
    // private DeviceInfo deviceInfo;

    public LoginRequestDto(   String email, String password) {
//        this.username = username;
        this.email = email;
        this.password = password;
        // this.deviceInfo = deviceInfo;
    }

    public LoginRequestDto() {
    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // public DeviceInfo getDeviceInfo() {
    // return deviceInfo;
    // }
    //
    // public void setDeviceInfo(DeviceInfo deviceInfo) {
    // this.deviceInfo = deviceInfo;
    // }
}
