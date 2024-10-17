package com.eLearningWebApp.eLearningWebApp.service.implementation;

import com.eLearningWebApp.eLearningWebApp.dto.LoginRequest;
import com.eLearningWebApp.eLearningWebApp.dto.Response;
import com.eLearningWebApp.eLearningWebApp.dto.UserDTO;
import com.eLearningWebApp.eLearningWebApp.entity.User;
import com.eLearningWebApp.eLearningWebApp.exception.ExceptionsMessages;
import com.eLearningWebApp.eLearningWebApp.repository.UserRepository;
import com.eLearningWebApp.eLearningWebApp.service.interfac.IUserService;
import com.eLearningWebApp.eLearningWebApp.utilities.JWTUtilities;
import com.eLearningWebApp.eLearningWebApp.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing user-related operations such as registration, login, profile management,
 * score updates, and retrieving user information.
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;  // Repository for performing database operations on User entities.

    @Autowired
    private PasswordEncoder passwordEncoder;  // Password encoder to securely hash passwords.

    @Autowired
    private JWTUtilities jwtUtils;  // Utility for handling JWT token generation and validation.

    @Autowired
    private AuthenticationManager authenticationManager;  // Handles user authentication using Spring Security.

    /**
     * Registers a new user, hashes their password, and saves their details in the database.
     * If no role is provided, sets the default role to "USER".
     * @param user The user object containing the registration details.
     * @return Response with the registered user details or error information.
     */
    @Override
    public Response userRegister(User user) {
        Response response = new Response();

        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");  // Set default role to USER if none is provided
            }

            // Check if the email is already taken
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new ExceptionsMessages(user.getEmail() + " " + "User exists");
            }

            // Hash the password before saving the user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);  // Save the user to the database
            UserDTO userDTO = Utilities.mapUserEntityToUserDTO(savedUser);  // Convert User entity to DTO

            response.setStatusCode(200);
            response.setUser(userDTO);  // Set the registered user data in the response
            response.setMessage("User's successful register");

        } catch (ExceptionsMessages e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());  // Handle custom exception for user existence

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Saving a User: " + e.getMessage());  // Handle general exceptions
        }
        return response;
    }

    /**
     * Logs in a user by validating their credentials and generating a JWT token.
     * @param loginRequest Contains the user's login credentials (email and password).
     * @return Response containing the JWT token, role, and status.
     */
    @Override
    public Response userLogin(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            // Authenticate user using provided credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // Retrieve user from database
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ExceptionsMessages("Cant find user"));
            var token = jwtUtils.generateToken(user);  // Generate JWT token

            response.setToken(token);
            response.setExpirationTime("7 days");
            response.setRole(user.getRole());
            response.setMessage("User's successful login");
            response.setStatusCode(200);

        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());  // Handle exceptions for missing users

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Logging in: " + e.getMessage());  // Handle other exceptions
        }
        return response;
    }

    /**
     * Retrieves all users from the database.
     * @return Response containing a list of users and their details.
     */
    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> userList = userRepository.findAll();  // Fetch all users
            List<UserDTO> userDTOList = Utilities.mapUserListEntityToUserListDTO(userList);  // Convert entities to DTOs

            response.setUserList(userDTOList);  // Set the user list in the response
            response.setMessage("successful");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users: " + e.getMessage());  // Handle exceptions
        }
        return response;
    }

    /**
     * Deletes a user by their ID.
     * @param userId The ID of the user to delete.
     * @return Response containing the status of the deletion.
     */
    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try {
            // Check if user exists before deletion
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new ExceptionsMessages("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));  // Delete the user

            response.setMessage("User has been deleted successfully");
            response.setStatusCode(200);

        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());  // Handle exceptions for user not found

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting a user: " + e.getMessage());  // Handle other exceptions
        }
        return response;
    }

    /**
     * Updates the profile information of the logged-in user.
     * @param loggedInUserEmail The email of the logged-in user.
     * @param updatedUser Contains the updated user information (name, email, password).
     * @return Response with the updated user details or error information.
     */
    @Override
    public Response updateUserProfile(String loggedInUserEmail, User updatedUser) {
        Response response = new Response();

        try {
            // Retrieve the logged-in user by email
            User user = userRepository.findByEmail(loggedInUserEmail)
                    .orElseThrow(() -> new ExceptionsMessages("User not found"));

            // Update the user's name and email if changed
            if (updatedUser.getName() != null && !updatedUser.getName().isBlank()) {
                user.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
                if (!user.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
                    throw new ExceptionsMessages("Email already exists");
                }
                user.setEmail(updatedUser.getEmail());
            }

            // Update password only if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userRepository.save(user);  // Save updated user

            UserDTO userDTO = Utilities.mapUserEntityToUserDTO(user);  // Convert entity to DTO

            response.setMessage("Profile updated successfully");
            response.setStatusCode(200);
            response.setUser(userDTO);

        } catch (ExceptionsMessages e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());  // Handle validation exceptions

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating profile: " + e.getMessage());  // Handle other exceptions
        }

        return response;
    }

    /**
     * Retrieves a user by their ID.
     * @param userId The ID of the user to retrieve.
     * @return Response with the user's details or error information.
     */
    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new ExceptionsMessages("User doesn't exist"));
            UserDTO userDTO = Utilities.mapUserEntityToUserDTO(user);  // Convert entity to DTO

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setUser(userDTO);  // Return user details in the response

        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());  // Handle user not found

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting a user by id: " + e.getMessage());  // Handle other exceptions
        }
        return response;
    }

    /**
     * Retrieves a user by their email.
     * @param email The email of the user to retrieve.
     * @return Response with the user's details or error information.
     */
    @Override
    public Response getUserByEmail(String email) {
        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ExceptionsMessages("User doesn't exist"));
            UserDTO userDTO = Utilities.mapUserEntityToUserDTO(user);  // Convert entity to DTO

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setUser(userDTO);  // Return user details in the response

        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());  // Handle user not found

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting user info: " + e.getMessage());  // Handle other exceptions
        }
        return response;
    }

    /**
     * Updates the score of the user by their email.
     * @param email The email of the user.
     * @param score The new score to update.
     * @return Response with the updated score or error information.
     */
    @Override
    public Response updateUserScore(String email, int score) {
        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ExceptionsMessages("User not found"));

            user.setUserScore(score);  // Update user score
            userRepository.save(user);  // Save updated user

            UserDTO userDTO = Utilities.mapUserEntityToUserDTO(user);  // Convert updated entity to DTO

            response.setStatusCode(200);
            response.setUser(userDTO);
            response.setMessage("Score updated successfully");

        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());  // Handle user not found

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating score: " + e.getMessage());  // Handle other exceptions
        }
        return response;
    }
}
