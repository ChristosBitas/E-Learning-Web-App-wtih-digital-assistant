package com.eLearningWebApp.eLearningWebApp.repository;

import com.eLearningWebApp.eLearningWebApp.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}