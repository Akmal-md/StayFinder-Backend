
package com.stayfinder.backend.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@RestController
public class FileUploadController {

    private final Cloudinary cloudinary;

    public FileUploadController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("image") MultipartFile image) {

        try {
            if (image.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please select an image");
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

            Map uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );

            String imageUrl = (String) uploadResult.get("secure_url");

            return ResponseEntity.ok(imageUrl);

        } catch (IOException error) {
            return ResponseEntity.internalServerError()
                    .body("Image upload failed");
        }
    }
}
