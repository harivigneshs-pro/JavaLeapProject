package com.example.OnlineExaminationSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.OnlineExaminationSystem.entity.Result;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.repository.ResultRepository;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    public List<Result> getResultsByUser(User user) {
        return resultRepository.findByUser(user);
    }
    
    public List<Result> getResultsByExam(Exam exam) {
        return resultRepository.findByExam(exam);
    }
    
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }
}

