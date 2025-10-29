package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.entity.ExamAttempt;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.repository.ExamAttemptRepository;
import com.example.OnlineExaminationSystem.service.AuthService;
import com.example.OnlineExaminationSystem.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/exam-monitor")
public class ExamMonitorController {

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ExamService examService;

    @PostMapping("/violation")
    public ResponseEntity<?> reportViolation(@RequestParam Long examId, 
                                           @RequestParam String violationType,
                                           Authentication authentication) {
        try {
            User user = authService.findByUsername(authentication.getName()).orElse(null);
            Exam exam = examService.getExamById(examId).orElse(null);

            if (user == null || exam == null) {
                return ResponseEntity.badRequest().body("Invalid user or exam");
            }

            Optional<ExamAttempt> attemptOpt = examAttemptRepository.findByUserAndExamAndIsTerminatedFalse(user, exam);
            ExamAttempt attempt;

            if (attemptOpt.isPresent()) {
                attempt = attemptOpt.get();
            } else {
                attempt = new ExamAttempt();
                attempt.setUser(user);
                attempt.setExam(exam);
            }

            attempt.setViolationCount(attempt.getViolationCount() + 1);
            attempt.setViolationReason(violationType);

            if (attempt.getViolationCount() == 1 && !attempt.isHasUsedGraceChance()) {
                // First violation - give grace chance
                attempt.setHasUsedGraceChance(true);
                examAttemptRepository.save(attempt);
                return ResponseEntity.ok().body("{\"action\":\"warning\",\"message\":\"Warning! This is your only grace chance. Next violation will terminate the exam.\"}");
            } else {
                // Second violation or no grace chance - terminate exam
                attempt.setTerminated(true);
                attempt.setTerminatedAt(LocalDateTime.now());
                examAttemptRepository.save(attempt);
                return ResponseEntity.ok().body("{\"action\":\"terminate\",\"message\":\"Exam terminated due to violation: " + violationType + "\"}");
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing violation");
        }
    }

    @GetMapping("/status/{examId}")
    public ResponseEntity<?> getExamStatus(@PathVariable Long examId, Authentication authentication) {
        try {
            User user = authService.findByUsername(authentication.getName()).orElse(null);
            Exam exam = examService.getExamById(examId).orElse(null);

            if (user == null || exam == null) {
                return ResponseEntity.badRequest().body("Invalid user or exam");
            }

            Optional<ExamAttempt> attemptOpt = examAttemptRepository.findByUserAndExam(user, exam);
            
            if (attemptOpt.isPresent() && attemptOpt.get().isTerminated()) {
                return ResponseEntity.ok().body("{\"status\":\"terminated\",\"reason\":\"" + attemptOpt.get().getViolationReason() + "\"}");
            }

            return ResponseEntity.ok().body("{\"status\":\"active\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error checking exam status");
        }
    }
}