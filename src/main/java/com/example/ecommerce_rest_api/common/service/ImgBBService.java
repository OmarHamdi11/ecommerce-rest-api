package com.example.ecommerce_rest_api.common.service;

import com.example.ecommerce_rest_api.common.response.ImgBBUploadResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;  // ✅ الـ import الصحيح
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImgBBService {

    @Value("${imgbb.api.key}")
    private String apiKey;

    @Value("${imgbb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ImgBBService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }


    public ImgBBUploadResponse uploadImage(MultipartFile file) {

        try {
            // validate file type
            if (!isValidImageType(file.getContentType())) {
                throw new RuntimeException("File must be image");
            }

            // validate file size
            if (file.getSize() > 32 * 1024 * 1024) {
                throw new RuntimeException("Image very big(Max size: 32)");
            }

            // convert image to Base64
            String base64Image = Base64.getEncoder()
                    .encodeToString(file.getBytes());

            // prepare request body
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("key", apiKey);
            body.add("image", base64Image);
            body.add("name", generateFileName(file.getOriginalFilename()));

            // prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // create the request
            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(body, headers);

            // send the request
            ResponseEntity<String> response = restTemplate.postForEntity(
                    apiUrl,
                    request,
                    String.class
            );

            // convert response to object
            ImgBBUploadResponse uploadResponse = objectMapper.readValue(
                    response.getBody(),
                    ImgBBUploadResponse.class
            );

            if (!uploadResponse.isSuccess()) {
                throw new RuntimeException("Failed to upload image on ImgBB");
            }

            return uploadResponse;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }


    public ImgBBUploadResponse uploadImageWithExpiration(
            MultipartFile file,
            int expirationSeconds
    ) {

        try {
            String base64Image = Base64.getEncoder()
                    .encodeToString(file.getBytes());

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("key", apiKey);
            body.add("image", base64Image);
            body.add("name", generateFileName(file.getOriginalFilename()));
            body.add("expiration", String.valueOf(expirationSeconds));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    apiUrl, request, String.class);

            return objectMapper.readValue(
                    response.getBody(),
                    ImgBBUploadResponse.class
            );

        } catch (IOException e) {
            throw new RuntimeException("Error on image uploading: " + e.getMessage());
        }
    }


    private boolean isValidImageType(String contentType) {
        if (contentType == null) return false;

        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/bmp") ||
                contentType.equals("image/webp");
    }


    private String generateFileName(String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );
        }
        return UUID.randomUUID().toString() + extension;
    }
}