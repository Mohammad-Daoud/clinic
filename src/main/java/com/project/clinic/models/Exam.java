package com.project.clinic.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    private String symptoms;
    private  String diagnosis;
    private String management;
    private String treatment;
    private LocalDate dateOfReExamination;
    private LocalDate dateLastExam;
    private BigDecimal price;


    public void updateExamDetails(Exam updatedExam) {
        this.symptoms = updatedExam.getSymptoms();
        this.diagnosis = updatedExam.getDiagnosis();
        this.management = updatedExam.getManagement();
        this.treatment = updatedExam.getTreatment();
        this.dateOfReExamination = updatedExam.getDateOfReExamination();
    }
}

