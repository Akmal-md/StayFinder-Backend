package com.stayfinder.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stayfinder.backend.Favorite;
import com.stayfinder.backend.Property;
import com.stayfinder.backend.User;
import com.stayfinder.backend.dto.PropertyResponse;
import com.stayfinder.backend.repository.FavoriteRepository;
import com.stayfinder.backend.repository.PropertyRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PropertyRepository propertyRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            PropertyRepository propertyRepository) {
        this.favoriteRepository = favoriteRepository;
        this.propertyRepository = propertyRepository;
    }

    public List<PropertyResponse> getFavorites(User user) {
return favoriteRepository.findByUserId(user.getId())
.stream()
.map(Favorite::getProperty)
.map(PropertyResponse::new)
.toList();
}


    @Transactional
    public void addFavorite(Long propertyId, User user) {

        boolean alreadyFavorite = favoriteRepository
                .findByUserIdAndPropertyId(user.getId(), propertyId)
                .isPresent();

        if (alreadyFavorite) {
            return;
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProperty(property);

        favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long propertyId, User user) {

        Favorite favorite = favoriteRepository
                .findByUserIdAndPropertyId(user.getId(), propertyId)
                .orElseThrow(() ->
                        new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }
}