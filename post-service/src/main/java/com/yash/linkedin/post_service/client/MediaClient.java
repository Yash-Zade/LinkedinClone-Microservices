package com.yash.linkedin.post_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(name = "media-service", path = "/media")
public interface MediaClient {
    @PostMapping(path = "/upload")
    String uploadFile(MultipartFile file, Long postId);

    @GetMapping(path = "/upload/post/{postId}")
    String getMediaUrl(@PathVariable Long postId);
}
