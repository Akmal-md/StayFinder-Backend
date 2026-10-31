package com.stayfinder.backend.dto;

import java.time.LocalDateTime;

public class InquiryResponse {

    private Long id;
    private String name;
    private String email;
    private String message;
    private LocalDateTime createdAt;
    private Long propertyId;
    private String propertyTitle;
    private String status;

    public InquiryResponse(
            Long id,
            String name,
            String email,
            String message,
            LocalDateTime createdAt,
            Long propertyId,
            String propertyTitle,
            String status) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
        this.createdAt = createdAt;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public String getStatus() {
        return status;
    }
}
