package org.example.authservice.model;

import java.util.Objects;

public class DeviceInfo {
    private String ipAddress; // IP address of the device
    private String userAgent; // Browser user-agent string

    // Constructor
    public DeviceInfo(String ipAddress, String userAgent) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    // Generate a unique device identifier based on IP and user-agent
    public String getDeviceIdentifier() {
        return ipAddress + "-" + userAgent.hashCode(); // Combine IP and hashed user-agent
    }

    // Getters and Setters
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    // Equals and HashCode (for comparing devices)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceInfo device = (DeviceInfo) o;
        return Objects.equals(ipAddress, device.ipAddress) &&
                Objects.equals(userAgent, device.userAgent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, userAgent);
    }

    // toString (for logging and debugging)
    @Override
    public String toString() {
        return "Device{" +
                "ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}