package com.project.clinic.services;

import com.project.clinic.exceptions.ClientNotFoundException;
import com.project.clinic.exceptions.ExamNotFoundException;
import com.project.clinic.models.Client;
import com.project.clinic.models.Exam;
import com.project.clinic.models.PaymentType;
import com.project.clinic.repositories.ClientRepository;
import com.project.clinic.repositories.ExamRepository;
import com.project.clinic.utils.ClientsUtils;
import com.project.clinic.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, ClientRepository clientRepository) {
        this.examRepository = examRepository;
        this.clientRepository = clientRepository;
    }

    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ExamNotFoundException(id));
    }

    public Long getClientIdByExamId(Long examId) {
        return getExamById(examId).getClient().getId();
    }

    public void addExam(Exam exam, Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));
        exam.setId(null);
        exam.setClient(client);
        exam.setDateLastExam(LocalDate.now());
        if(exam.getPrice() == null){
            exam.setPrice(new BigDecimal(0));
        }
        if (exam.getPaymentType() == null){
           exam.setPaymentType(PaymentType.CASH);
        }
        String treatment = exam.getTreatment();
        if (treatment !=null){
            treatment = StringUtils.capitalizeFirstWordInLine(treatment);
        }
        exam.setTreatment(treatment);
        examRepository.save(exam);
        setClientStatus(clientId);
    }


    public void updateExam(Exam exam) {
        Exam existingExam = getExamById(exam.getId());

        existingExam.updateExamDetails(exam);
        examRepository.save(existingExam);
        setClientStatus(getClientIdByExamId(existingExam.getId()));
    }

    public void deleteExamById(Long examId) {
        Long clientId = getClientIdByExamId(examId);
        if (!examRepository.existsById(examId)) {
            throw new ExamNotFoundException(examId);
        }
        examRepository.deleteById(examId);
        setClientStatus(clientId);
    }

    private void setClientStatus(Long clientId){
        Client client = clientRepository.findById(clientId)
                .orElseThrow(()->new ClientNotFoundException(clientId));
        client.setStatus(ClientsUtils.calculatePatientStatus(client));
        clientRepository.save(client);
    }

    public List<Exam> findExaminationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return examRepository.findExaminationsBetweenDates(startDate, endDate);
    }
}
