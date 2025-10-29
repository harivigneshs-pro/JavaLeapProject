package com.example.OnlineExaminationSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/exam_start")
    public String examStart() {
        return "exam_start";  // Thymeleaf will look for exam_start.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/faculty_dashboard")
    public String facultyDashboard() {
        return "faculty_dashboard";
    }
}
