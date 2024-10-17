package com.eLearningWebApp.eLearningWebApp.repository;

import com.eLearningWebApp.eLearningWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository interface for performing database operations on the User entity.
 * It extends JpaRepository, which provides generic CRUD operations and more.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a user with the specified email exists in the database.
     * @param email The email to check for existence.
     * @return true if a user with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their email.
     * @param email The email of the user to find.
     * @return An Optional containing the User if found, or empty if not found.
     */
    Optional<User> findByEmail(String email);
}
