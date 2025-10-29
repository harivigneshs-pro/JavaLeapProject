package com.example.OnlineExaminationSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OnlineExaminationSystem.entity.Result;
import com.example.OnlineExaminationSystem.service.ResultService;

@RestController
@RequestMapping("/api/results")
public class ResultController {
    @Autowired
    private ResultService resultService;

    @PostMapping("/save")
    public Result save(@RequestBody Result result) {
        return resultService.saveResult(result);
    }

    @GetMapping("/{userId}")
    public List<Result> getUserResults(@PathVariable Long userId) {
        return resultService.getResultsByUser(userId);
    }
}

