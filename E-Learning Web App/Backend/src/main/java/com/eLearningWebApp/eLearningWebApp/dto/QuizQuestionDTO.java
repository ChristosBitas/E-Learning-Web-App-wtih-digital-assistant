package com.eLearningWebApp.eLearningWebApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * QuizQuestionDTO represents a Data Transfer Object for quiz questions.
 * It is used to transfer quiz question data between client and server.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizQuestionDTO {

    private Long id;
    private String questionText;
    private String answerOption1;
    private String answerOption2;
    private String answerOption3;
    private String answerOption4;
    private int correctAnswer;

    private List<QuizQuestionDTO> quizQuestionList;  // List of quiz questions when multiple questions are needed
}
