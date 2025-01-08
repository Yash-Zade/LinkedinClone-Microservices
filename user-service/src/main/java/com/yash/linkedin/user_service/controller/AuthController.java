package com.yash.linkedin.user_service.controller;

import com.yash.linkedin.user_service.dto.LoginRequestDTO;
import com.yash.linkedin.user_service.dto.SignUpDTO;
import com.yash.linkedin.user_service.dto.UserDTO;
import com.yash.linkedin.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signupDTO) {
        UserDTO user = authService.signUp(signupDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String token = authService.login(loginRequestDTO);
        return ResponseEntity.ok(token);
    }
}
