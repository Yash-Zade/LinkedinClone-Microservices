package com.yash.linkedin.post_service.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostCreationDTO {
    private String content;
    private MultipartFile file;
}
