package com.eLearningWebApp.eLearningWebApp.controller;

import com.eLearningWebApp.eLearningWebApp.dto.Response;
import com.eLearningWebApp.eLearningWebApp.entity.User;
import com.eLearningWebApp.eLearningWebApp.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UserController manages user-related operations such as fetching, deleting, and retrieving user profiles.
 */
@RestController
@RequestMapping("/users")  // Base URL for all user-related endpoints
public class UserController {

    @Autowired
    private IUserService userService;  // Injects the IUserService interface to handle user operations

    /**
     * Fetches a list of all users.
     * This endpoint is restricted to users with 'ADMIN' authority.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ADMIN')")  // Only ADMINs are allowed to access this method
    public ResponseEntity<Response> getAllUsers() {
        // Calls the service layer to fetch all users and wraps the result in a ResponseEntity
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Fetches details of a specific user by their user ID.
     * This endpoint is available to any authenticated user.
     * @param userId The ID of the user to be fetched.
     * @return ResponseEntity containing a custom Response object with user details and appropriate HTTP status.
     */
    @GetMapping("/get-user-by-id/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId) {
        // Calls the service layer to get the user by ID
        Response response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Deletes a specific user by their user ID.
     * This endpoint is restricted to users with 'ADMIN' authority.
     * @param userId The ID of the user to be deleted.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @DeleteMapping("/delete-user-by-id/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Only ADMINs are allowed to delete users
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId) {
        // Calls the service layer to delete the user by ID
        Response response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Updates the logged-in user's profile information.
     * This endpoint is available to any authenticated user.
     * @param updatedUser The updated user information (name, email, password).
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @PutMapping("/update-profile")
    @PreAuthorize("isAuthenticated()")  // Ensure that the user is logged in
    public ResponseEntity<Response> updateUserProfile(@RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();  // Get the email of the logged-in user

        // Ensure the user is updating their own profile
        Response response = userService.updateUserProfile(loggedInUserEmail, updatedUser);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Retrieves the user's data of the currently logged-in user.
     * This endpoint is available to any authenticated user.
     * @return ResponseEntity containing a custom Response object with the logged-in user's data and appropriate HTTP status.
     */
    @GetMapping("/get-logged-in-user-data")
    public ResponseEntity<Response> getLoggedInUSerProfile() {
        // Retrieves the currently authenticated user's details from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // Gets the email of the logged-in user
        // Calls the service layer to fetch user details by their email
        Response response = userService.getUserByEmail(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Allows the logged-in user to update their score.
     * @param scoreData A map containing the updated score.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @PutMapping("/save-score")
    @PreAuthorize("isAuthenticated()")  // Ensure that the user is logged in
    public ResponseEntity<Response> updateUserScore(@RequestBody Map<String, Integer> scoreData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // Get logged-in user's email
        int score = scoreData.get("user_score");  // Retrieve score from the request body
        Response response = userService.updateUserScore(email, score);  // Call the service method
        return ResponseEntity.status(response.getStatusCode()).body(response);  // Return the response
    }
}
