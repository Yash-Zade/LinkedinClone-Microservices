package com.yash.linkedin.media_service.controller;

import com.yash.linkedin.media_service.service.MediaService;
import com.yash.linkedin.media_service.service.UploadcareService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/upload")
public class MediaController {

    private final UploadcareService uploadcareService;
    private final MediaService mediaService;

    @PostMapping
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile,Long postId){
        return uploadcareService.uploadFile(multipartFile,postId);
    }

    @GetMapping("/post/{postId}")
    public String getMediaUrl(@PathVariable Long postId){
        return mediaService.getMediaByPostId(postId);
    }

}
