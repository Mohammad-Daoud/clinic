package com.project.clinic.exceptions;

public class ExamNotFoundException extends RuntimeException {
    public ExamNotFoundException(Long examId) {
        super("Exam with ID " + examId + " not found.");
    }
}
