package com.stayfinder.backend.dto;
import java.util.List;

import com.stayfinder.backend.Property;

public class PropertyResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private Double rent;
    private String propertyType;
    private String image;
    private Boolean available;
    private Long ownerId;
    private List<String> amenities;

    public PropertyResponse(Property property) {
        this.id = property.getId();
        this.title = property.getTitle();
        this.description = property.getDescription();
        this.location = property.getLocation();
        this.rent = property.getRent();
        this.propertyType = property.getPropertyType();
        this.image = property.getImage();
        this.available = property.getAvailable();
        this.ownerId = property.getOwner().getId();
        this.amenities = property.getAmenities();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public Double getRent() {
        return rent;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getImage() {
        return image;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public List<String> getAmenities() {
    return amenities;
}
}