package com.example.OnlineExaminationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.service.AuthService;
import com.example.OnlineExaminationSystem.service.ExamService;

@Controller
@RequestMapping("/faculty")
@PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
public class FacultyController {

    @Autowired
    private ExamService examService;
    
    @Autowired
    private AuthService authService;

    @GetMapping("/dashboard")
    public String facultyDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.findByUsername(username).orElse(null);
        
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("exams", examService.getAllExams());
        }
        
        return "faculty_dashboard";
    }
}