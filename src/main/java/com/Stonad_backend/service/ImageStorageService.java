
package com.Stonad_backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    private final Path uploadPath =
            Paths.get("uploads").toAbsolutePath().normalize();

    public String storeImage(MultipartFile file) throws IOException {
    	
    	 validateImage(file);

        // Create uploads folder if it doesn't exist
        Files.createDirectories(uploadPath);

        // Get original filename
        String originalFilename = file.getOriginalFilename();

        // Extract file extension
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(
                    originalFilename.lastIndexOf("."));
        }

        // Generate unique filename
        String filename = UUID.randomUUID() + extension;

        // Create destination path
        Path destination = uploadPath.resolve(filename).normalize();

        // Save image
        file.transferTo(destination);

        return filename;
    }
    
    
    

private void validateImage(MultipartFile file) {

    // Check empty file
    if (file == null || file.isEmpty()) {
        throw new IllegalArgumentException("Image cannot be empty");
    }

    // Check file type
    String contentType = file.getContentType();

    if (contentType == null || !contentType.startsWith("image/")) {
        throw new IllegalArgumentException("Only image files are allowed");
    }

    // Check file size (5 MB)
    long maxSize = 5 * 1024 * 1024;

    if (file.getSize() > maxSize) {
        throw new IllegalArgumentException("Image size cannot exceed 5 MB");
    }
}

}
