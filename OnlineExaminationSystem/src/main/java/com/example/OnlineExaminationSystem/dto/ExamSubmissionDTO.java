package com.example.OnlineExaminationSystem.dto;

import java.util.List;
import lombok.Data;

@Data
public class ExamSubmissionDTO {
    private Long userId;
    private Long examId;
    private List<AnswerDTO> answers;

    @Data
    public static class AnswerDTO {
        private Long questionId;
        private String selectedAnswer;
    }
}