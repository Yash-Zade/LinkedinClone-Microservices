package com.yash.linkedin.user_service.service;


import com.yash.linkedin.user_service.config.PasswordUtil;
import com.yash.linkedin.user_service.dto.LoginRequestDTO;
import com.yash.linkedin.user_service.dto.SignUpDTO;
import com.yash.linkedin.user_service.dto.UserDTO;
import com.yash.linkedin.user_service.entity.User;
import com.yash.linkedin.user_service.exception.BadRequestException;
import com.yash.linkedin.user_service.exception.ResourceNotFoundException;
import com.yash.linkedin.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDTO signUp(SignUpDTO signupRequestDto) {
        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists) {
            throw new BadRequestException("User already exists, cannot signup again.");
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public String login(LoginRequestDTO loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) {
            throw new BadRequestException("Incorrect password");
        }

        return jwtService.generateAccessToken(user);
    }
}
