package com.stayfinder.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stayfinder.backend.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByUserIdAndPropertyId(
        Long userId,
        Long propertyId
    );

    List<Favorite> findByPropertyId(Long propertyId);
}