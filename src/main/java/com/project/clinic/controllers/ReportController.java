package com.project.clinic.controllers;


import com.project.clinic.dto.ReportDTO;
import com.project.clinic.models.Exam;
import com.project.clinic.models.PaymentType;
import com.project.clinic.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/report")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ReportController {

    private final ExamService examService;

    @Autowired
    public ReportController(ExamService examService) {
        this.examService = examService;
    }
    @GetMapping
    public String generateReport(@ModelAttribute ReportDTO reportDTO, Model model) {
        List<Exam> examinations = examService.findExaminationsBetweenDates(reportDTO.getStartDate(), reportDTO.getEndDate());

        // Calculate totals
        long totalVisits = examinations.size();
        long totalInsuranceVisits = examinations.stream().filter(e -> e.getPaymentType() == PaymentType.INSURANCE).count();
        long totalCashVisits = examinations.stream().filter(e -> e.getPaymentType() == PaymentType.CASH).count();
        double totalCashAmount = examinations.stream()
                .filter(e -> e.getPaymentType().equals(PaymentType.CASH))
                .mapToDouble(exam -> exam.getPrice().doubleValue())
                .sum();
        double totalInsuranceAmount = examinations.stream()
                .filter(e -> e.getPaymentType().equals(PaymentType.INSURANCE))
                .mapToDouble(exam -> exam.getPrice().doubleValue())
                .sum();


        // Add data to the model
        model.addAttribute("examinations", examinations);
        model.addAttribute("totalInsuranceVisits",totalInsuranceVisits);
        model.addAttribute("totalCashVisits",totalCashVisits);
        model.addAttribute("totalVisits", totalVisits);

        model.addAttribute("totalCashAmount", totalCashAmount);
        model.addAttribute("totalInsuranceAmount", totalInsuranceAmount);
        model.addAttribute("totalAmount", totalCashAmount+totalInsuranceAmount);

        return "report"; // Return the report.html view
    }

}
