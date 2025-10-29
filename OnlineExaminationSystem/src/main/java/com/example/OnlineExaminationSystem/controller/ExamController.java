package com.example.OnlineExaminationSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.service.ExamService;
@RestController
@RequestMapping("/api/exams")
public class ExamController {
    @Autowired
    private ExamService examService;

    @PostMapping("/create")
    public Exam create(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @GetMapping("/")
    public List<Exam> getAll() {
        return examService.getAllExams();
    }
}

