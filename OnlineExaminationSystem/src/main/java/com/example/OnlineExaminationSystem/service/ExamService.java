package com.example.OnlineExaminationSystem.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.OnlineExaminationSystem.dto.ExamSubmissionDTO;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.entity.Result;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.repository.ExamRepository;
import com.example.OnlineExaminationSystem.repository.QuestionRepository;
import com.example.OnlineExaminationSystem.repository.ResultRepository;
import com.example.OnlineExaminationSystem.repository.UserRepository;

@Service
public class ExamService {
    private static final Logger logger = LoggerFactory.getLogger(ExamService.class);

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }
    
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }
    
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    @Transactional
    public Result gradeExam(ExamSubmissionDTO submission) {
        // Get exam and user
        Exam exam = examRepository.findById(submission.getExamId())
            .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
        User user = userRepository.findById(submission.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
        // Check if user already submitted this exam
        Optional<Result> existingResult = resultRepository.findByUserAndExam(user, exam);
        if (existingResult.isPresent()) {
            throw new IllegalStateException("User has already submitted this exam");
        }

        // Get all questions for this exam
        List<Question> questions = questionRepository.findByExamId(submission.getExamId());
        if (questions.isEmpty()) {
            throw new IllegalStateException("No questions found for exam " + submission.getExamId());
        }

        // Calculate score
        int correctAnswers = 0;
        for (ExamSubmissionDTO.AnswerDTO answer : submission.getAnswers()) {
            Optional<Question> questionOpt = questions.stream()
                .filter(q -> q.getId().equals(answer.getQuestionId()))
                .findFirst();

            if (questionOpt.isPresent() && 
                questionOpt.get().getCorrectAnswer().equals(answer.getSelectedAnswer())) {
                correctAnswers++;
            }
        }

        // Calculate percentage
        double percentage = (double) correctAnswers / questions.size() * 100;

        // Create and save result
        Result result = new Result();
        result.setUser(user);
        result.setExam(exam);
        result.setScore(correctAnswers);
        result.setPercentage(percentage);
        result.setTotalQuestions(questions.size());

        logger.info("Exam {} graded for user {}: score={}/{}, percentage={}", 
            exam.getTitle(), user.getUsername(), correctAnswers, questions.size(), percentage);

        return resultRepository.save(result);
    }
}
