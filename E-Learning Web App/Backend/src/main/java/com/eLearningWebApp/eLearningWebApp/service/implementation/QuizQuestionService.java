package com.eLearningWebApp.eLearningWebApp.service.implementation;

import com.eLearningWebApp.eLearningWebApp.dto.QuizQuestionDTO;
import com.eLearningWebApp.eLearningWebApp.dto.Response;
import com.eLearningWebApp.eLearningWebApp.entity.QuizQuestion;
import com.eLearningWebApp.eLearningWebApp.exception.ExceptionsMessages;
import com.eLearningWebApp.eLearningWebApp.repository.QuizQuestionRepository;
import com.eLearningWebApp.eLearningWebApp.service.interfac.IQuizQuestionService;
import com.eLearningWebApp.eLearningWebApp.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing quiz questions. Handles
 * retrieving, adding, updating, and deleting quiz questions.
 */
@Service
public class QuizQuestionService implements IQuizQuestionService {

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    /**
     * Retrieves all quiz questions from the database.
     * @return Response containing a list of QuizQuestionDTO and status.
     */
    @Override
    public Response getAllQuestions() {
        Response response = new Response();
        try {
            List<QuizQuestion> quizQuestions = quizQuestionRepository.findAll(); // Fetch all quiz questions
            List<QuizQuestionDTO> quizQuestionDTOs = Utilities.mapQuizQuestionListEntityToDTO(quizQuestions); // Map entities to DTOs
            response.setQuizQuestionList(quizQuestionDTOs);
            response.setStatusCode(200);
            response.setMessage("All quiz questions retrieved successfully.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching quiz questions: " + e.getMessage());
        }
        return response;
    }

    /**
     * Retrieves a specific quiz question by its ID.
     * @param questionId ID of the quiz question.
     * @return Response containing the QuizQuestionDTO and status.
     */
    @Override
    public Response getQuestionById(Long questionId) {
        Response response = new Response();
        try {
            Optional<QuizQuestion> quizQuestion = quizQuestionRepository.findById(questionId); // Fetch quiz question by ID
            if (quizQuestion.isPresent()) {
                QuizQuestionDTO quizQuestionDTO = Utilities.mapQuizQuestionEntityToDTO(quizQuestion.get()); // Map entity to DTO
                response.setQuizQuestion(quizQuestionDTO);
                response.setStatusCode(200);
                response.setMessage("Quiz question retrieved successfully.");
            } else {
                throw new ExceptionsMessages("Quiz question not found."); // Handle if quiz question is not found
            }
        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching quiz question: " + e.getMessage());
        }
        return response;
    }

    /**
     * Adds a new quiz question to the database.
     * @param quizQuestion QuizQuestion entity to be added.
     * @return Response containing the added QuizQuestionDTO and status.
     */
    @Override
    public Response addQuestion(QuizQuestion quizQuestion) {
        Response response = new Response();
        try {
            QuizQuestion savedQuestion = quizQuestionRepository.save(quizQuestion); // Save the new quiz question
            QuizQuestionDTO quizQuestionDTO = Utilities.mapQuizQuestionEntityToDTO(savedQuestion); // Map entity to DTO
            response.setQuizQuestion(quizQuestionDTO);
            response.setStatusCode(201);
            response.setMessage("Quiz question added successfully.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding quiz question: " + e.getMessage());
        }
        return response;
    }

    /**
     * Updates an existing quiz question.
     * @param questionId ID of the quiz question to update.
     * @param updatedQuizQuestion QuizQuestion entity containing updated data.
     * @return Response containing the updated QuizQuestionDTO and status.
     */
    @Override
    public Response updateQuestion(Long questionId, QuizQuestion updatedQuizQuestion) {
        Response response = new Response();
        try {
            QuizQuestion existingQuestion = quizQuestionRepository.findById(questionId)
                    .orElseThrow(() -> new ExceptionsMessages("Quiz question not found.")); // Find quiz question or throw error

            // Update the quiz question fields
            existingQuestion.setQuestionText(updatedQuizQuestion.getQuestionText());
            existingQuestion.setAnswerOption1(updatedQuizQuestion.getAnswerOption1());
            existingQuestion.setAnswerOption2(updatedQuizQuestion.getAnswerOption2());
            existingQuestion.setAnswerOption3(updatedQuizQuestion.getAnswerOption3());
            existingQuestion.setAnswerOption4(updatedQuizQuestion.getAnswerOption4());
            existingQuestion.setCorrectAnswer(updatedQuizQuestion.getCorrectAnswer());

            QuizQuestion savedQuestion = quizQuestionRepository.save(existingQuestion); // Save updated quiz question
            QuizQuestionDTO quizQuestionDTO = Utilities.mapQuizQuestionEntityToDTO(savedQuestion); // Map entity to DTO

            response.setQuizQuestion(quizQuestionDTO);
            response.setStatusCode(200);
            response.setMessage("Quiz question updated successfully.");
        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating quiz question: " + e.getMessage());
        }
        return response;
    }

    /**
     * Deletes a quiz question from the database.
     * @param questionId ID of the quiz question to be deleted.
     * @return Response containing status and message.
     */
    @Override
    public Response deleteQuestion(Long questionId) {
        Response response = new Response();
        try {
            QuizQuestion quizQuestion = quizQuestionRepository.findById(questionId)
                    .orElseThrow(() -> new ExceptionsMessages("Quiz question not found.")); // Find quiz question or throw error
            quizQuestionRepository.deleteById(questionId); // Delete the quiz question
            response.setStatusCode(200);
            response.setMessage("Quiz question deleted successfully.");
        } catch (ExceptionsMessages e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting quiz question: " + e.getMessage());
        }
        return response;
    }
}