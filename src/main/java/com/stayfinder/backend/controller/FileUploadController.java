package com.stayfinder.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {


private final String uploadDirectory = "uploads/";

@PostMapping("/upload")
public ResponseEntity<String> uploadImage(
        @RequestParam("image") MultipartFile image) {

    try {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select an image");
        }

        if (image.getSize() > 5 * 1024 * 1024) {
    return ResponseEntity.badRequest()
            .body("Image size must not exceed 5 MB");
}

        String contentType = image.getContentType();

if (contentType == null ||
    !(contentType.equals("image/jpeg") ||
      contentType.equals("image/png") ||
      contentType.equals("image/webp"))) {

    return ResponseEntity.badRequest()
            .body("Only JPG, PNG and WEBP images are allowed");
}

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDirectory);

        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(image.getInputStream(), filePath);

        return ResponseEntity.ok(fileName);

    } catch (IOException error) {
        return ResponseEntity.internalServerError()
                .body("Image upload failed");
    }
}

@GetMapping("/uploads/{fileName:.+}")
public ResponseEntity<Resource> getImage(
        @PathVariable String fileName) {

    try {
        Path filePath = Paths.get(uploadDirectory)
                .resolve(fileName)
                .normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(filePath);

        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);

    } catch (IOException error) {
        return ResponseEntity.internalServerError().build();
    }
}


}
