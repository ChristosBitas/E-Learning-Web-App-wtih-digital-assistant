package com.eLearningWebApp.eLearningWebApp.controller;

import com.eLearningWebApp.eLearningWebApp.dto.LoginRequest;
import com.eLearningWebApp.eLearningWebApp.dto.Response;
import com.eLearningWebApp.eLearningWebApp.entity.User;
import com.eLearningWebApp.eLearningWebApp.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth controller for managing authentication-related operations like user registration and login.
 */

@RestController
@RequestMapping("/auth")  // Base URL for all endpoints in this controller
public class Auth {

    @Autowired
    private IUserService userService;  // Injects the IUserService implementation to handle user services

    /**
     * Registers a new user.
     * @param user The user object containing registration details.
     * @return ResponseEntity containing the custom response object and HTTP status.
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user){
        // Calls the service layer to register the user and retrieves a response
        Response response = userService.userRegister(user);
        // Returns the response wrapped in a ResponseEntity with the appropriate status code
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Authenticates a user based on login credentials.
     * @param loginRequest The login request containing username and password.
     * @return ResponseEntity containing the custom response object and HTTP status.
     */
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        // Calling the service layer to authenticate the user and retrieves a response
        Response response = userService.userLogin(loginRequest);
        // Returning the response wrapped in a ResponseEntity with the appropriate status code
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
