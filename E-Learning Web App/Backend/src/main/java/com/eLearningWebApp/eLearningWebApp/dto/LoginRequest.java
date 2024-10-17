package com.eLearningWebApp.eLearningWebApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * LoginRequest DTO (Data Transfer Object) represents the structure of the login request payload.
 * It contains the user's email and password, both of which are required fields.
 */
@Data  // Automatically generate getters, setters, and other utility methods like equals, hashCode, etc.
public class LoginRequest {

    @NotBlank(message = "Email is empty")  // Ensures that the email field cannot be blank or null
    private String email;

    @NotBlank(message = "Password is empty")  // Ensures that the password field cannot be blank or null
    private String password;

}