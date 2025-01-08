package com.yash.linkedin.user_service.repository;


import com.yash.linkedin.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
