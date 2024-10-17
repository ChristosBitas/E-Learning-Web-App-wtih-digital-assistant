package com.eLearningWebApp.eLearningWebApp.controller;


import com.eLearningWebApp.eLearningWebApp.dto.Response;
import com.eLearningWebApp.eLearningWebApp.entity.QuizQuestion;
import com.eLearningWebApp.eLearningWebApp.service.interfac.IQuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * QuizQuestionController manages quiz-related operations such as fetching, adding, updating, and deleting questions.
 */
@RestController
@RequestMapping("/api/quiz-questions")  // Base URL for all quiz-related endpoints
public class QuizQuestionController {

    @Autowired
    private IQuizQuestionService quizQuestionService;  // Injects the QuizQuestionService

    /**
     * Fetches a list of all quiz questions.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @GetMapping("/getAllQuizzes")
    public ResponseEntity<Response> getAllQuestions() {
        // Calls the service layer to fetch all quiz questions and wraps the result in a ResponseEntity
        Response response = quizQuestionService.getAllQuestions();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Fetches details of a specific quiz question by its ID.
     * @param id The ID of the quiz question to be fetched.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @GetMapping("/getQuizById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Only ADMINs are allowed to get quiz by id
    public ResponseEntity<Response> getQuestionById(@PathVariable Long id) {
        // Calls the service layer to fetch the question by ID
        Response response = quizQuestionService.getQuestionById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Adds a new quiz question.
     * This endpoint is restricted to users with 'ADMIN' authority.
     * @param quizQuestion The quiz question to be added.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @PostMapping("/addQuiz")
    @PreAuthorize("hasAuthority('ADMIN')")  // Only ADMINs are allowed to add quiz
    public ResponseEntity<Response> addQuestion(@RequestBody QuizQuestion quizQuestion) {
        // Calls the service layer to add a new question
        Response response = quizQuestionService.addQuestion(quizQuestion);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Updates an existing quiz question by its ID.
     * This endpoint is restricted to users with 'ADMIN' authority.
     * @param id The ID of the quiz question to be updated.
     * @param quizQuestion The updated quiz question details.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @PutMapping("/updateQuiz/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Only ADMINs are allowed to update questions
    public ResponseEntity<Response> updateQuestion(@PathVariable Long id, @RequestBody QuizQuestion quizQuestion) {
        // Calls the service layer to update the question
        Response response = quizQuestionService.updateQuestion(id, quizQuestion);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Deletes a quiz question by its ID.
     * This endpoint is restricted to users with 'ADMIN' authority.
     * @param id The ID of the quiz question to be deleted.
     * @return ResponseEntity containing a custom Response object and appropriate HTTP status.
     */
    @DeleteMapping("/deleteQuiz/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")  // Only ADMINs are allowed to delete questions
    public ResponseEntity<Response> deleteQuestion(@PathVariable Long id) {
        // Calls the service layer to delete the question
        Response response = quizQuestionService.deleteQuestion(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
