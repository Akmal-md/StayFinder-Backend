package com.stayfinder.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stayfinder.backend.User;
import com.stayfinder.backend.dto.InquiryRequest;
import com.stayfinder.backend.dto.InquiryResponse;
import com.stayfinder.backend.dto.UpdateInquiryStatusRequest;
import com.stayfinder.backend.service.InquiryService;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping
    public ResponseEntity<String> createInquiry(
            @RequestBody InquiryRequest request,
            Authentication authentication) {

        User tenant = (User) authentication.getPrincipal();

        inquiryService.createInquiry(request, tenant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Inquiry submitted successfully");
    }

    @GetMapping("/owner")
public ResponseEntity<List<InquiryResponse>> getOwnerInquiries(
        Authentication authentication) {

    User owner = (User) authentication.getPrincipal();

    List<InquiryResponse> inquiries =
            inquiryService.getOwnerInquiries(owner.getId());

    return ResponseEntity.ok(inquiries);
}

  @GetMapping("/tenant")
public ResponseEntity<List<InquiryResponse>> getTenantInquiries(
        Authentication authentication) {

    User tenant = (User) authentication.getPrincipal();

    List<InquiryResponse> inquiries =
            inquiryService.getTenantInquiries(tenant.getId());

    return ResponseEntity.ok(inquiries);
}

    

    @PatchMapping("/{inquiryId}/status")
public ResponseEntity<String> updateInquiryStatus(
@PathVariable Long inquiryId,
@RequestBody UpdateInquiryStatusRequest request,
Authentication authentication) {


User owner = (User) authentication.getPrincipal();

inquiryService.updateInquiryStatus(inquiryId, request, owner);

return ResponseEntity.ok("Inquiry status updated successfully");


}

}