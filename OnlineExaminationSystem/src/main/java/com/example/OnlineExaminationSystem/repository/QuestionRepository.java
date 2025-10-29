package com.example.OnlineExaminationSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OnlineExaminationSystem.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> { 
    List<Question> findByExamId(Long examId);

}
