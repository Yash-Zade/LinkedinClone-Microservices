package com.yash.linkedin.media_service.service;

import com.uploadcare.api.Client;
import com.uploadcare.api.File;
import com.uploadcare.upload.FileUploader;
import com.yash.linkedin.media_service.MediaServiceApplication;
import com.yash.linkedin.media_service.auth.UserContextHolder;
import com.yash.linkedin.media_service.entity.Media;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UploadcareService {

    private final Client client;
    private final MediaService mediaService;

    public UploadcareService(@Value("${uploadcare.public-key}") String publicKey,
                             @Value("${uploadcare.secret-key}") String secretKey, MediaService mediaService) {
        this.client = new Client(publicKey, secretKey);
        this.mediaService = mediaService;
    }




    public String uploadFile(MultipartFile multipartFile, Long postId) {
        try {
            java.io.File file = convertMultipartFileToFile(multipartFile);
            File uploadedFile = new FileUploader(client, file).upload().save();
            String url = uploadedFile.getOriginalFileUrl().toString();
            Media media = Media.builder()
                    .postId(postId)
                    .userId(UserContextHolder.getCurrentUserId())
                    .url(url)
                    .build();
            mediaService.saveMedia(media);
            return url;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private java.io.File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        java.io.File file = java.io.File.createTempFile("temp", multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

}
