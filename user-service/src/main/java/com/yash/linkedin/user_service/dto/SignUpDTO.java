package com.yash.linkedin.user_service.dto;


import lombok.Data;

@Data
public class SignUpDTO {
    private String name;
    private String email;
    private String password;
}
