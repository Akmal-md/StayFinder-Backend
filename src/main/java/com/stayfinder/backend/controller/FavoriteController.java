package com.stayfinder.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stayfinder.backend.User;
import com.stayfinder.backend.dto.PropertyResponse;
import com.stayfinder.backend.service.FavoriteService;

@RestController
public class FavoriteController {


private final FavoriteService favoriteService;

public FavoriteController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
}


@GetMapping("/favorites")
public List<PropertyResponse> getFavorites() {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    return favoriteService.getFavorites(user);
}


@PostMapping("/favorites/{propertyId}")
public ResponseEntity<Void> addFavorite(
        @PathVariable Long propertyId) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    favoriteService.addFavorite(propertyId, user);

    return ResponseEntity.noContent().build();
}

@DeleteMapping("/favorites/{propertyId}")
public ResponseEntity<Void> removeFavorite(
        @PathVariable Long propertyId) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    favoriteService.removeFavorite(propertyId, user);

    return ResponseEntity.noContent().build();
}


}
