package com.yash.linkedin.user_service.config;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordUtil {

    // Hash a password for the first time
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Check that a plain text password matches a previously hashed one
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
