package com.example.OnlineExaminationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.entity.Result;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.service.AuthService;
import com.example.OnlineExaminationSystem.service.ExamService;
import com.example.OnlineExaminationSystem.service.ResultService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
public class StudentController {

    @Autowired
    private ExamService examService;
    
    @Autowired
    private ResultService resultService;
    
    @Autowired
    private AuthService authService;

    @GetMapping("/dashboard")
    public String studentDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.findByUsername(username).orElse(null);
        
        if (user != null) {
            List<Result> userResults = resultService.getResultsByUser(user);
            List<Exam> allExams = examService.getAllExams();
            
            // Filter out exams already taken by the student
            List<Exam> availableExams = allExams.stream()
                .filter(exam -> userResults.stream()
                    .noneMatch(result -> result.getExam().getId().equals(exam.getId())))
                .collect(Collectors.toList());
            
            model.addAttribute("user", user);
            model.addAttribute("exams", availableExams);
            model.addAttribute("results", userResults);
            model.addAttribute("completedExams", userResults.size());
        }
        
        return "student_dashboard";
    }
}