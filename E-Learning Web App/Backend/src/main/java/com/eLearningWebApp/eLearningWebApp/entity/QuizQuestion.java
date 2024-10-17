package com.eLearningWebApp.eLearningWebApp.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text")
    @NotBlank(message = "Question text is required")

    private String questionText;

    @Column(name = "answer_option_1")
    @NotBlank(message = "Answer option 1 is required")

    private String answerOption1;

    @Column(name = "answer_option_2")
    @NotBlank(message = "Answer option 2 is required")

    private String answerOption2;

    @Column(name = "answer_option_3")
    @NotBlank(message = "Answer option 3 is required")

    private String answerOption3;

    @Column(name = "answer_option_4")
    @NotBlank(message = "Answer option 4 is required")

    private String answerOption4;

    @Column(name = "correct_answer")
    @Min(value = 1, message = "Correct answer must be between 1 and 4")
    @Max(value = 4, message = "Correct answer must be between 1 and 4")
    private int correctAnswer;

}