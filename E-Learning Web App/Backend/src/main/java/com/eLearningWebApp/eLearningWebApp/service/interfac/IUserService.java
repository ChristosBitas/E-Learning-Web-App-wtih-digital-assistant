package com.eLearningWebApp.eLearningWebApp.service.interfac;

import com.eLearningWebApp.eLearningWebApp.dto.LoginRequest;
import com.eLearningWebApp.eLearningWebApp.dto.Response;
import com.eLearningWebApp.eLearningWebApp.entity.User;

/**
 * IUserService defines the contract for user-related operations such as registration, login,
 * retrieving users, and deleting users. This interface provides the signatures for all
 * necessary user service methods to be implemented.
 */
public interface IUserService {

    /**
     * Registers a new user and returns a Response with the result of the registration.
     * @param user The user entity containing the user's registration details.
     * @return A Response object with registration status and user details.
     */
    Response userRegister(User user);

    /**
     * Authenticates a user based on login credentials and generates a JWT token.
     * @param loginRequest A DTO containing the user's login details (email and password).
     * @return A Response object containing the JWT token, role, and login status.
     */
    Response userLogin(LoginRequest loginRequest);

    /**
     * Retrieves all users from the database.
     * @return A Response object containing a list of all users.
     */
    Response getAllUsers();

    /**
     * Deletes a user from the database by their user ID.
     * @param userId The ID of the user to be deleted.
     * @return A Response object containing the result of the delete operation.
     */
    Response deleteUser(String userId);

    /**
     * Updates the profile information of the logged-in user.
     * @param loggedInUserEmail The email of the logged-in user.
     * @param updatedUser Contains the updated user information (name, email, password).
     * @return A Response object containing the result of the update operation.
     */
    Response updateUserProfile(String loggedInUserEmail, User updatedUser);

    /**
     * Retrieves a user by their user ID.
     * @param userId The ID of the user to retrieve.
     * @return A Response object containing the user's details.
     */


    Response getUserById(String userId);

    /**
     * Retrieves a user by their email address.
     * @param email The email of the user to retrieve.
     * @return A Response object containing the user's details.
     */
    Response getUserByEmail(String email);

    /**
     * Updates the score of the user based on their email.
     * @param email The email of the user whose score needs to be updated.
     * @param score The new score value to be updated for the user.
     * @return Response containing the updated user details with the new score or error information.
     */
    Response updateUserScore(String email, int score);  // Method to update user score

}