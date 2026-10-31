package com.stayfinder.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stayfinder.backend.Property;


public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByOwnerId(Long ownerId);

    boolean existsByImageAndIdNot(String image, Long id);
}