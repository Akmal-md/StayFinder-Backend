package com.stayfinder.backend;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inquiries")
public class Inquiry {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String name;

private String email;

private String message;

private LocalDateTime createdAt;

private String status = "PENDING";

@ManyToOne
@JoinColumn(name = "property_id", nullable = false)
private Property property;

@ManyToOne
@JoinColumn(name = "tenant_id", nullable = false)
private User tenant;

public Inquiry() {
}

public Long getId() {
    return id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getMessage() {
    return message;
}

public void setMessage(String message) {
    this.message = message;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public Property getProperty() {
    return property;
}

public void setProperty(Property property) {
    this.property = property;
}

public User getTenant() {
    return tenant;
}

public void setTenant(User tenant) {
    this.tenant = tenant;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

}
