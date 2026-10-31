package com.stayfinder.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stayfinder.backend.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByProperty_Owner_Id(Long ownerId);
    List<Inquiry> findByTenant_Id(Long tenantId);
    boolean existsByProperty_Id(Long propertyId);
}