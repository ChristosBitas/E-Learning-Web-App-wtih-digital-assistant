package com.eLearningWebApp.eLearningWebApp.service;

import com.eLearningWebApp.eLearningWebApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService is a custom implementation of the UserDetailsService interface.
 * It is used by Spring Security to load user-specific data during authentication.
 */
@Service  // Marks this class as a Spring service component, making it available for dependency injection.
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;  // Injects the UserRepository to interact with the database.

    /**
     * Loads the user from the database by their email (used as the username).
     * If the user is not found, it throws a UsernameNotFoundException.
     *
     * @param username The username (email) of the user to load.
     * @return UserDetails containing user-specific data for authentication.
     * @throws UsernameNotFoundException If no user with the given username is found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Finds the user by their email, or throws an exception if not found.
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username doesn't exist"));
    }
}