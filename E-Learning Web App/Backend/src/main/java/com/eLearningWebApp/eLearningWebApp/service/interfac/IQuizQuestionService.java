package com.eLearningWebApp.eLearningWebApp.service.interfac;

import com.eLearningWebApp.eLearningWebApp.dto.Response;
import com.eLearningWebApp.eLearningWebApp.entity.QuizQuestion;

/**
 * IQuizQuestionService defines the contract for quiz question-related operations
 * such as retrieving, adding, updating, and deleting quiz questions.
 */
public interface IQuizQuestionService {

    /**
     * Retrieves all quiz questions from the database.
     * @return A Response object containing a list of all quiz questions.
     */
    Response getAllQuestions();

    /**
     * Retrieves a quiz question by its ID.
     * @param questionId The ID of the quiz question to retrieve.
     * @return A Response object containing the quiz question details.
     */
    Response getQuestionById(Long questionId);

    /**
     * Adds a new quiz question to the database.
     * @param quizQuestion The QuizQuestion entity containing the question details.
     * @return A Response object with the result of the add operation.
     */
    Response addQuestion(QuizQuestion quizQuestion);

    /**
     * Updates an existing quiz question in the database.
     * @param questionId The ID of the quiz question to update.
     * @param updatedQuizQuestion Contains the updated quiz question information.
     * @return A Response object containing the result of the update operation.
     */
    Response updateQuestion(Long questionId, QuizQuestion updatedQuizQuestion);

    /**
     * Deletes a quiz question from the database by its ID.
     * @param questionId The ID of the quiz question to delete.
     * @return A Response object containing the result of the delete operation.
     */
    Response deleteQuestion(Long questionId);
}