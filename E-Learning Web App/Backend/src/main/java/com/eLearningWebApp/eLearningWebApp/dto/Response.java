package com.eLearningWebApp.eLearningWebApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Response DTO (Data Transfer Object) is used to structure responses sent back to the client.
 * It includes fields for status code, message, token, role, and dynamic data for users or quiz questions.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;   // HTTP status code of the response (e.g., 200, 400)
    private String message;   // A message that describes the outcome of the operation (e.g., success, error)

    // Auth-related fields
    private String token;
    private String role;
    private String expirationTime;

    // Data for User and Quiz-related operations (only one will be populated per response)
    private UserDTO user;
    private List<UserDTO> userList;
    private QuizQuestionDTO quizQuestion;
    private List<QuizQuestionDTO> quizQuestionList;
}
