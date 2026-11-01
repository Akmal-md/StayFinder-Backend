package com.stayfinder.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PropertyAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String amenity;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    public PropertyAmenity() {
    }

    public PropertyAmenity(String amenity, Property property) {
        this.amenity = amenity;
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
