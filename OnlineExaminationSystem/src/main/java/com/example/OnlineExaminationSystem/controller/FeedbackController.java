package com.example.OnlineExaminationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.OnlineExaminationSystem.entity.Feedback;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.service.FeedbackService;
import com.example.OnlineExaminationSystem.service.AuthService;
import com.example.OnlineExaminationSystem.service.ExamService;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private ExamService examService;

    @PostMapping("/submit")
    public String submitFeedback(Feedback feedback, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User user = authService.findByUsername(authentication.getName()).orElse(null);
            if (user != null) {
                feedback.setUser(user);
                feedbackService.submitFeedback(feedback);
                redirectAttributes.addFlashAttribute("message", "Feedback submitted successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting feedback: " + e.getMessage());
        }
        return "redirect:/student/dashboard";
    }
    
    @GetMapping
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("exams", examService.getAllExams());
        return "feedback_form";
    }
}

