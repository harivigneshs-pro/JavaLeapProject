package com.example.OnlineExaminationSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/exam_start.html")
    public String examStart() {
        return "exam_start";  
    }

    @GetMapping("/login.html")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/faculty_dashboard.html")
    public String facultyDashboard() {
        return "faculty_dashboard";
    }

    @GetMapping("/exam.html")
    public String examPage() {
        return "exam";
    }

    @GetMapping("/results.html")
    public String resultsPage() {
        return "results";
    }
}
