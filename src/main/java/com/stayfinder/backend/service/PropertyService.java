package com.stayfinder.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stayfinder.backend.Favorite;
import com.stayfinder.backend.Property;
import com.stayfinder.backend.User;
import com.stayfinder.backend.dto.PropertyRequest;
import com.stayfinder.backend.dto.PropertyResponse;
import com.stayfinder.backend.repository.InquiryRepository;
import com.stayfinder.backend.repository.FavoriteRepository;
import com.stayfinder.backend.repository.PropertyRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final InquiryRepository inquiryRepository;
    private final FavoriteRepository favoriteRepository;

    public PropertyService(PropertyRepository propertyRepository, InquiryRepository inquiryRepository, FavoriteRepository favoriteRepository) {
        this.propertyRepository = propertyRepository;
        this.inquiryRepository = inquiryRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public PropertyResponse createProperty(PropertyRequest request, User owner) {

        Property property = new Property();

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setLocation(request.getLocation());
        property.setRent(request.getRent());
        property.setPropertyType(request.getPropertyType());
        property.setImage(request.getImage());
        property.setAvailable(request.getAvailable());
        property.setAmenities(request.getAmenities());

        property.setOwner(owner);

        Property savedProperty = propertyRepository.save(property);

        return new PropertyResponse(savedProperty);
    }

    public List<PropertyResponse> getAllProperties() {

        List<Property> properties = propertyRepository.findAll();

        return properties.stream()
                .map(PropertyResponse::new)
                .toList();
    }

    public List<PropertyResponse> getPropertiesByOwner(Long ownerId) {

        List<Property> properties = propertyRepository.findByOwnerId(ownerId);

        return properties.stream()
                .map(PropertyResponse::new)
                .toList();
    }

    public PropertyResponse getPropertyById(Long id) {

        Property property = propertyRepository.findById(id)
                .orElse(null);

        if (property == null) {
            return null;
        }

        return new PropertyResponse(property);
    }

    public PropertyResponse updateProperty(Long propertyId, PropertyRequest request, User owner) {

        Property property = propertyRepository.findById(propertyId)
                .orElse(null);

        if (property == null) {
            return null;
        }

        if (!property.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You cannot edit another owner's property");
        }

        // Store the existing image filename
        String oldImage = property.getImage();

        // Update property details
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setLocation(request.getLocation());
        property.setRent(request.getRent());
        property.setPropertyType(request.getPropertyType());
        property.setImage(request.getImage());
        property.setAvailable(request.getAvailable());
        property.setAmenities(request.getAmenities());

        Property updatedProperty = propertyRepository.save(property);

        // Delete the old image if it is no longer being used
        if (oldImage != null
                && !oldImage.isBlank()
                && !oldImage.equals(request.getImage())) {

            boolean imageIsUsed = propertyRepository
                    .existsByImageAndIdNot(oldImage, propertyId);

            if (!imageIsUsed) {

                try {
                    Path imagePath = Paths.get("uploads")
                            .resolve(oldImage)
                            .normalize();

                    System.out.println("Old image path: " + imagePath.toAbsolutePath());
                    System.out.println("File exists: " + Files.exists(imagePath));

                    boolean deleted = Files.deleteIfExists(imagePath);

                    System.out.println("Old image deleted: " + deleted);

                } catch (IOException error) {
                    System.out.println(
                            "Unable to delete old image: " + error.getMessage());
                }
            }
        }

        return new PropertyResponse(updatedProperty);
    }


    @Transactional
    public void deleteProperty(Long propertyId, User owner) {

        Property property = propertyRepository.findById(propertyId)
                .orElse(null);

        if (property == null) {
            throw new RuntimeException("Property not found");
        }

        // Verify property ownership
        if (!property.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException(
                    "You cannot delete another owner's property");
        }

        // Check whether the property has existing inquiries
        boolean hasInquiries
                = inquiryRepository.existsByProperty_Id(propertyId);

       if (hasInquiries) {
    throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Cannot delete this property because it has existing inquiries."
    );
}

        // Get image filename before deleting the property
        String image = property.getImage();

        // Delete all favorites associated with this property
        List<Favorite> favorites
                = favoriteRepository.findByPropertyId(propertyId);

        favoriteRepository.deleteAll(favorites);

        // Delete the property from the database
        propertyRepository.delete(property);

        // Check whether another property uses the same image
        if (image != null && !image.isBlank()) {

            boolean imageIsUsed
                    = propertyRepository.existsByImageAndIdNot(image, propertyId);

            // Delete image only if no other property uses it
            if (!imageIsUsed) {

                try {
                    Path imagePath = Paths.get("uploads")
                            .resolve(image)
                            .normalize();

                    boolean deleted = Files.deleteIfExists(imagePath);

                    System.out.println("Deleted property image: " + deleted);

                } catch (IOException error) {
                    System.out.println(
                            "Unable to delete image: " + error.getMessage());
                }
            }
        }
    }
}
