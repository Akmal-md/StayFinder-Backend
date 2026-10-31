package com.stayfinder.backend.dto;
import java.util.List;

public class PropertyRequest {

    private String title;
    private String description;
    private String location;
    private Double rent;
    private String propertyType;
    private String image;
    private Boolean available;
    private List<String> amenities;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<String> getAmenities() {
    return amenities;
}

public void setAmenities(List<String> amenities) {
    this.amenities = amenities;
}





}