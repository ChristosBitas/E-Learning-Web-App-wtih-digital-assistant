package com.eLearningWebApp.eLearningWebApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * UserDTO (Data Transfer Object) is used to transfer user data between different layers of the application.
 * It contains essential user information such as ID, name, password, email, and role.
 */
@Data  // Automatically generate getters, setters, equals, hashCode, and toString methods.
@JsonInclude(JsonInclude.Include.NON_NULL)  // Ensures that null fields are not included in the JSON response.
public class UserDTO {

    private Long id;        // The unique identifier for the user
    private String name;    // The name of the user
    private String password; // The user's password (in practice, this should be handled securely)
    private String email;   // The email of the user
    private String role;    // The role of the user (e.g., ADMIN, USER)
    private Integer userScore;  // The user's score

}
