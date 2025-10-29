package com.example.OnlineExaminationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OnlineExaminationSystem.entity.Feedback;
import com.example.OnlineExaminationSystem.service.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/submit")
    public Feedback submit(@RequestBody Feedback feedback) {
        return feedbackService.submitFeedback(feedback);
    }
}

