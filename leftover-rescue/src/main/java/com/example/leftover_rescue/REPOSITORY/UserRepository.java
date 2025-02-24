package com.example.leftover_rescue.REPOSITORY;

import com.example.leftover_rescue.MODEL.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their email.
     *
     * @param email The user's email.
     * @return The User entity if found, else null.
     */
    User findByEmail(String email);
    User findByUsername(String username);
}
