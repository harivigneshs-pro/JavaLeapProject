package com.example.OnlineExaminationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.OnlineExaminationSystem.dto.ExamSubmissionDTO;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.entity.Result;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.service.AuthService;
import com.example.OnlineExaminationSystem.service.ExamService;
import com.example.OnlineExaminationSystem.repository.QuestionRepository;
import com.example.OnlineExaminationSystem.repository.ResultRepository;

import java.util.Optional;

import java.util.List;

@Controller
@RequestMapping("/exams")
public class ExamController {
    @Autowired
    private ExamService examService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private ResultRepository resultRepository;

    @PostMapping("/create")
    public String createExam(Exam exam, Authentication authentication, RedirectAttributes redirectAttributes) {
        exam.setCreatedBy(authentication.getName());
        examService.createExam(exam);
        redirectAttributes.addFlashAttribute("message", "Exam created successfully!");
        return "redirect:/faculty/dashboard";
    }

    @GetMapping("/{id}/start")
    public String startExam(@PathVariable Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        Exam exam = examService.getExamById(id).orElse(null);
        User user = authService.findByUsername(authentication.getName()).orElse(null);
        
        if (exam == null || user == null) {
            redirectAttributes.addFlashAttribute("error", "Exam not found or user not authenticated");
            return "redirect:/student/dashboard";
        }
        
        // Check if student has already taken this exam
        Optional<Result> existingResult = resultRepository.findByUserAndExam(user, exam);
        if (existingResult.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "You have already completed this exam. Score: " + 
                existingResult.get().getScore() + "/" + existingResult.get().getTotalQuestions());
            return "redirect:/student/dashboard";
        }
        
        List<Question> questions = questionRepository.findByExamId(id);
        if (questions.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "This exam has no questions yet");
            return "redirect:/student/dashboard";
        }
        
        model.addAttribute("exam", exam);
        model.addAttribute("questions", questions);
        model.addAttribute("user", user);
        
        return "exam_interface";
    }

    @PostMapping("/submit")
    public String submitExam(ExamSubmissionDTO submission, RedirectAttributes redirectAttributes) {
        try {
            Result result = examService.gradeExam(submission);
            redirectAttributes.addFlashAttribute("message", 
                String.format("Exam submitted! Score: %d/%d (%.1f%%)", 
                    result.getScore(), result.getTotalQuestions(), result.getPercentage()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting exam: " + e.getMessage());
        }
        return "redirect:/student/dashboard";
    }
}