package com.example.OnlineExaminationSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.OnlineExaminationSystem.entity.Result;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.service.ResultService;
import com.example.OnlineExaminationSystem.service.AuthService;
import com.example.OnlineExaminationSystem.service.ExamService;

@Controller
@RequestMapping("/results")
public class ResultController {
    @Autowired
    private ResultService resultService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private ExamService examService;

    @GetMapping
    public String getAllResults(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = authService.findByUsername(username).orElse(null);
        
        if (user != null && "STUDENT".equals(user.getRole())) {
            List<Result> results = resultService.getResultsByUser(user);
            model.addAttribute("results", results);
            model.addAttribute("user", user);
            return "results";
        } else {
            // For admin/faculty - show all results
            List<Result> results = resultService.getAllResults();
            model.addAttribute("results", results);
            return "results";
        }
    }
    
    @GetMapping("/exam/{examId}")
    public String getResultsByExam(@PathVariable Long examId, Model model) {
        Exam exam = examService.getExamById(examId).orElse(null);
        if (exam != null) {
            List<Result> results = resultService.getResultsByExam(exam);
            model.addAttribute("results", results);
            model.addAttribute("exam", exam);
        }
        return "results";
    }
}

