package com.cyfrifpro.DTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MembershipDTO {

    private Long membershipId;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotBlank(message = "Membership type is required")
    private String membershipType;

    @Min(value = 0, message = "Fee must be positive")
    private double fee;

    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private String status; // e.g., ACTIVE, EXPIRED

    // Getters and setters...
    public Long getMembershipId() {
        return membershipId;
    }
    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }
    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public String getMembershipType() {
        return membershipType;
    }
    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
