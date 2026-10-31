package com.stayfinder.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stayfinder.backend.User;
import com.stayfinder.backend.dto.PropertyRequest;
import com.stayfinder.backend.dto.PropertyResponse;
import com.stayfinder.backend.service.PropertyService;
 
@RestController
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping("/properties")
    public PropertyResponse createProperty(@RequestBody PropertyRequest request) {

          Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

          User owner = (User) authentication.getPrincipal();

          return propertyService.createProperty(request, owner);
     }

     @GetMapping("/properties")
     public List<PropertyResponse> getAllProperties() {
         return propertyService.getAllProperties();
     }

     @GetMapping("/my-properties")
     public List<PropertyResponse> getMyProperties() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        User owner = (User) authentication.getPrincipal();

        return propertyService.getPropertiesByOwner(owner.getId());
    }

     @GetMapping("/properties/{id}")
     public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {

         PropertyResponse property = propertyService.getPropertyById(id);

        if (property == null) {
            return ResponseEntity.notFound().build();
        }

          return ResponseEntity.ok(property);
      }

      @PutMapping("/properties/{id}")
      public PropertyResponse updateProperty(
        @PathVariable Long id,
        @RequestBody PropertyRequest request) {

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        User owner = (User) authentication.getPrincipal();

        return propertyService.updateProperty(id, request, owner);
     }

     @DeleteMapping("/properties/{id}")
     public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        User owner = (User) authentication.getPrincipal();

        propertyService.deleteProperty(id, owner);

        return ResponseEntity.noContent().build();
    }
}
