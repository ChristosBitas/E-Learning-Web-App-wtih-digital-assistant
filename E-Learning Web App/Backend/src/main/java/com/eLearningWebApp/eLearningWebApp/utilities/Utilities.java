package com.eLearningWebApp.eLearningWebApp.utilities;


import com.eLearningWebApp.eLearningWebApp.dto.QuizQuestionDTO;
import com.eLearningWebApp.eLearningWebApp.dto.UserDTO;
import com.eLearningWebApp.eLearningWebApp.entity.QuizQuestion;
import com.eLearningWebApp.eLearningWebApp.entity.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class that provides helper methods for various operations such as generating confirmation codes
 * and mapping entities (User and QuizQuestion) to their corresponding DTO objects.
 */
public class Utilities {

    // ------------------------------------
    // User Entity to DTO Mapping Methods
    // ------------------------------------

    /**
     * Maps a User entity to a UserDTO object, transferring relevant user details.
     * @param user The User entity to be mapped to a UserDTO.
     * @return The UserDTO containing user information.
     */
    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());  // Sets the ID from the User entity.
        userDTO.setName(user.getName());  // Sets the name from the User entity.
        userDTO.setEmail(user.getEmail());  // Sets the email from the User entity.
        userDTO.setRole(user.getRole());  // Sets the role from the User entity.
        userDTO.setUserScore(user.getUserScore());  // Set the score
        return userDTO;  // Returns the mapped UserDTO.
    }

    /**
     * Maps a list of User entities to a list of UserDTO objects.
     * @param userList The list of User entities to be mapped.
     * @return A list of UserDTO objects containing user details.
     */
    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream()
                .map(Utilities::mapUserEntityToUserDTO)  // Maps each User entity to a UserDTO.
                .collect(Collectors.toList());  // Collects the results into a list.
    }

    // ------------------------------------
    // QuizQuestion Entity to DTO Mapping Methods
    // ------------------------------------

    /**
     * Maps a QuizQuestion entity to a QuizQuestionDTO object, transferring relevant quiz question details.
     * @param quizQuestion The QuizQuestion entity to be mapped to a QuizQuestionDTO.
     * @return The QuizQuestionDTO containing quiz question information.
     */
    public static QuizQuestionDTO mapQuizQuestionEntityToDTO(QuizQuestion quizQuestion) {
        QuizQuestionDTO dto = new QuizQuestionDTO();
        dto.setId(quizQuestion.getId());  // Sets the ID from the QuizQuestion entity.
        dto.setQuestionText(quizQuestion.getQuestionText());  // Sets the question text.
        dto.setAnswerOption1(quizQuestion.getAnswerOption1());  // Sets answer option 1.
        dto.setAnswerOption2(quizQuestion.getAnswerOption2());  // Sets answer option 2.
        dto.setAnswerOption3(quizQuestion.getAnswerOption3());  // Sets answer option 3.
        dto.setAnswerOption4(quizQuestion.getAnswerOption4());  // Sets answer option 4.
        dto.setCorrectAnswer(quizQuestion.getCorrectAnswer());  // Sets the correct answer.

        return dto;  // Returns the mapped QuizQuestionDTO.
    }

    /**
     * Maps a list of QuizQuestion entities to a list of QuizQuestionDTO objects.
     * @param quizQuestions The list of QuizQuestion entities to be mapped.
     * @return A list of QuizQuestionDTO objects containing quiz question details.
     */
    public static List<QuizQuestionDTO> mapQuizQuestionListEntityToDTO(List<QuizQuestion> quizQuestions) {
        return quizQuestions.stream()
                .map(Utilities::mapQuizQuestionEntityToDTO)  // Maps each QuizQuestion entity to a QuizQuestionDTO.
                .collect(Collectors.toList());  // Collects the results into a list.
    }
}

