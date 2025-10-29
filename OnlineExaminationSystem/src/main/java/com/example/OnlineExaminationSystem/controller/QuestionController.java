package com.example.OnlineExaminationSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "http://localhost:8080") // Important for frontend calls
public class QuestionController {
    @Autowired
    private QuestionService questionService;

   /* @GetMapping("/")
    public List<Question> getAll() {
        return questionService.getAllQuestions();
    }*/
    @GetMapping("/exam/{examId}")
public List<Question> getQuestionsByExam(@PathVariable Long examId) {
     System.out.println("Fetching questions for examId: " + examId);
    return questionService.getQuestionsByExamId(examId);
}


    @PostMapping("/add")
    public Question add(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }
}

