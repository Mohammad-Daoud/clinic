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
    @Lob
    private String symptoms;
    @Lob
    private  String diagnosis;
    @Lob
    private String management;
    @Lob
    private String treatment;
    private LocalDate dateOfReExamination;
    private LocalDate dateLastExam;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    public void updateExamDetails(Exam updatedExam) {
        this.symptoms = updatedExam.getSymptoms();
        this.diagnosis = updatedExam.getDiagnosis();
        this.management = updatedExam.getManagement();
        this.treatment = updatedExam.getTreatment();
        this.dateOfReExamination = updatedExam.getDateOfReExamination();
        if(updatedExam.getPrice() == null){
            updatedExam.setPrice(new BigDecimal(0));
        }
        this.price = updatedExam.getPrice();
        if (updatedExam.getPaymentType() == null){
            this.paymentType = PaymentType.CASH;
        }else {
            this.paymentType = updatedExam.getPaymentType();
        }
    }
}

