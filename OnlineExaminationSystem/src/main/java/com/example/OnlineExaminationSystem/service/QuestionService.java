package com.example.OnlineExaminationSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.OnlineExaminationSystem.entity.Question;
import com.example.OnlineExaminationSystem.repository.QuestionRepository;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    
    
    public List<Question> getQuestionsByExamId(Long examId) {
    return questionRepository.findByExamId(examId);
}


    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }
}
