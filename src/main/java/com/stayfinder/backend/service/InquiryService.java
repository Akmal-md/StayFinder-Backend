package com.stayfinder.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.stayfinder.backend.Inquiry;
import com.stayfinder.backend.Property;
import com.stayfinder.backend.User;
import com.stayfinder.backend.dto.InquiryRequest;
import com.stayfinder.backend.dto.InquiryResponse;
import com.stayfinder.backend.dto.UpdateInquiryStatusRequest;
import com.stayfinder.backend.repository.InquiryRepository;
import com.stayfinder.backend.repository.PropertyRepository;

@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final PropertyRepository propertyRepository;

    public InquiryService(
            InquiryRepository inquiryRepository,
            PropertyRepository propertyRepository) {

        this.inquiryRepository = inquiryRepository;
        this.propertyRepository = propertyRepository;
    }

    public Inquiry createInquiry(InquiryRequest request, User tenant) {

        // Find the property using the property ID
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Create a new inquiry
        Inquiry inquiry = new Inquiry();

        inquiry.setName(request.getName());
        inquiry.setEmail(request.getEmail());
        inquiry.setMessage(request.getMessage());

        // Connect the inquiry with the property
        inquiry.setProperty(property);

        // Connect the inquiry with the logged-in tenant
        inquiry.setTenant(tenant);

        // Store the submission date and time
        inquiry.setCreatedAt(LocalDateTime.now());

        // Save the inquiry in MySQL
        return inquiryRepository.save(inquiry);
    }

    public List<InquiryResponse> getOwnerInquiries(Long ownerId) {

        return inquiryRepository.findByProperty_Owner_Id(ownerId)
                .stream()
                .map(inquiry -> new InquiryResponse(
                        inquiry.getId(),
                        inquiry.getName(),
                        inquiry.getEmail(),
                        inquiry.getMessage(),
                        inquiry.getCreatedAt(),
                        inquiry.getProperty().getId(),
                        inquiry.getProperty().getTitle(),
                        inquiry.getStatus()
                ))
                .collect(Collectors.toList());
    }

    public List<InquiryResponse> getTenantInquiries(Long tenantId) {

    return inquiryRepository.findByTenant_Id(tenantId)
            .stream()
            .map(inquiry -> new InquiryResponse(
                    inquiry.getId(),
                    inquiry.getName(),
                    inquiry.getEmail(),
                    inquiry.getMessage(),
                    inquiry.getCreatedAt(),
                    inquiry.getProperty().getId(),
                    inquiry.getProperty().getTitle(),
                    inquiry.getStatus()
            ))
            .collect(Collectors.toList());
}

    public Inquiry updateInquiryStatus(
            Long inquiryId,
            UpdateInquiryStatusRequest request,
            User owner) {

// Find the inquiry
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

// Make sure this inquiry belongs to the logged-in owner
        if (!inquiry.getProperty().getOwner().getId().equals(owner.getId())) {
            throw new AccessDeniedException("You cannot update this inquiry");
        }

// Allow only PENDING or CONTACTED
        String status = request.getStatus();

        if (status == null
                || (!status.equals("PENDING") && !status.equals("CONTACTED"))) {
            throw new IllegalArgumentException(
                    "Status must be PENDING or CONTACTED");
        }

// Update and save the status
        inquiry.setStatus(status);

        return inquiryRepository.save(inquiry);

    }

}
