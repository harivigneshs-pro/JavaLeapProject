package com.example.OnlineExaminationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OnlineExaminationSystem.entity.Result;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.entity.Exam;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
	List<Result> findByUser(User user);
	List<Result> findByExam(Exam exam);
	Optional<Result> findByUserAndExam(User user, Exam exam);
}
