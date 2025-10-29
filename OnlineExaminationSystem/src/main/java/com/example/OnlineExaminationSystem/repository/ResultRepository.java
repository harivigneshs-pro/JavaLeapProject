package com.example.OnlineExaminationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OnlineExaminationSystem.entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> { }
