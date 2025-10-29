package com.example.OnlineExaminationSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.service.QuestionService;
import com.example.OnlineExaminationSystem.service.ExamService;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private ExamService examService;

    @GetMapping("/exam/{examId}")
    public String getQuestionsByExam(@PathVariable Long examId, Model model) {
        List<Question> questions = questionService.getQuestionsByExamId(examId);
        Exam exam = examService.getExamById(examId).orElse(null);
        
        model.addAttribute("questions", questions);
        model.addAttribute("exam", exam);
        return "questions";
    }

    @PostMapping("/add")
    public String addQuestion(Question question, RedirectAttributes redirectAttributes) {
        try {
            questionService.addQuestion(question);
            redirectAttributes.addFlashAttribute("message", "Question added successfully!");
            return "redirect:/questions/create/" + question.getExam().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding question: " + e.getMessage());
            return "redirect:/faculty/dashboard";
        }
    }
    
    @GetMapping("/create/{examId}")
    public String showCreateForm(@PathVariable Long examId, Model model) {
        Exam exam = examService.getExamById(examId).orElse(null);
        if (exam == null) {
            return "redirect:/faculty/dashboard";
        }
        
        Question question = new Question();
        question.setExam(exam);
        
        model.addAttribute("question", question);
        model.addAttribute("exam", exam);
        return "create_question";
    }
}

