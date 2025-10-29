package com.example.OnlineExaminationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.OnlineExaminationSystem.service.ExamService;
import com.example.OnlineExaminationSystem.service.ResultService;
import com.example.OnlineExaminationSystem.repository.UserRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ExamService examService;
    
    @Autowired
    private ResultService resultService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("results", resultService.getAllResults());
        model.addAttribute("users", userRepository.findAll());
        return "admin_dashboard";
    }
}