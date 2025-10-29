package com.example.OnlineExaminationSystem.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.repository.ExamRepository;
import com.example.OnlineExaminationSystem.repository.QuestionRepository;

@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    public List<Question> getQuestionsByExamId(Long examId) {
        logger.info("Fetching questions for exam ID: {}", examId);
        List<Question> questions = questionRepository.findByExamId(examId);
        logger.info("Found {} questions for exam ID: {}", questions.size(), examId);
        return questions;
    }    public Optional<Question> addQuestion(Question question) {
        // Validate that the exam exists and attach the managed entity to the question
        if (question.getExam() == null || question.getExam().getId() == null) {
            logger.warn("Attempted to add question without exam or exam id");
            return Optional.empty();
        }

        Long examId = question.getExam().getId();
        Optional<Exam> examOpt = examRepository.findById(examId);
        if (examOpt.isEmpty()) {
            logger.warn("Exam not found for id: {}", examId);
            return Optional.empty();
        }

        // Attach managed Exam and save
        question.setExam(examOpt.get());
        Question saved = questionRepository.save(question);
        logger.info("Saved question id={} for exam id={}", saved.getId(), examId);
        return Optional.of(saved);
    }
}
